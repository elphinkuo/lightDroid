package com.elphin.framework.app.fpstack;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-20
 * Time: 上午11:53
 */
public final class TaskManagerFactory {
    public static TaskManager getTaskManager() {
        return TaskManagerImpl.getInstance();
    }
}
