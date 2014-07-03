package com.elphin.framework.util;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: elphin
 * Date: 13-8-8
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public class VMRuntimeUtil {
    //VMRuntime.getRuntime().setMinimumHeapSize
    public static void setMinimumHeapSize(long size) {
        try
        {
            Class VMRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            Method getRuntimeMethod = VMRuntimeClass.getMethod("getRuntime", new Class[0]);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Long.TYPE;
            Method setMinimumHeapSizeMethod = VMRuntimeClass.getMethod("setMinimumHeapSize", arrayOfClass);
            Object runtimeObject = getRuntimeMethod.invoke(null, new Object[0]);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Long.valueOf(size);
            setMinimumHeapSizeMethod.invoke(runtimeObject, arrayOfObject);
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
    }

    public static long getMinimumHeapSize() {
        try
        {
            Class VMRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            Method getRuntimeMethod = VMRuntimeClass.getMethod("getRuntime", new Class[0]);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Long.TYPE;
            Method getMinimumHeapSizeMethod = VMRuntimeClass.getMethod("getMinimumHeapSize", new Class[0]);
            Object runtimeObject = getRuntimeMethod.invoke(null, new Object[0]);
            Long ret = (Long)getMinimumHeapSizeMethod.invoke(runtimeObject, new Object[0]);
            android.util.Log.e("BAIDUMAP_PERFM","getMinimumHeapSizeMethod:"+ret.longValue());
            return ret.longValue();
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
        return 0;
    }

    public static float getTargetHeapUtilization() {
        try
        {
            Class VMRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            Method getRuntimeMethod = VMRuntimeClass.getMethod("getRuntime", new Class[0]);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Long.TYPE;
            Method getTargetHeapUtilization = VMRuntimeClass.getMethod("getTargetHeapUtilization", new Class[0]);
            Object runtimeObject = getRuntimeMethod.invoke(null, new Object[0]);
            Float ret = (Float)getTargetHeapUtilization.invoke(runtimeObject, new Object[0]);
            android.util.Log.e("BAIDUMAP_PERFM","getTargetHeapUtilization:"+ret.floatValue());
            return ret.floatValue();
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
        return 0;
    }

    public static void setTargetHeapUtilization(float value) {
        try
        {
            Class VMRuntimeClass = Class.forName("dalvik.system.VMRuntime");
            Method getRuntimeMethod = VMRuntimeClass.getMethod("getRuntime", new Class[0]);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Float.TYPE;
            Method setTargetHeapUtilizationMethod = VMRuntimeClass.getMethod("setTargetHeapUtilization", arrayOfClass);
            Object runtimeObject = getRuntimeMethod.invoke(null, new Object[0]);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Float.valueOf(value);
            setTargetHeapUtilizationMethod.invoke(runtimeObject, arrayOfObject);
        }
        catch (Throwable localThrowable)
        {
            localThrowable.printStackTrace();
        }
    }
}
