package com.elphin.framework.util.async;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-27
 * Time: 下午1:48
 */
public interface Callback<T> {

    void onSuccess(T msg);

    void onError(T msg);

}
