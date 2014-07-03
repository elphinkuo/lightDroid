package com.elphin.framework.util.async;

/**
 * Created with IntelliJ IDEA.
 * User: guangongbo
 * Date: 13-6-27
 * Time: 上午1:04
 */
public enum ObservableState {
    /**
     * 未开始状态
     */
    INIT,

    /**
     * 准备状态
     */
    BEFORE,

    /**
     * 后台运行中
     */
    RUN,

    /**
     * 后台运行完毕，整理结果
     */
    AFTER,

    /**
     * 后台执行过程中中断
     */
    INTERRUPT,

    /**
     * 结束，显示结果
     */
    END
}
