package com.elphin.framework.util.acd;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-30
 * Time: 下午3:32
 */
public interface Stateful {
    void onStateCreate();

    void onStateDestroy();
}
