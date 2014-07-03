package com.elphin.framework.app.fpstack;

/**
 * 页面工厂接口</p>
 *
 * 页面为单实例，不支持同一个页面的多个实例</p>
 *
 * @version 1.0
 * @author elphin
 * @Date: 13-6-8 下午4:33
 */
interface PageFactory {
    String DEFAULT_PAGE_TAG = "";
    /**
     * 获取页面实例
     *
     * @param pageClsName   页面类名
     * @return    页面实例
     */
    public BasePage getBasePageInstance( String pageClsName);

    public BasePage getBasePageInstance( String pageClsName, String pageTagString);

    public void removePage(BasePage page);

    /**
     * 清空缓存
     */
    public void clearCache();

}
