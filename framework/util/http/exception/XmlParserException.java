
package com.elphin.framework.util.http.exception;

/**
 * 附加信息
 * 
 * TODO 尝试删除掉
 * @author elphin
 * 2010-9-18
 */
public class XmlParserException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3789211073927189813L;
    private String mExtra;

    public XmlParserException(String errorMessage) {
        super(errorMessage);
    }

    public XmlParserException(String errorMessage, String extra) {
        super(errorMessage);
        mExtra = extra;
    }
    
    public String getExtra() {
        return mExtra;
    }
}
