package com.elphin.framework.util.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.params.HttpParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 网络工具类
 * 
 * @author elphin
 * @version 1.0
 * @date 2011-1-19
 */
public class HttpUtils
{
	public static final int TYPE_WAP = 1;
	public static final int TYPE_NET = 2;
	public static final int TYPE_UNKNOWN = 3;
	
	public static final String WAP = "wap";
	public static final String NET = "net";
	public final static String http = "http://";
	public final static String https = "https://";
	public static final String PROXY_IP = "10.0.0.172";
	
	/**
	 * 默认的代理端口号
	 */
	public final static int DEFAULT_PROXY_PORT = 80;
	
	public final static int HTTP_OK_CODE = 202;
	
	/**
	 * build parameter list string in http url. Eg. k1=v1&k2=v2...
	 * 
	 * @param params
	 *            list of key-value pair.
	 * @return Return the parameter string in url.
	 */
	public static String buildParamListInHttpRequest(List<NameValuePair> params)
	{
		
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < params.size(); index++)
		{
			sb.append(params.get(index).getName());
			sb.append("=");
			sb.append(params.get(index).getValue());
			
			if (index < params.size() - 1)
			{
				
				sb.append("&");
			}
		}
		return sb.toString();
	}
	
	public static boolean isHttp(final String s) {
		if (s == null) {
			return false;
		}
		return s.startsWith(http);
	}

	public static boolean isHttps(final String s) {
		if (s == null) {
			return false;
		}
		return s.startsWith(https);
	}
	
	public static boolean isWap(final Context context)
	{
		if(context == null)
		{
			return false;
		}		
 
		final NetworkInfo info = NetworkUtil.getActiveNetworkInfo(context);    
		if(info != null && info.getExtraInfo() != null)
		{
			return info.getExtraInfo().endsWith(WAP);
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 获取是否是wap或者net
	 * @param context
	 * @return
	 */
	public static int getNetType(final Context context)
	{
		if(context == null)
		{
			return TYPE_UNKNOWN;
		}
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
		final NetworkInfo info = connectivityManager.getActiveNetworkInfo();    
		if(info != null && info.getExtraInfo() != null)
		{
			final String extraInfo = info.getExtraInfo();
			if(extraInfo.endsWith(WAP))
			{
				return TYPE_WAP;
			}
			else if(extraInfo.endsWith(NET))
			{
				return TYPE_NET;
			}
			else
			{
				return TYPE_UNKNOWN;
			}
		}
		else
		{
			return TYPE_UNKNOWN;
		}
	}
	
	/**
	 * 根据当前网络状态填充代理
	 * @param context
	 * @param httpParams
	 */
	public static void fillProxy(final Context context, final HttpParams httpParams)
	{		
		final NetworkInfo networkInfo = NetworkUtil.getActiveNetworkInfo(context);
		if (networkInfo == null || networkInfo.getExtraInfo() == null) {
            return;
        }
        String info = networkInfo.getExtraInfo().toLowerCase(); //3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap 
        // 先根据网络apn信息判断,并进行 proxy 自动补齐
        if (info != null) {
            if (info.startsWith("cmwap") || info.startsWith("uniwap") || info.startsWith("3gwap")) {
                HttpHost proxy = new HttpHost("10.0.0.172", 80);
    			httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
                return;
            } else if (info.startsWith("ctwap")) {
                HttpHost proxy = new HttpHost("10.0.0.200", 80);
    			httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
                return;
            } else if (info.startsWith("cmnet") || info.startsWith("uninet") || info.startsWith("ctnet")
                    || info.startsWith("3gnet")) {
                return;
            } // else fall through
        } // else fall through
        
        // 如果没有 apn 信息，则根据 proxy代理判断。
        // 由于android 4.2 对 "content://telephony/carriers/preferapn" 读取进行了限制，我们通过系统接口获取。
        
        // 绝大部分情况下不会走到这里
        // 此两个方法是deprecated的，但在4.2下仍可用
        String defaultProxyHost = android.net.Proxy.getDefaultHost();	
        int defaultProxyPort = android.net.Proxy.getDefaultPort();
        
        if (defaultProxyHost != null && defaultProxyHost.length() > 0) {
            /*
             * 无法根据  proxy host 还原 apn 名字 这里不设置  mApn
             */
            if ("10.0.0.172".equals(defaultProxyHost.trim())) {
                // 当前网络连接类型为cmwap || uniwap
                HttpHost proxy = new HttpHost("10.0.0.172", defaultProxyPort);
    			httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
            } else if ("10.0.0.200".equals(defaultProxyHost.trim())) {
                HttpHost proxy = new HttpHost("10.0.0.200", 80);
    			httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
            } else {
            }
        } else {
            // 其它网络都看作是net
        }
	}
	
//	public static void fillProxy(final Context context, final HttpParams httpParams)
//	{
//			// 用APN的方式去获取
//			try
//			{
//				Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // 获取当前正在使用的APN接入点
//				Cursor mCursor = null;
//				try
//				{
//					mCursor = context.getContentResolver().query(uri, null, null, null, null);
//					if (mCursor != null) 
//					{
//						boolean b = mCursor.moveToNext(); // 游标一直第一条记录，当前只有一条
//						if (b) 
//						{
//							String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));// 有可能报错
//							if (proxyStr != null && proxyStr.trim().length() > 0) 
//							{
//								HttpHost proxy = new HttpHost(proxyStr, DEFAULT_PROXY_PORT);
//								httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
//							}
//						}
//					}
//				}
//				finally
//				{
//					if(mCursor != null && !mCursor.isClosed())
//					{
//						mCursor.close();
//					}
//				}
//			}
//			catch(Exception e)
//			{
//			}
//	}
	
	/**
	 * parse integer text and get positive integer from it.
	 * 
	 * @param intValue
	 *            integer string
	 * 
	 * @return positive integer and zero. return zero if exception or parsed
	 *         integer is negative.
	 */
	public static int safePositiveInteger(String intValue)
	{
		int value = 0;
		try
		{
			value = Integer.parseInt(intValue);
			if (value < 0)
			{
				value = 0;
			}
		}
		catch (NumberFormatException e)
		{
			value = 0;
		}
		return value;
	}
	
	public static Date strToDate(String str) throws ParseException
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(str);
	}
	
	/**
	 * parse long text and get positive integer from it.
	 * 
	 * @param longValue
	 *            long string
	 * 
	 * @return positive long and zero. return zero if exception or parsed long
	 *         is negative.
	 */
	public static long safePositiveLong(String longValue)
	{
		long value = 0;
		try
		{
			value = Long.parseLong(longValue);
			if (value < 0)
			{
				value = 0;
			}
		}
		catch (NumberFormatException e)
		{
			value = 0;
		}
		return value;
	}
	
	/**
	 * 字符串替换
	 * @param strVal 源
	 * @param tagList 需要被替换的字符串列表
	 * @return 替换完成的字符串
	 */
	public static String filterXmlTags(String strVal, List<String> tagList)
	{
		String newVal = strVal;
		if (tagList != null)
		{
			for (String tag : tagList)
			{
				String startTag = "<" + tag + ">";
				String endTag = "</" + tag + ">";
				newVal = newVal.replaceAll(startTag, "");
				newVal = newVal.replaceAll(endTag, "");
			}
		}
		return newVal;
	}
	
	/**
	 * 获取网络类型，GSM等
	 * @param context 上下文
	 * @return 
	 */
	public static String getNetworkType(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = tm.getNetworkType();
		String type = "";
		switch (networkType)
		{
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				type = "1xRTT";
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				type = "CDMA";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				type = "EDGE";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				type = "EVDO 0";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				type = "EVDO A";
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				type = "GPRS";
				break;
//			case TelephonyManager.NETWORK_TYPE_HSDPA:
//				type = "HSDPA";
//				break;
//			case TelephonyManager.NETWORK_TYPE_HSPA:
//				type = "HSPA";
//				break;
//			case TelephonyManager.NETWORK_TYPE_HSUPA:
//				type = "HSUPA";
//				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				type = "UMTS";
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			default:
				type = "UNKNOWN";
				break;
		}
		return type;
	}
	
}
