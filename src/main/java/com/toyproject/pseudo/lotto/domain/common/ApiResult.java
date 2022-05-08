package com.toyproject.pseudo.lotto.domain.common;

public class ApiResult<T> {
    boolean success;
    T result;
    String message;

    public static <T> ApiResult success(T result) {
        return new ApiResult(true, result, null);
    }

    public static ApiResult fail(String message) {
        return new ApiResult(false, null, message);
    }

    ApiResult(boolean success, T result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
