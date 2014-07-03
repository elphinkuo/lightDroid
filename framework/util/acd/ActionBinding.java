package com.elphin.framework.util.acd;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午5:51
 */
public class ActionBinding {
    private static final ExecutorService SERVICE = new ThreadPoolExecutor(0, 1, 120,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private Object mOwner;
    private Binder mBinder;
    private BindingFinder mFinder;
    private View mParent;

    private Runnable mInternalRunnable = new Runnable() {
        @Override
        public void run() {
            final BindingBean[] beans = mFinder.finding(mOwner.getClass());
            for (int i = 0, len = beans.length; i < len; ++i) {
                beans[i].binding(mOwner, mParent);
            }

            // 发消息通知
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    mBinder.onBinded();
                }
            });
        }
    };

    public ActionBinding(Binder mBinder, View parent) {
        this(mBinder, mBinder, parent);
    }

    public ActionBinding(Object mOwner, Binder mBinder, View parent) {
        if (mOwner == null || mBinder == null || parent == null) {
            throw new NullPointerException();
        }

        this.mOwner = mOwner;
        this.mBinder = mBinder;
        mParent = parent;
        mFinder = new BindingFinder();
    }

    public void startBinding() {
        SERVICE.execute(mInternalRunnable);
    }

}