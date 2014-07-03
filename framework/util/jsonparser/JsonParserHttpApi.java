package com.elphin.framework.util.jsonparser;

import com.elphin.framework.util.http.HttpApiWithBasicAuth;
import com.elphin.framework.util.http.exception.AuthorizationException;
import com.elphin.framework.util.http.exception.XmlParserException;
import com.elphin.framework.util.http.exception.XmlParserParseException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class JsonParserHttpApi extends HttpApiWithBasicAuth {

    public JsonParserHttpApi(DefaultHttpClient httpClient, String clientVersion) {
        super(httpClient, clientVersion);
    }

    public BaseObject analysisInputStreamToObject(InputStream inputStream, Parser<? extends BaseObject> parser,
            String encodeing) throws JSONException, IOException, Exception {
        return parser.parse(BaseParser.createJSONParser(inputStream, encodeing));
    }

    public BaseObject analysisHttpResponseToObject(HttpResponse response, Parser<? extends BaseObject> parser,
            String encodeing) throws AuthorizationException, XmlParserParseException, XmlParserException, IOException {

        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
        case 200:
            // InputStream is = response.getEntity().getContent();
            InputStream is = getUngzippedContent(response.getEntity());
            try {
//                 is = UserController.getIS();
                return analysisInputStreamToObject(is, parser, encodeing);
            } catch (JSONException e) {
                throw new XmlParserException(response.getStatusLine().toString(), EntityUtils.toString(response
                        .getEntity()));
            } catch (Exception e) {
                throw new XmlParserException(response.getStatusLine().toString(), EntityUtils.toString(response
                        .getEntity()));
            } finally {
                is.close();
            }

        case 400:
            throw new XmlParserException(response.getStatusLine().toString(),
                    EntityUtils.toString(response.getEntity()));

        case 401:
            response.getEntity().consumeContent();
            throw new AuthorizationException(response.getStatusLine().toString());

        case 404:
            response.getEntity().consumeContent();
            throw new XmlParserException(response.getStatusLine().toString());

        case 500:
            response.getEntity().consumeContent();
            throw new XmlParserException("Internal Server Error");

        default:
            response.getEntity().consumeContent();
            throw new XmlParserException("Error connecting : " + statusCode + ". Try again later.");
        }
    }

    public BaseObject executeHttpRequest(HttpRequestBase httpRequest, Parser<? extends BaseObject> parser,
            String encodeing) throws AuthorizationException, XmlParserParseException, XmlParserException, IOException {
        httpRequest.addHeader("Accept-Encoding", "gzip");
        HttpResponse response = executeHttpRequest(httpRequest);
        return analysisHttpResponseToObject(response, parser, encodeing);
    }

    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null)
            return responseStream;
        Header header = entity.getContentEncoding();
        if (header == null)
            return responseStream;
        String contentEncoding = header.getValue();
        if (contentEncoding == null)
            return responseStream;
        if (contentEncoding.contains("gzip"))
            responseStream = new GZIPInputStream(responseStream);
        return responseStream;
    }

    public BaseObject doHttpRequest(HttpRequestBase httpRequest, Parser<? extends BaseObject> parser, String encodeing)
            throws AuthorizationException, XmlParserParseException, XmlParserException, IOException, Exception {
        return executeHttpRequest(httpRequest, parser, encodeing);
    }
}
