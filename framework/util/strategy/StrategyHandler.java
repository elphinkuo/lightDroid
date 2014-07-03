package com.elphin.framework.util.strategy;

/**
 * Created with IntelliJ IDEA.
 * User: elphin
 * Date: 13-7-3
 * Time: 下午2:54
 */
public interface StrategyHandler<T> {

    boolean handle(T inParam);

}
