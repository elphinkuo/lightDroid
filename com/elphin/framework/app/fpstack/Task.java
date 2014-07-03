package com.elphin.framework.app.fpstack;

import android.content.Intent;
import android.os.Bundle;

/**
 * 页面栈的抽象接口
 * </p>
 * @version 1.0
 * @author elphin
 * @date 13-5-26 午1:49
 */
public interface Task {

    abstract TaskManager getTaskManager();

    /**
     * 设置当前栈tag
     * @param taskTag 栈tag
     */
    abstract  void setTaskTag(String taskTag);

    /**
     * 获取栈Tag
     * @return  页面Tag
     */
    abstract  String getTaskTag();

    //abstract  public boolean registerPage(Page page);

    /**
     * 获取当前栈的页面
     * @return
     */
    abstract  java.util.Stack<Page> getPageStack();


    abstract void navigateTo(String pageClsName,String pageTagString,Bundle pageArgs);

    /**
     * 显示默认内容，无子页面
     */
    abstract  void onShowDefaultContent(Intent intent);

    abstract  boolean goBack(Bundle args);

    abstract  boolean goBack();

    abstract  boolean handleBack(Bundle args);

    /**
     * 销毁该页面，并从栈记录中移除。
     * <p>当需要跳转到其他页面并把自己销毁时使用，谨慎使用，这个接口适合单个Task，无page的移植页面。</p>
     */
    abstract  void finish();

//    void addGlobalConfig(GlobalConfigCallback config);




}
