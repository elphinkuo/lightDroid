package com.elphin.framework.util.cache;

import java.io.File;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author guangongbo
 * @version 1.0 13-3-20
 */
public class FileCache<K> implements Cache<K, File> {

    /**
     * @param maxSize
     *            缓存文件总大小的上限（单位：字节）。
     * @param cacheDir
     *            缓存目录
     * @throws IllegalArgumentException
     *             如果 maxSize 不大于 0；如果 cacheDir 不存在或者不是文件夹或者不可写。
     * @throws NullPointerException
     *             如果 cacheDir 为 null。
     */
    public FileCache(int maxSize, File cacheDir) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0!");
        }
        if (cacheDir == null) {
            throw new NullPointerException("cacheDir can't be null!");
        }
        if (!cacheDir.exists() || !cacheDir.isDirectory()) {
            throw new IllegalArgumentException("cacheDir isn't directory!");
        }
        if (!cacheDir.canWrite()) {
            throw new IllegalArgumentException("cacheDir can't write to!");
        }
        mMaxSize = maxSize;
        mLruCache = new PrivateLruCache<K>(maxSize, cacheDir);
        mCacheDir = cacheDir;
    }

    @Override
    public File put(K key, File value) {
        if (key == null || value == null)
            return null;
        if (!value.isFile())
            return null;
        if (value.length() == 0)
            return null;
        final String prevName = mLruCache.put(key, value.getName());
        if (TextUtils.isEmpty(prevName))
            return null;
        return new File(mCacheDir, prevName);
    }

    @Override
    public File get(K key) {
        final String name = mLruCache.get(key);
        if (TextUtils.isEmpty(name))
            return null;
        return new File(mCacheDir, name);
    }

    @Override
    public int getMaxSize() {
        return mMaxSize;
    }

    @Override
    public File remove(K key) {
        final String name = mLruCache.remove(key);
        if (TextUtils.isEmpty(name))
            return null;
        return new File(mCacheDir, name);
    }

    @Override
    public synchronized boolean contain(K key) {
        return mLruCache.get(key) != null;
    }

    private File mCacheDir;
    private int mMaxSize;
    private LruCache<K, String> mLruCache; // 使用<K, String>而不是<K, File>效率更高

    private static class PrivateLruCache<K> extends LruCache<K, String> {
        private File mCacheDir;

        public PrivateLruCache(int maxSize, File cacheDir) {
            super(maxSize);
            mCacheDir = cacheDir;
        }

        @Override
        protected void entryRemoved(boolean evicted, K key, String oldValue, String newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            // File 对象从 Map 中移除的时候删除文件
            // if (evicted) {
            if (TextUtils.isEmpty(oldValue)) {
                return;
            }
            final File f = new File(mCacheDir, oldValue);
            if (!f.isFile()) {
                return;
            }
            f.delete();
            // }
        }

        @Override
        protected int sizeOf(K key, String value) {
            if (TextUtils.isEmpty(value)) {
                return 0;
            }
            final File f = new File(mCacheDir, value);
            if (!f.isFile()) {
                return 0;
            }
            return (int) f.length();
        }
    }
}
