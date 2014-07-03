package com.elphin.framework.util.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guangongbo
 * @version 1.0 13-3-20
 */
public interface Cache<K, V> {

    public abstract V put(K key, V value);

    public abstract V get(K key);

    public abstract int getMaxSize();

    public abstract V remove(K key);

    public abstract boolean contain(K key);

}
