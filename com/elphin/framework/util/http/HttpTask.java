package com.elphin.framework.util.http;

import org.apache.http.client.methods.HttpUriRequest;

/**
 * http任务描述
 * @author elphin
 * @version 1.0
 * @data 2012-7-29
 */
public class HttpTask
{
	protected HttpUriRequest mHttpUriRequest = null;
	protected int mConnectTimeout = 15000;
	protected int mSocketTimeout = 15000;
	protected HttpCallBack mCallBack = null;

	/**
	 * 默认构造方法
	 */
	public HttpTask()
	{

	}

	/**
	 * 构造方法
	 * @param request 请求描述
	 * @param timeout 超时时间
	 * @param callBack 回调
	 */
	public HttpTask(HttpUriRequest request, HttpCallBack callBack, int connectTimeout, int socketTimeout)
	{
		mHttpUriRequest = request;
		mCallBack = callBack;
		mConnectTimeout = connectTimeout;
		mSocketTimeout = socketTimeout;
	}

	/**
	 * 构造方法
	 * @param request 请求描述
	 * @param callBack 回调
	 */
	public HttpTask(HttpUriRequest request, HttpCallBack callBack)
	{
		this(request, callBack, 15000, 15000);
	}

	/**
	 * 获取对应回调
	 * @return
	 */
	public HttpCallBack getCallBack()
	{
		return mCallBack;
	}
	
	/**
	 * 获取http请求
	 * @return http请求
	 */
	public HttpUriRequest getHttpUriRequest()
	{
		return mHttpUriRequest;
	}
	
	/**
	 * 获取连接超时时间
	 * @return 连接超时时间
	 */
	public int getConnectTimeout()
	{
		return mConnectTimeout;
	}
	
	/**
	 * 获取socket超时
	 * @return
	 */
	public int getSocketTimeout()
	{
		return mSocketTimeout;
	}
	
	
}
