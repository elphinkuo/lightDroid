package com.elphin.framework.app;

/**
 * App Lifecycle Callback
 *
 * @author elphinkuo
 * @version 1.0
 * @date 13-7-2 11:45am
 */
public interface AppLifecycleCallbacks {
    /**
     * 后台
     */
    abstract void onBackground();

    /**
     * 前台
     */
    abstract void onForeground();

}
