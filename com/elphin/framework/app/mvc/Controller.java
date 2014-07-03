package com.elphin.framework.app.mvc;

/**
 * Controller 接口</p>
 *
 * <p>Controller的设计遵循单一职责原则，避免过多的复杂逻辑。</p>
 *
 * @version 1.0
 * @author elphin
 * @date 13-6-9 上午9:59
 */
public interface Controller {

    /**
     * 注册View
     * @param view
     */
    public void registerView(View view);

    public void unRegisterView(View view);

    public void notifyChange(Object obj);

}
