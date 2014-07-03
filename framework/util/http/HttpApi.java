package com.elphin.framework.util.http;

import com.elphin.framework.util.http.exception.AuthorizationException;
import com.elphin.framework.util.http.exception.XmlParserException;
import com.elphin.framework.util.http.exception.XmlParserParseException;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpApi {

    abstract public String doHttpPost(String url,
                                      NameValuePair... nameValuePairs)
            throws AuthorizationException, XmlParserParseException,
            XmlParserException, IOException;

    abstract public HttpGet createHttpGet(String url,
                                          NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url,
                                            NameValuePair... nameValuePairs);

    abstract public HttpURLConnection createHttpURLConnectionPost(URL url,
                                                                  String boundary) throws IOException;
}
