package com.elphin.framework.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Acitivty Lifecycle Callback
 *
 * @see {@link android.app.Application.ActivityLifecycleCallbacks} (Api level = 14)
 *
 * @author elphinkuo
 * @version 1.0
 * @date 13-7-2 9:32am
 */
public interface ActivityLifecycleCallbacks {
    abstract void   onActivityCreated(Activity activity, Bundle savedInstanceState);
    abstract void	onActivityDestroyed(Activity activity);
    abstract void	onActivityPaused(Activity activity);
    abstract void	onActivityResumed(Activity activity);
    abstract void	onActivitySaveInstanceState(Activity activity, Bundle outState);
    abstract void	onActivityStarted(Activity activity);
    abstract void	onActivityStopped(Activity activity);
}
