package com.elphin.framework.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-7-8
 * Time: 下午8:24
 */
public final class ProcessUtil {

    /**
     * 判断当前进程是否为主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        return getProcessName(context, android.os.Process.myPid()).equals(context.getPackageName());
    }

    /**
     * 根据 pid 获取进程名称
     *
     * @param context
     * @param pid
     * @return
     */
    public static String getProcessName(Context context, int pid) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                activityManager.getRunningAppProcesses();
        try {
            for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
                if (info.pid == pid) {
                    return info.processName;
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 获取当前程序的所有进程 pid
     * @param context
     * @return
     */
    public static int[] getPids(Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningAppProcesses =
                activityManager.getRunningAppProcesses();
        try {
            final LinkedList<Integer> cachePids = new LinkedList<Integer>();
            for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
                if (info.uid == android.os.Process.myUid()) {
                    cachePids.add(info.pid);
                }
            }
            final int[] pids = new int[cachePids.size()];
            for (int i = 0, len = pids.length; i < len; ++i) {
                pids[i] = cachePids.get(i);
            }
            return pids;
        } catch (Exception e) {
        }
        return new int[0];
    }
}
