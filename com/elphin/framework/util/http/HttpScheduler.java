package com.elphin.framework.util.http;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.util.ArrayList;

/**
 * Http网络调度器
 * 
 * @author yuankai
 * @version 1.0
 * @data 2012-7-29
 */
public class HttpScheduler
{
//	private final static String TAG = "httpTask";
	/**
	 * 网络模块上下文
	 */
	private Context mContext = null;
	
	/**
	 * 最大并发线程数
	 */
	private int mMaxConcurrentCount = 2;
	
	/**
	 * 等待队列
	 */
	private ArrayList<HttpTask> mWaitingList = new ArrayList<HttpTask>();
	
	/**
	 * 正在运行的任务
	 */
	private ArrayList<AsyncConnectThread> mRunningTasks = new ArrayList<HttpScheduler.AsyncConnectThread>();

	/**
	 * 调度器构造方法
	 * @param context 上下文
	 * @param maxConcurrentCount 最大并发线程数
	 */
	public HttpScheduler(Context context, int maxConcurrentCount)
	{
		mContext = context;
		mMaxConcurrentCount = maxConcurrentCount;

		for(int i = 0;i < mMaxConcurrentCount; ++i)
		{
			mRunningTasks.add(new AsyncConnectThread());
		}
	}

	/**
	 * 将任务加入到网络队列中
	 * @param task 任务
	 * @return 如果参数不正确，则启动失败
	 */
	public boolean asyncConnect(HttpTask task)
	{
		if(task == null || task.getHttpUriRequest() == null)return false;
		// 将任务加入到队首，后进先出
		synchronized(mWaitingList)
		{
			mWaitingList.add(0, task);
		}

		// 如果当前有空档线程，则启动之
		synchronized(mRunningTasks)
		{
			final int runningSize = mRunningTasks.size();
			for(int i = 0;i < runningSize; ++i)
			{
				final AsyncConnectThread runningTask = mRunningTasks.get(i);
				if(!runningTask.isRunning())
				{
					runningTask.setRunning(true);
					runningTask.start();
					break;
				}
			}
		}

		return true;
	}

	/**
	 * 停止网络任务，停止之后不会回调
	 * @param task 任务
	 */
	public void cancel(HttpTask task)
	{
		if(task == null)return;

		// 判断waitinglist中是否有此任务
		synchronized(mWaitingList)
		{
			int waitSize = mWaitingList.size();
			for(int i = 0;i < waitSize; ++i)
			{
				if(mWaitingList.get(i) == task)
				{
					mWaitingList.remove(i);
					-- i;
					-- waitSize;
				}
			}
		}

		// 判断正在运行的任务是否有此任务
		synchronized(mRunningTasks)
		{
			final int size = mRunningTasks.size();
			for(int i = 0;i < size; ++i)
			{
				AsyncConnectThread thread = mRunningTasks.get(i);
				if(thread.getCurrentTask() == task)
				{
					thread.abortTask();
				}
			}
		}
	}

	public void release()
	{
		// 停止所有线程，停止所有队列
		synchronized(mWaitingList)
		{
			mWaitingList.clear();
		}
		synchronized(mRunningTasks)
		{
			for(int i = 0;i < mRunningTasks.size(); ++i)
			{
				mRunningTasks.get(i).cancel();
				mRunningTasks.set(i, new AsyncConnectThread());	// replace with new object
			}
		}
	}

	/**
	 * 下载任务类
	 * @author yuankai
	 * @version 1.0
	 * @data 2012-7-10
	 */
	class AsyncConnectThread extends Thread
	{
		private HttpTask mHttpTask = null;		// 当前正在处理的任务
		private volatile boolean isRunning = false;
		private volatile boolean isCancel = false;
		private volatile boolean isAbort = false;
		private DefaultHttpClient mHttpClient = new DefaultHttpClient();
		private HttpParams mHttpParams = new BasicHttpParams();

		public void run()
		{
			while(!isCancel)
			{
				synchronized(mWaitingList)
				{
					if(mWaitingList.size() > 0)
					{
						mHttpTask = mWaitingList.remove(0);
					}
					else
					{
						break;
					}
				}

				HttpCallBack callBack = mHttpTask.getCallBack();
				try
				{
					HttpResponse httpResponse = doConnect(mHttpTask);
					if(callBack != null && !isCancel && !isAbort)
					{
						callBack.onCallBack(mHttpTask, HttpCallBack.SUCCESS, httpResponse);
					}
				}
				catch(Exception e)
				{
					if(callBack != null && !isCancel && !isAbort)
					{
						callBack.onCallBack(mHttpTask, HttpCallBack.EXCEPTION, e);
					}
				}
				
				isAbort = false;
				mHttpTask = null;
			}
			
			synchronized(mRunningTasks)
			{
				mRunningTasks.remove(this);
				mRunningTasks.add(new AsyncConnectThread());
			}
			isRunning = false;
			isCancel = false;
		}
		
		public void abortTask()
		{
			isAbort = true;
			if(mHttpTask != null)
			{
				mHttpTask.getHttpUriRequest().abort();
			}
			
			if(mHttpParams != null)
			{
				HttpConnectionParams.setConnectionTimeout(mHttpParams, 1);
				HttpConnectionParams.setSoTimeout(mHttpParams, 1);
			}
		}
		
		protected HttpResponse doConnect(HttpTask task) throws Exception
		{
			mHttpParams = new BasicHttpParams();
			HttpProtocolParams.setVersion(mHttpParams, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(mHttpParams, "utf-8");
			HttpConnectionParams.setConnectionTimeout(mHttpParams, task.getConnectTimeout());
			HttpConnectionParams.setSoTimeout(mHttpParams, task.getSocketTimeout());
			// 加入代理
			HttpUtils.fillProxy(mContext, mHttpParams);
			mHttpClient = new DefaultHttpClient(mHttpParams);
			// 重试一次
			mHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
			HttpResponse httpResponse = mHttpClient.execute(task.getHttpUriRequest());
			
			return httpResponse;
		}
		
		public void setRunning(boolean running)
		{
			isRunning = running;
		}
		
		/**
		 * 获取当前任务
		 * @return 任务
		 */
		public HttpTask getCurrentTask()
		{
			return mHttpTask;
		}
		
		/**
		 * 停止任务
		 */
		public void cancel()
		{
			isCancel = true;
		}
		
		/**
		 * 线程是否在运行中
		 * @return
		 */
		public boolean isRunning()
		{
			return isRunning;
		}
	};
}