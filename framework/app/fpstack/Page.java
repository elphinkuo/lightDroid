package com.elphin.framework.app.fpstack;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.elphin.framework.app.mvc.View;

/**
 * 页面抽象接口
 * </p>
 *
 * @version 1.0
 * @author  elphin
 * @date 13-5-26 下午1:44
 */
public interface Page extends View {
    /**
     * 设置页面tag
     * @param pageTag 页面tag
     */
    abstract  void setPageTag(String pageTag);

    /**
     * 获取页面Tag
     * @return  页面Tag
     */
    abstract  String getPageTag();

    abstract  Task getTask();

    /**
     * 回退操作处理，该方法由框架调用。在 {@link android.app.Activity#onBackPressed()}中调用
     *
     * 如果需要手动操作页面回退，调用 {@link Task#goBack(android.os.Bundle)} 或 {@link Task#goBack()}
     * @return
     */
    abstract  boolean onBackPressed();

    /**
     * 获取页面参数
     * @return
     */
    abstract  Bundle getPageArguments();

    /**
     * 设置页面参数
     */
    abstract  void setPageArguments(Bundle args);

    /**
     * 设置 传递到要回退到的目标页面的参数
     * @param args
     */
    abstract void setBackwardArguments(Bundle args);

    /**
     * 获取传递到要回退到的目标页面的参数
     * @return
     */
    abstract Bundle getBackwardArguments();

    /**
     * 是否是栈回退时进入的该页面
     * @return
     */
    abstract boolean isNavigateBack();

    /**
     * 其他页面回退到该页面时的回调
     * @param args
     */
    void onBackFromOtherPage(Bundle args);

    /**
     * 获取自定义进出动画.
     * 无动画指定为0.
     * @return
     * int[0] - int[3]对应:
     * <li>[0]进入时, 本Page的进入动画</li>
     * <li>[1]进入时, 被替换的Page的退出动画</li>
     * <li>[2]退出时, 替换Page的进入动画</li>
     * <li>[3]退出时, 本Page的退出动画</li>
     */
	int[] getCustomAnimations();
	
	/**
	 * 指定其它Page back回本Page时, 本Page的进入动画, 是否由{@link #getCustomAnimations()}[2]覆盖.
	 * 如果Page有自己的动画, 不希望其它界面指定动画时, 需要return false, 并重写{@link #getCustomAnimations()}返回动画效果.
	 * @return 默认返回true, 可以覆盖.
	 */
	boolean shouldOverrideCustomAnimations();

    /**
     * 获取页面性能统计Tag
     * @return 该页面性能统计的Tag @see {@link com.elphin.common.statistics.PerformanceMonitorConst.PageTag}
     */
    String getPageLogTag();

    /**
     * 设置该页面下默认的屏幕方向
     * @see {@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT} {@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
     * @return
     */
    int getDefaultRequestedOrientation();

    /**
     * 是否指定该页面下默认的屏幕方向
     * @return 默认返回false，需要固定该页面下屏幕方向时，返回true，并实现{@link #getDefaultRequestedOrientation}
     */
    boolean shouldOverrideRequestedOrientation();
}
