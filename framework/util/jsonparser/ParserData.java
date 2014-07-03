package com.elphin.framework.util.jsonparser;

import com.elphin.framework.util.http.exception.AuthorizationException;
import com.elphin.framework.util.http.exception.XmlParserException;
import com.elphin.framework.util.http.exception.XmlParserParseException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class ParserData {

    public static BaseObject analysisHttpResponseToObject(InputStream is,
            Parser<? extends BaseObject> parser, String encodeing)
            throws AuthorizationException, XmlParserParseException,
            XmlParserException, IOException {
        try {
            return parser.parse(BaseParser.createJSONParser(is, encodeing));
        } catch (JSONException e) {
            throw new XmlParserException("parser json exception");
        } catch (Exception e) {
            throw new XmlParserException("Exception");
        } finally {
            is.close();
        }
    }

    public static BaseObject analysisHttpResponseToObject(HttpResponse response,
            Parser<? extends BaseObject> parser, String encodeing)
            throws AuthorizationException, XmlParserParseException,
            XmlParserException, IOException {

        int statusCode = response.getStatusLine().getStatusCode();
        switch (statusCode) {
        case 200:
            // InputStream is = response.getEntity().getContent();
            InputStream is = getUngzippedContent(response.getEntity());
            return analysisHttpResponseToObject(is, parser, encodeing);
        case 400:
            throw new XmlParserException(response.getStatusLine().toString(),
                    EntityUtils.toString(response.getEntity()));

        case 401:
            response.getEntity().consumeContent();
            throw new AuthorizationException(response.getStatusLine()
                    .toString());

        case 404:
            response.getEntity().consumeContent();
            throw new XmlParserException(response.getStatusLine().toString());

        case 500:
            response.getEntity().consumeContent();
            throw new XmlParserException("Internal Server Error");

        default:
            response.getEntity().consumeContent();
            throw new XmlParserException("Error connecting : " + statusCode
                    + ". Try again later.");
        }
    }

    public static InputStream getUngzippedContent(HttpEntity entity)
            throws IOException {
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

}
