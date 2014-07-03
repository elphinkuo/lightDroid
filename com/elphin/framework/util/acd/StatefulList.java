package com.elphin.framework.util.acd;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-30
 * Time: 下午3:32
 */
public class StatefulList {
    private Collection<Stateful> mStatefuls = new HashSet<Stateful>();

    public StatefulList() {}

    public StatefulList add(Stateful bean) {
        mStatefuls.add(bean);
        return this;
    }

    public StatefulList remove(Stateful bean) {
        mStatefuls.remove(bean);
        return this;
    }

    public void create() {
        for (Stateful bean : mStatefuls) {
            bean.onStateCreate();
        }
    }

    public void destroy() {
        for (Stateful bean : mStatefuls) {
            bean.onStateDestroy();
        }
    }
}
