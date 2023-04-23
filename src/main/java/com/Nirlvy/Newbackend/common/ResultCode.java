package com.Nirlvy.Newbackend.common;

public enum ResultCode {
    UNKNOWN_ERROR(-1, "未知错误"),
    SUCCESS(200, "成功"),
    INVALID_PARAMS(400, "错误的参数"),
    FORBIDDEN(403, "权限不足"),
    SYSTEM_ERROR(500, "系统错误"),
    FAULT_PAGENUM_OR_PAGESIZE(501, "错误的页码或者页面大小"),
    FREEZER_IS_BLANK(600, "冰柜名为空"),
    FREEZER_ALREADY_EXISTS(601, "该冰柜已存在"),
    UNKNOWN_FREEZER(602, "冰柜不存在"),
    FAULT_LOCATION(603, "错误的地址"),
    PRODUCT_ALREADY_EXISTS(651, "该产品已存在"),
    UNKNOWN_PRODUCT_AND_FREEZER(652, "冰柜商品不存在");

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
