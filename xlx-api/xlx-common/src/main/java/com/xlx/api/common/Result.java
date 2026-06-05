package com.xlx.api.common;

/**
 * 统一 API 响应包装类。
 * <p>所有接口返回值均使用此类封装，保证响应格式一致：{code, message, data}</p>
 *
 * @param <T> 响应数据类型
 */
public class Result<T> {

    /** 响应状态码：200 成功，4xx 客户端错误，5xx 服务端错误 */
    private int code;
    /** 响应消息 */
    private String message;
    /** 响应数据 */
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    /** 成功响应（带数据） */
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "success", data);
    }

    /** 成功响应（无数据） */
    public static <T> Result<T> ok() {
        return new Result<>(200, "success", null);
    }

    /** 成功响应（自定义消息 + 数据） */
    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(200, message, data);
    }

    /** 错误响应（指定状态码和消息） */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /** 错误响应（默认 500 状态码） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
