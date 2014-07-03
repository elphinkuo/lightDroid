package com.elphin.framework.util.acd;

import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午5:51
 */
public enum ActionType {
    ON_CLICK,
    ON_ITEM_CLICK
}

class ActionParamsMap {
    static final Map<ActionType, Class[]> ACTION_PARAM_TYPES = new HashMap<ActionType, Class[]>();

    static {
        ACTION_PARAM_TYPES.put(ActionType.ON_CLICK, new Class[]{View.class});
        ACTION_PARAM_TYPES.put(ActionType.ON_ITEM_CLICK, new Class[]{AdapterView.class, View.class, int.class, long.class});
    }
}
