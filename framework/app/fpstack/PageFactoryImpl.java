package com.elphin.framework.app.fpstack;

import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import com.baidu.map.components.platform.manager.ComponentManager;

/**
 * Factory Implementation class for pages
 * <p>Geneate and maintain the instance of page. Page instance is identified by class name and tag</p>
 *
 * @version 1.0
 * @author elphin
 * @date 13-6-4 11:14pm
 */
class PageFactoryImpl implements PageFactory {

    private static final boolean DEBUG = false;
    private static final String TAG = "PageFactory";

    private static class PageFactoryHolder{
        static final PageFactory sInstance = new PageFactoryImpl();
    }

    public static PageFactory getInstance() {
        return PageFactoryHolder.sInstance;
    }

    private LruCache<String,BasePage> mLruCache;

    private LinkedHashMap<String,SoftReference<BasePage>> mSoftCache;

    private static final int MAX_LRU_SIZE = 32; //the max number of BasePage
    private static final int MAX_SOFT_SIZE = 10;

    private PageFactoryImpl(){
        mSoftCache = new LinkedHashMap<String, SoftReference<BasePage>>(MAX_SOFT_SIZE);
        mLruCache = new LruCache<String, BasePage>(MAX_LRU_SIZE) {

            @Override
            protected void entryRemoved(boolean evicted, String key, BasePage oldValue, BasePage newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null){
                    if(DEBUG){
                        android.util.Log.i(TAG,"LruCache entryRemoved size:"+this.size());
                    }
                    mSoftCache.put(key, new SoftReference<BasePage>(oldValue));
                }
            }

            @Override
            protected BasePage create(String key) {
                return super.create(key);// null
                //return newBasePageInstance(key);
            }
        };
    }

    @Override
    public BasePage getBasePageInstance(String pageClsName) {
        return getBasePageInstance(pageClsName,DEFAULT_PAGE_TAG);
    }


    /**
     * <p>获取页面对象</p>
     *
     * 首先从LRUcache中查找，没有再从softcache中查找
     *
     * @param pageClsName   页面类名
     * @param pageTagString 页面标签，用于区分多实例
     * @return 页面对象
     */
    @Override
    public BasePage getBasePageInstance(String pageClsName,String pageTagString) {

        if (pageClsName == null) {
            return null;
            //throw new NullPointerException("pageClsName == null");
        }

        String pageCacheKey = pageTagString == null ? pageClsName +"@" +DEFAULT_PAGE_TAG : pageClsName+"@"+pageTagString;

        BasePage page;

        synchronized (mLruCache) {
            page = mLruCache.get(pageCacheKey);

            if(page != null){
                mLruCache.remove(pageCacheKey);
                mLruCache.put(pageCacheKey,page);
                return page;
            }
        }

        synchronized (mSoftCache) {
            SoftReference<BasePage> softReference = mSoftCache.get(pageCacheKey);

            if(softReference!=null) {
                page = softReference.get();
                if( page!=null) {
                    mLruCache.put(pageCacheKey,page);
                    mSoftCache.remove(pageCacheKey);
                    return page;
                }else
                    mSoftCache.remove(pageCacheKey);
            }
        }

        page = newBasePageInstance(pageClsName);

        synchronized (mLruCache) {
            mLruCache.put(pageCacheKey,page);
        }

        return page;
    }

    @Override
    public void clearCache() {
        mLruCache.evictAll();
        mSoftCache.clear();
    }

    @Override
    public void removePage(BasePage page) {
        String pageTag = page.getPageTag();
        String pageCacheKey = pageTag == null ?
                page.getClass().getName() +"@" +DEFAULT_PAGE_TAG : page.getClass().getName()+"@"+pageTag;

        synchronized (mLruCache) {
            mLruCache.remove(pageCacheKey);
        }
        synchronized (mSoftCache) {
            mSoftCache.remove(pageCacheKey);
        }
    }

    /**
     * 获取新的页面实例
     * @param pageClsName 页面类名
     * @return
     */
    private BasePage newBasePageInstance(String pageClsName) {

        Class<?> pgCls;
        BasePage page = null;
        try {
//          pgCls = Class.forName(pageClsName);
          pgCls = getPageClassByName(pageClsName);
            if(pgCls != null) {
                page = (BasePage)pgCls.newInstance();
            }
        }catch ( ClassNotFoundException e ) {

        }catch ( IllegalAccessException e ) {

        }catch ( InstantiationException e ) {

        }

        return page;
    }
    
    private Class<?> getPageClassByName(String pageClsName) throws ClassNotFoundException {
        Class<?> pageClass = null;
        if (ComponentManager.getInstance().isComponentPage(pageClsName)) {
           pageClass = ComponentManager.getInstance().getComponentPageClass(pageClsName); 
           if (pageClass != null) {
               return pageClass;
           }
        }
        
        pageClass = Class.forName(pageClsName);
        return pageClass;
    }


}
