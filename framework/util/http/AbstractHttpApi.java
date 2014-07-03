
package com.elphin.framework.util.http;

import android.content.Context;

import com.elphin.framework.util.http.exception.AuthorizationException;
import com.elphin.framework.util.http.exception.XmlParserException;
import com.elphin.framework.util.http.exception.XmlParserParseException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author elphin
 *
 */
abstract public class AbstractHttpApi implements HttpApi {

    // protected static final boolean DEBUG = Config.DEBUG;

    private static final String DEFAULT_CLIENT_VERSION = "com.baidu.android";

    private static final String CLIENT_VERSION_HEADER = "User-Agent";

    private static final int TIMEOUT = 60;

    private final DefaultHttpClient mHttpClient;

    private final String mClientVersion;

    public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
        mHttpClient = httpClient;
        if (clientVersion != null) {
            mClientVersion = clientVersion;
        } else {
            mClientVersion = DEFAULT_CLIENT_VERSION;
        }
    }

    /**
     * 执行post
     */
    public String doHttpPost(String url, NameValuePair... nameValuePairs)
            throws AuthorizationException, XmlParserParseException, XmlParserException, IOException {
        HttpPost httpPost = createHttpPost(url, nameValuePairs);

        HttpResponse response = executeHttpRequest(httpPost);
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                try {
                    return EntityUtils.toString(response.getEntity());
                } catch (ParseException e) {
                    throw new XmlParserParseException(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new AuthorizationException(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new XmlParserException(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new XmlParserException(response.getStatusLine().toString());
        }
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     *
     * @param httpRequest
     * @return
     * @throws java.io.IOException
     */
    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        try {
            mHttpClient.getConnectionManager().closeExpiredConnections();
            return mHttpClient.execute(httpRequest);
        } catch (IOException e) {
            httpRequest.abort();
            throw e;
        }
    }

    /**
     * 根据URL地址和参数创建一个httpget对象
     * NameValuePair可以传入key-value对应的参数
     */
    public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
        HttpGet httpGet = null;
        if (nameValuePairs != null && nameValuePairs.length > 0) {
            String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
            httpGet = new HttpGet(url + query);
        } else {
            httpGet = new HttpGet(url);
        }
         httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        httpGet.addHeader("Accept-Encoding", "gzip");
        return httpGet;
    }

    /**
     * 根据URL地址和参数创建一个httpget对象
     * NameValuePair可以传入key-value对应的参数
     */
    public HttpGet createHttpGet(String url, String encode, NameValuePair... nameValuePairs) {
        HttpGet httpGet = null;
        if (nameValuePairs != null && nameValuePairs.length > 0) {
            String query = URLEncodedUtils.format(stripNulls(nameValuePairs), encode);
            httpGet = new HttpGet(url + "?" + query);
        } else {
            httpGet = new HttpGet(url);
        }
         httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        httpGet.addHeader("Accept-Encoding", "gzip");
        return httpGet;
    }

    public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);
         httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1) {
            throw new IllegalArgumentException("Unable to encode http parameters.");
        }
        httpPost.addHeader("Accept-Encoding", "gzip");
        return httpPost;
    }

    public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIMEOUT * 1000);
        conn.setRequestMethod("POST");

         conn.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        return conn;
    }

    private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            NameValuePair param = nameValuePairs[i];
            if (param.getValue() != null) {
                params.add(param);
            }
        }
        return params;
    }

    /**
     * Create a thread-safe client. This client does not do redirecting, to allow us to capture
     * correct "error" codes.
     *
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpClient(Context mContext) {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams(mContext);
        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    public static final DefaultHttpClient createHttpClientSimple(Context mContext) {
        final HttpParams httpParams = createHttpParams(mContext);
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);
        return httpclient;
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams(Context mContext) {
        final HttpParams params = new BasicHttpParams();

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        // 加入代理
        HttpUtils.fillProxy(mContext, params);
        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        return params;
    }

}
