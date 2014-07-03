package com.elphin.framework.util.acd;

import android.view.View;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午10:58
 */
class BindingBean {

    private Method mMethod;
    private Binding mBinding;

    BindingBean(Method mMethod, Binding mBinding) {
        this.mMethod = mMethod;
        this.mMethod.setAccessible(true);
        this.mBinding = mBinding;
    }

    Method getmMethod() {
        return mMethod;
    }

    Binding getmBinding() {
        return mBinding;
    }

    void binding(Object owner, View parent) {
        final ActionType type = mBinding.type();
        final Id[] ids = mBinding.value();

        boolean found = false;
        MethodBinding methodBinding = null;
        for (int i = 0, len = ids.length; i < len; ++i) {
            final View v = parent.findViewById(ids[i].value());
            if (v == null) {
                continue;
            }

            switch (type) {
                case ON_CLICK:
                    methodBinding = new OnClickBinding();
                    break;
                case ON_ITEM_CLICK:
                    methodBinding = new OnItemClickBinding();
                    break;
                default:
                    throw new IllegalStateException("Unknowen action type");
            }

            methodBinding.bind(v, owner, mMethod);
            found = true;
        }

        if (!found) {
            throw new IllegalStateException("nothing found!");
        }
    }
}
