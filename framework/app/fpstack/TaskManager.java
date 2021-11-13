package com.elphin.framework.app.fpstack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.net.URI;

/**
 * Task manager interface
 *
 * @author elphin
 * @version 1.0
 * @date 13-6-14 8:02pm
 */
public interface TaskManager {
    // stack strategy
    //@Deprecated
    //int STACK_STRATEGY_ADD = 0;
    int STACK_STRATEGY_REPLACE = 1;
    /**
     * 页面栈页面切换的action定义 用于栈内page切换，Task切换不需要该参数
     */
    String ACTION_NAVIGATE_PAGE = "com.baidu.map.act.navigate_page";
    String ACTION_NAVIGATE_BACK = "com.baidu.map.act.back";
    String NAVIGATE_PAGE_NAME = "target_page_name";
    String NAVIGATE_PAGE_PARAM = "target_page_param";
    /**
     * 页面tag
     */
    String NAVIGATE_PAGE_TAG = "target_page_tag";
    /**
     * 页面 URI scheme
     */
    String PAGE_SCHEME = "bdmapui";

    /**
     * 注册页面栈
     * @param task
     * @param page
     * @return
     */
    abstract boolean registerPage(Class<?> task, Class<?> page);

    /**
     * 无参数跳转页面
     * @param ctx
     * @param pageClsName
     * @see {@link #navigateTo(android.content.Context, String, android.os.Bundle)}
     */
    abstract void navigateTo(Context ctx, String pageClsName);

    /**
     * 带参数跳转页面
     * @param ctx           Context
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageArgs      跳转参数
     */
    abstract void navigateTo(Context ctx, String pageClsName, Bundle pageArgs);

    /**
     * 指定页面Tag跳转页面
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     */
    abstract void navigateTo(Context ctx, String pageClsName,String pageTagString);

    /**
     * 指定页面Tag跳转页面，附加页面参数
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     * @param pageArgs      页面参数
     */
    abstract void navigateTo(Context ctx, String pageClsName,String pageTagString, Bundle pageArgs);

    /**
     * 指定页面Tag跳转页面，附加页面参数
     * </p>
     * 为跳转的页面添加标签，构造多实例页面
     *
     * @param ctx           Context
     * @param pageCategory  目标页面类型，区分Map还是组件
     * @param pageClsName   目标页面的类名(全限定名)
     * @param pageTagString 目标页面标签
     * @param pageArgs      页面参数
     */
    abstract void navigateTo(Context ctx, PageCategory pageCategory, String pageClsName, String pageTagString, Bundle pageArgs);



    /**
     * Task跳转
     * <p>按默认方式（单实例）跳转Task。</p>
     *
     * @param ctx    Context
     * @param intent Intent
     */
    abstract void navigateToTask(Context ctx, Intent intent);

    /**
     * Task跳转
     * <p>按给定 Intent Flag 进行 Task 跳转。</p>
     *
     * @param ctx    Context
     * @param intent Activity Intent
     * @param flags  Intent Flags
     */
    abstract void navigateToTask(Context ctx, Intent intent, int flags);

    /**
     * 根据URI跳转页面
     * <p/>
     * 扩展方法，暂未实现
     *
     * @param ctx      Context
     * @param uri      URI
     * @param pageArgs Bundle
     */
    abstract void navigateTo(Context ctx, URI uri, Bundle pageArgs);

    /**
     * 页面栈带参回退回退
     * @param pageArgs
     */
    abstract void onGoBack(Bundle pageArgs);

    /**
     * 页面栈无参回退回调
     */
    abstract void onGoBack();

    /**
     * 添加历史记录
     *
     * @param record
     */
    abstract void track(HistoryRecord record);

	/**
     * 获取根页面记录
     */	
    abstract HistoryRecord getRootRecord();

    /**
     * 获取栈顶页面记录
     *
     * @return HistoryRecord
     */
    abstract HistoryRecord getLatestRecord();

    /**
     * 获取当前页面栈记录集合
     *
     * @return
     */
    abstract ReorderStack<HistoryRecord> getHistoryRecords();

    /**
     * 以record作为标记重置页面栈状态
     * <p>切断历史记录，将record之前的记录清掉。</p>
     *
     * @return 成功返回 true,否则返回 false
     */
    abstract boolean resetStackStatus(HistoryRecord record);

    /**
     * 重置当前页面栈
     * </p>
     * 清空当前页面栈，并将 record作为新的根页面
     *
     * @param record
     * @return
     */
    abstract void resetRootRecord(HistoryRecord record);

    /**
     * 恢复页面状态到根页面
     */
    abstract void resetToRootRecord();

    /**
     * 清除record在页面栈中的记录
     *
     * @param record
     * @return 成功返回 true,否则返回 false
     */
    abstract boolean removeStackRecord(HistoryRecord record);

    /**
     * 页面记录弹栈
     *
     * @return 成功返回 true,否则返回 false
     */
    abstract boolean pop();

    /**
     * 调试信息输出
     *
     * @return 页面栈状态调试信息字符串
     */
    abstract String dump();

    /**
     * 页面进栈策略
     *
     * @return
     */
    abstract int getStackStrategy();

    /**
     * 设置根页面
     *
     * @param record
     */
    abstract void setRootRecord(HistoryRecord record);

    /**
     * ClearTop操作
     * </p>
     * 清除栈中record之后进栈的页面
     *
     * @param record
     */
    abstract void clearTop(HistoryRecord record);

    /**
     * 清理栈记录
     */
    abstract void clear();

    /**
     * 销毁TaskManager
     */
    abstract void destroy();

    /**
     * 获取当前的Context
     *
     * @return 当前激活页面的 Activity Context
     */
    abstract Context getContext();

    /**
     * 保存页面栈状态
     * @return
     */
    abstract Parcelable saveState();

    /**
     * 恢复页面栈状态
     * @param state
     */
    abstract void restoreState(Parcelable state);

}
