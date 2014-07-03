package com.elphin.framework.util.strategy;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: elphin
 * Date: 13-7-3
 * Time: 下午2:54
 */
public class StrategyHandlerList<T> {

    private static final String TAG = StrategyHandlerList.class.getSimpleName();

    private List<StrategyHandler<T>> mStrategyHandlers = new LinkedList<StrategyHandler<T>>();

    public StrategyHandlerList() {
    }

    public StrategyHandlerList addHandler(StrategyHandler<T> handler) {
        if (handler != null) {
            mStrategyHandlers.add(handler);
        }
        return this;
    }

    public boolean handle(T inParam) {
        final List<StrategyHandler<T>> list = mStrategyHandlers;
        boolean handled = false;
        for (StrategyHandler<T> handler : list) {
            Log.d(TAG, "handler : " + handler.getClass().getSimpleName());
            if (handler.handle(inParam)) {
                Log.d(TAG, "break handler : " + handler.getClass().getSimpleName());
                handled = true;
                break;
            }
        }
        return handled;
    }
}