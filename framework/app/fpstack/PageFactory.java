package com.elphin.framework.app.fpstack;

/**
 * Page Factory interface</p>
 * Page is designed as single instance, we do not support multi instances for the same page.
 * </p>
 *
 * @version 1.0
 * @author elphin
 * @Date: 13-6-8 4:33pm
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
