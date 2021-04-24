package com.obito.seckill.vo;

import com.obito.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
//    自定义注解
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
