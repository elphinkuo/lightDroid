package com.elphin.framework.util.http;

/**
 * @author elphin
 * @version 1.0
 * @data 2012-7-29
 */
public interface HttpCallBack
{
	/**
	 * 网络请求成功，Obj为HttpResponse
	 */
	public final static int SUCCESS = 1;
	
	/**
	 * 网络请求失败，Obj为Exception异常
	 */
	public final static int EXCEPTION = 2;
	
	/**
	 * 回调方法
	 * @param task http任务
	 * @param event 事件
	 * @param obj 事件附带数据，如果是SUCCESS则为HttpResponse，如果是EXCEPTION则为Exception
	 */
	public void onCallBack(HttpTask task, int event, Object obj);
}
