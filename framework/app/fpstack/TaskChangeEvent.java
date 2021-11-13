package com.elphin.framework.app.fpstack;

/**
 * Created with IntelliJ IDEA.
 *
 * @author elphin
 * @version 1.0
 * @date 13-7-6 3:00pm
 */
public class TaskChangeEvent {
    static final int CHANGE_CUR_TASK = 0;
    static final int REMOVE_TASK = 1;
    int type = -1;
    Task curTask;
}
