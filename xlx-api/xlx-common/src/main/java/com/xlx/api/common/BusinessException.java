package com.xlx.api.common;

/**
 * 业务异常类。
 * <p>用于业务逻辑中的异常抛出，如"产品不存在"、"用户名或密码错误"等。
 * 全局异常处理器会捕获此类异常并返回对应的错误响应。</p>
 */
public class BusinessException extends RuntimeException {

    /** HTTP 状态码，默认 400 */
    private final int code;

    /** 构造业务异常（默认 400 状态码） */
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    /** 构造业务异常（指定状态码） */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
