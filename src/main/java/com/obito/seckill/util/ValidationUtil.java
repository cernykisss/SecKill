package com.obito.seckill.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* 手机号码校验类
* */
public class ValidationUtil {

    private static final Pattern mobile_pattern = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");
    public static boolean isMobile(String mobile) {
        if (mobile.isEmpty()) return false;
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
