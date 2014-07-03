package com.elphin.framework.util.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guangongbo
 * @version 1.0 13-3-20
 */
public class BitmapCache<K> implements Cache<K, Bitmap> {
    /**
     * @param maxSize 缓存的最大字节数。
     * @throws IllegalArgumentException 如果 maxSize 不大于 0。
     */
    public BitmapCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0!");
        }
        mMaxSize = maxSize;
        mLruCache = new PrivateLruCache<K>(maxSize);
    }

    @Override
    public Bitmap put(K key, Bitmap value) {
        if (key == null || value == null) return null;
        return mLruCache.put(key, value);
    }

    @Override
    public Bitmap get(K key) {
        return mLruCache.get(key);
    }

    @Override
    public int getMaxSize() {
        return mMaxSize;
    }

    @Override
    public Bitmap remove(K key) {
        return mLruCache.remove(key);
    }

    @Override
    public synchronized boolean contain(K key) {
        return  mLruCache.get(key) != null;
    }

    private int mMaxSize;
    private LruCache<K, Bitmap> mLruCache;

    private static class PrivateLruCache<K> extends LruCache<K, Bitmap> {

        public PrivateLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(K key, Bitmap value) {
            // 每张图片占用的字节数
            return value.getRowBytes() * value.getHeight();
        }
    }
}
