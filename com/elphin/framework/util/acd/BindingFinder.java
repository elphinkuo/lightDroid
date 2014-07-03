package com.elphin.framework.util.acd;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-25
 * Time: 下午11:07
 */
class BindingFinder {
    private static final HashMap<Class, BindingBean[]> bindingTableCache = new HashMap<Class, BindingBean[]>();

    BindingFinder() {
    }

    BindingBean[] finding(Class<?> target) {
        BindingBean[] contents = null;
        synchronized (bindingTableCache) {
            contents = bindingTableCache.get(target);
        }
        if (contents != null) {
            return contents;
        }
        ArrayList<BindingBean> bindingBeans = new ArrayList<BindingBean>();
        Class<?> cls = target;
        boolean found = false;
        do {
            String name = cls.getCanonicalName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            Method[] methods = cls.getDeclaredMethods();

            for (int i = 0, len = methods.length; i < len; ++i) {
                Binding binding = methods[i].getAnnotation(Binding.class);
                if (binding == null) {
                    continue;
                }
                Id[] value = binding.value();
                if (value == null || value.length == 0) {
                    continue;
                }

                Class<?>[] parameterTypes = methods[i].getParameterTypes();
                Class<?>[] targetTypes = ActionParamsMap.ACTION_PARAM_TYPES.get(binding.type());
                if (Arrays.equals(parameterTypes, targetTypes)) {
                    bindingBeans.add(new BindingBean(methods[i], binding));
                    found = true;
                } else {
                    throw new IllegalStateException("参数类型匹配失败 -> \n\t" + Arrays.toString(parameterTypes)
                            + " not match \n\t" + Arrays.toString(targetTypes));
                }
            }

            cls = cls.getSuperclass();
        } while (cls != null);

        if (!found) {
            throw new IllegalStateException("There is no binding method in " + target.getCanonicalName());
        }

        contents = new BindingBean[bindingBeans.size()];
        bindingBeans.toArray(contents);
        synchronized (bindingTableCache) {
            bindingTableCache.put(target, contents);
        }
        return contents;
    }
}
