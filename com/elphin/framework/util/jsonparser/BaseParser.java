
package com.elphin.framework.util.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 解析逻辑的基础类
 * @author elphin
 * @param <T>
 */
public abstract class BaseParser<T extends BaseObject> implements Parser<T> {
    public abstract T parse(JSONObject json) throws JSONException;

    public static final JSONObject createJSONParser(InputStream inputStream, String encodeing)
            throws JSONException, IOException, Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, encodeing));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        // LogUtils.debug(buffer.toString());
        JSONObject jsonObject = new JSONObject(buffer.toString());
        return jsonObject;
    }

    /*
    * inner builder util 
    * @param json
    * @param builder
    * @return
    * @throws JSONException
    * @author sunpengshuai
    */
    public final static <V extends BaseObject> List<V> parser(String json, Parser<V> builder,boolean islinked) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        
        int n = jsonArray.length();
        List<V> lists ;
        if(islinked){
            lists= new LinkedList<V>();
        }else{
            lists= new ArrayList<V>(n);
        }
        for (int i = 0; i < n; i++) {
            lists.add(builder.parse(jsonArray.getJSONObject(i)));
        }
        return lists;
    }
}
