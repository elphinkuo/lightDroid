package com.elphin.framework.util.async;

import android.os.Handler;
import android.os.Looper;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA. User: elphin Date: 13-6-27 Time: 上午12:42
 */
public abstract class AsyncCommand<T> {
//    static class AsyncObservableThreadFactory implements ThreadFactory {
//
//        static volatile Thread sRunningThread;
//
//        @Override
//        public Thread newThread(Runnable r) {
//            return sRunningThread = new Thread(r, "AsyncCommand");
//        }
//    }

//    private static final AsyncObservableThreadFactory THREAD_FACTORY = new AsyncObservableThreadFactory();
    private static final ExecutorService SERVICE = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30L,
        TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
        new ThreadPoolExecutor.CallerRunsPolicy());
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    // private static final Field FIELD;
    //
    // static {
    // try {
    // FIELD = Observable.class.getDeclaredField("observers");
    // FIELD.setAccessible(true);
    // } catch (NoSuchFieldException e) {
    // throw new RuntimeException(e);
    // }
    // }

    private final Observable mObservable;
    // private List<Observer> mOriObservers;
    private long mTimeout;
    private CountDownLatch mLatch = new CountDownLatch(1);

    private volatile boolean isMonitorRunnableSended;
    private volatile boolean isTimeout;
    private volatile boolean isInterrupt;
    private volatile ObservableState mState;
    private volatile Object mResponseData;
    private final Callback<T> mCallback;
    private volatile T mResult;

    public AsyncCommand(Observable observable, Callback<T> action, long timeout) {
        if (observable == null) {
            throw new NullPointerException();
        }
        mObservable = observable;
        mTimeout = timeout < 0 ? 0 : timeout;
        mState = ObservableState.INIT;
        mCallback = action;

        // mOriObservers = getObservers(observable);
        // observable.deleteObservers();
        observable.addObserver(mInternalObserver);
    }

    /**
     * 获取数据失败可以调用本方法，会触发{@link Callback#onError(Object)}回调方法。
     * 
     * @param msg
     */
    public final void fail(final T msg) {
        interrupt();

        if (Looper.myLooper() != Looper.getMainLooper()) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(msg);
                }
            });
        } else {
            mCallback.onError(msg);
        }
    }

    public final boolean isTimeout() {
        return isTimeout;
    }

    public final boolean isInterrupt() {
        return isInterrupt;
    }

    public final Object getData() {
        return mResponseData;
    }

    public final void interrupt() {
        if (mState.compareTo(ObservableState.AFTER) < 0) {
//            if (mState == ObservableState.RUN) {
//                AsyncObservableThreadFactory.sRunningThread.interrupt();
//            }
            isInterrupt = true;
        }
    }

    /**
     * 状态图：{@link ObservableState#INIT} -> {@link ObservableState#BEFORE} ->
     * {@link ObservableState#RUN} -> {@link ObservableState#AFTER} ->
     * {@link Callback#onSuccess(Object)}
     * 
     * @return
     */
    public final ObservableState getState() {
        return mState;
    }

    /**
     * 启动异步任务
     */
    public final void start() {
        if (mState != ObservableState.INIT) {
            throw new IllegalStateException("AsyncCommand can only start once.");
        }

        if (isInterrupt) {
            SERVICE.submit(mInterruptRunnable);
            return;
        }

        mState = ObservableState.BEFORE;
        HANDLER.post(mBeforeRunnable);
    }

    /**
     * 子类覆盖该方法。用于做准备工作，在 UI 线程执行。
     */
    protected void before() {
    }

    /**
     * 子类覆盖该方法。用于做后台善后工作，在非 UI 线程执行。
     */
    protected T after(Object data) {
        return null;
    }

    /**
     * 子类覆盖该方法。用于处理以外中断后的工作，在非 UI 线程执行。
     */
    protected void interrupt(boolean isTimeout) {
    }

    protected boolean internalUpdate(Observable observable, Object data) {
        return true;
    }

    private Observer mInternalObserver = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            if (internalUpdate(observable, data)) {
                if (isMonitorRunnableSended && !isInterrupt) {
                    HANDLER.removeCallbacks(mMonitorRunnable);
                }

                if (isInterrupt) {
                    return;
                }

                mResponseData = data;
                mLatch.countDown();
            }
        }
    };

    private Runnable mMonitorRunnable = new Runnable() {
        @Override
        public void run() {
            isTimeout = true;
            interrupt();
        }
    };

    private Runnable mBeforeRunnable = new Runnable() {
        @Override
        public void run() {
            if (isInterrupt) {
                SERVICE.submit(mInterruptRunnable);
                return;
            }
            before();
            if (isInterrupt) {
                SERVICE.submit(mInterruptRunnable);
                return;
            }
            mState = ObservableState.RUN;
            SERVICE.submit(mBackgroundRunnable);
            if (mTimeout > 0) {
                HANDLER.postDelayed(mMonitorRunnable, mTimeout);
                isMonitorRunnableSended = true;
            }
        }
    };

    private Runnable mEndRunnable = new Runnable() {
        @Override
        public void run() {
            mCallback.onSuccess(mResult);
        }
    };

    private Runnable mBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                mLatch.await();
                recoverObservers(); // 获取数据成功，恢复监听器
                mState = ObservableState.AFTER;
                mResult = after(mResponseData);
                if (!isInterrupt) {
                    mState = ObservableState.END;
                    HANDLER.post(mEndRunnable);
                } else {
                    mInterruptRunnable.run();
                }
            } catch (InterruptedException e) {
                mState = ObservableState.INTERRUPT;
                interrupt(isTimeout);
                recoverObservers(); // 获取数据失败，恢复监听器
                mState = ObservableState.END;
            }
        }
    };

    private Runnable mInterruptRunnable = new Runnable() {
        @Override
        public void run() {
            mState = ObservableState.INTERRUPT;
            interrupt(isTimeout);
            recoverObservers(); // 获取数据失败，恢复监听器
            mState = ObservableState.END;
        }
    };

    // private List<Observer> getObservers(Observable observable) {
    // try {
    // return new ArrayList<Observer>((List<Observer>) FIELD.get(observable));
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException(e);
    // }
    // }

    /**
     * 该方法放到 {@link #interrupt()} 回调之后执行，给用户在 {@link #interrupt()} 中删除 request
     * 的机会
     */
    private void recoverObservers() {
        mObservable.deleteObserver(mInternalObserver);
        // mObservable.deleteObservers();
        // if (mOriObservers != null) {
        // for (Observer observer : mOriObservers) {
        // mObservable.addObserver(observer);
        // }
        // }
    }
}
