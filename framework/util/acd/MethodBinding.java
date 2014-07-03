package com.elphin.framework.util.acd;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午10:45
 */
interface MethodBinding {
    void bind(View target, Object owner, Method action);
}

class OnClickBinding implements MethodBinding {

    @Override
    public void bind(View target, final Object owner, final Method action) {
        target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    action.invoke(owner, v);
                } catch (Exception e) {
                    Log.e("binding", "", e);
                }
            }
        });
    }

}

class OnItemClickBinding implements MethodBinding {

    @Override
    public void bind(View target, final Object owner, final Method action) {
        final AdapterView adapterView = (AdapterView) target;
        adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    action.invoke(owner, parent, view, position, id);
                } catch (Exception e) {
                    Log.e("binding", "", e);
                }
            }
        });
    }
}

