package com.elphin.framework.util.jsonparser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author elphin
 * 2010-12-7
 */
public interface Parser<T extends BaseObject> {

    public abstract T parse(JSONObject json) throws JSONException;
}
