package com.obito.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
* 公共返回对象枚举
* */
@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    //通用
    SUCCESS(200, "success"),
    ERROR(500, "服务端异常"),
    //登录模块
    LOGIN_ERROR(500210, "用户名或密码错误"),
    MOBILE_ERROR(500211, "手机号错误"),
    MOBILE_NOT_FOUND(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAILED(500214, "密码更新失败"),
    BIND_ERROR(500212, "参数校验异常"),
    SESSION_ERROE(500215, "用户不存在"),

    //秒杀模块
    EMPTY_STOCK(500500, "库存不足"),
    REPEAT_ERROR(500501, "该商品限购一件"),

    //订单模块
    ORDER_NOT_FOUND(500300, "订单信息不存在");
//    状态码
    private final Integer code;
//    信息
    private final String message;

}
