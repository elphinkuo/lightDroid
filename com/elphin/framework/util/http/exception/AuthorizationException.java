
package com.elphin.framework.util.http.exception;

/**
 * 授权异常,对应http error code 401.x
 * @author fuliqiang
 *
 */
public class AuthorizationException extends XmlParserException {
    private static final long serialVersionUID = 1L;

    public AuthorizationException(String message) {
        super(message);
    }

}
