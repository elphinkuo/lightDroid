package com.elphin.framework.app;

/**
 * App 生命周期回调
 *
 * @author elphinkuo
 * @version 1.0
 * @date 13-7-2 上午11:45
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
