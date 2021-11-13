package com.elphin.framework.app.fpstack;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import java.lang.reflect.Field;
import java.util.Observable;


/**
 * Implementation of base class BasePage
 * <p>
 *    Pay attemtion to the LifeCycle of View and Fragment when using BasePage
 * </p>
 *
 * @version 1.0
 * @author elphinkuo
 * @date 13-5-26 3:47 pm
 */
public class BasePage extends Fragment implements Page {

    private static final String TAG = BasePage.class.getSimpleName();
    private static final boolean DEBUG = false;

    private static final String STATE_BACK_KEY = "BasePage.is_back";
    private static final String STATE_BACK_ARGS = "BasePage.back_args";
    private static final String STATE_PAGE_TAG = "BasePage.page_tag";

    /**
     * 页面回退标志
     */
    protected boolean mIsBack = false;
    protected Bundle mBackArgs = null;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(isNavigateBack()) {
            onBackFromOtherPage(mBackArgs);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onConfigurationChanged " + newConfig.orientation);

        View newContent = buildOrientationContentView(newConfig);

        ViewGroup rootView = (ViewGroup) getView();
        if(newContent != null) {
            // Remove all the existing views from the root view.
            rootView.removeAllViews();
            rootView.addView(newContent);
            updateOrientationUI(newConfig);
        }
    }

    private String pageTag = PageFactory.DEFAULT_PAGE_TAG;

    private Task mTask;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    private void saveState(Bundle outState) {
        outState.putBoolean(STATE_BACK_KEY,mIsBack);
        outState.putString(STATE_PAGE_TAG,pageTag);
        if(mBackArgs!=null)
            outState.putBundle(STATE_BACK_ARGS,mBackArgs);
    }

    private void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            return;
        mIsBack = savedInstanceState.getBoolean(STATE_BACK_KEY);
        pageTag = savedInstanceState.getString(STATE_PAGE_TAG);
        mBackArgs = savedInstanceState.getBundle(STATE_BACK_ARGS);
        mTask = (BaseTask)getActivity();
    }

    protected void setTask(Task task) {
        this.mTask = task;
    }

    @Override
    public void setPageTag(String pageTag) {
        this.pageTag = pageTag;

    }

    @Override
    final public String getPageTag() {
        return this.pageTag;
    }

    @Override
    public Task getTask() {
         return mTask;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public Bundle getPageArguments() {
        return getArguments();
    }

    /**
     *
     * @param args
     */
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPageArguments(Bundle args) {
        setArguments(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackwardArguments(Bundle args) {
        mBackArgs = args;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bundle getBackwardArguments() {
        return mBackArgs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNavigateBack() {
        return mIsBack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackFromOtherPage(Bundle args) {

    }

    /**
     * 触发页面回退操作
     */
    public void goBack() {
        goBack(null);
    }

    /**
     * 触发页面回退操作
     */
    public void goBack(Bundle args) {
        getTask().goBack(args);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onAttach");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**屏幕常亮的控制逻辑*/
        if (GlobalConfig.getInstance().isAllBright()) {
            getActivity().getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getActivity().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onCreate");

        if(savedInstanceState!=null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onCreateView");
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onResume");

        boolean shouldOverrideOrientation = this.shouldOverrideRequestedOrientation();
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (shouldOverrideOrientation) {
                int orientation = getDefaultRequestedOrientation();
                getActivity().setRequestedOrientation(orientation);
            } else {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }

        String pageLogTag = getPageLogTag();
        if(!TextUtils.isEmpty(pageLogTag)) {
            PerformanceMonitor.getInstance().addEndTime(pageLogTag,System.currentTimeMillis());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onStop");
    }

    /**
     * Modified by HanSiwen
     * 增加将mChildFragmentManager置为null，子类在执行super.onDetach后不要再执行getChildFragmentManager
     * 因为使用了反射，所以今后替换support包时请验证此方法
     * */
    @Override
    public void onDetach() {
        super.onDetach();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onDetach");
        mBackArgs = null;
        mIsBack = false;

        try {
			Field childFMField = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFMField.setAccessible(true);
			childFMField.set(this, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(DEBUG)
            android.util.Log.e(TAG,getClass().getSimpleName()+" onDestroyView");
    }

    /**
     * 根据横竖屏状态构建View，横竖屏切换不同布局时需要实现
     * @see {@link BasePage#onConfigurationChanged(android.content.res.Configuration)}
     * @return 所需的竖屏布局
     */
    public View buildOrientationContentView(Configuration newConfig){
        return null;
    }

    /**
     * 更新UI界面，有横竖屏切换布局时需要实现
     * 在横竖屏切换后，更新相关的布局 @see {@link BasePage#onConfigurationChanged(android.content.res.Configuration)}
     */
    public void updateOrientationUI(Configuration newConfig){}

    @Override
    public void update(Observable observable, Object data) {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getCustomAnimations() {
        return new int[] { 0, 0, 0, 0 };
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean shouldOverrideCustomAnimations() {
		return true;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPageLogTag() {
        return null;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int getDefaultRequestedOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean shouldOverrideRequestedOrientation() {
        return false;
    }
}
