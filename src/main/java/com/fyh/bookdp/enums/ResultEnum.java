package com.fyh.bookdp.enums;


import lombok.Getter;

@Getter
public enum ResultEnum {
    STOCK_ERROR(1,"库存不足");

    //错误代码
    private Integer code;
    //错误信息
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
