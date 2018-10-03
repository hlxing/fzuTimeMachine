package com.west2.fzuTimeMachine.exception.error;

/**
 * @description: 时光错误枚举
 * @author: hlx 2018-10-03
 **/
public enum TimeErrorEnum implements ApiError{

    // 回调消息错误
    BACK_INVALID(130,"BACK_INVALID");

    private Integer code;

    private String msg;

    TimeErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
