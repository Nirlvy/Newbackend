package com.Nirlvy.Newbackend.common;

public enum ResultCode {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    INVALID_PARAMS(400, "错误的参数"),
    FORBIDDEN(403, "权限不足"),
    SYSTEM_ERROR(500, "系统错误"),
    USERNAME_OR_PASSWORD_INCORRECT(600, "用户名或密码错误"),
    USERNAME_OR_PASSWORD_IS_BLANK(601, "用户名或密码为空"),
    USERNAME_ALREADY_EXISTS(602, "用户名已存在"),
    TOKEN_NOT_EXISTS(603, "无token,请重新登录"),
    FAKE_TOKEN(604, "错误的token"),
    UNKNOWN_USER(605, "用户不存在，请重新登录"),
    TOKEN_ERROR(606, "token验证失败，请重新登录"),
    FREEZER_IS_BLANK(700, "冰柜名为空"),
    FREEZER_ALREADY_EXISTS(701, "该冰柜已存在"),
    FAULT_PAGENUM_OR_PAGESIZE(800, "错误的页码或者页面大小"),
    TYPE_OR_DEVICE_IS_BLANK(801, "类型或设备编号为空");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
