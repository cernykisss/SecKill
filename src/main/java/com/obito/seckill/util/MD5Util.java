package com.obito.seckill.util;

import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.*;
@Component
//md5两次加密 从前端到传输层一次 传输层到db一次
public class MD5Util {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }
//    与前端页面的salt一致
    private static final String salt = "1f24f1f24fa";
//    第一次加密
    public static String inputPassToFormPass(String inputPass) {
        String str = salt.charAt(0) + salt.charAt(3) + inputPass + salt.charAt(2);
        return md5(str);
    }
//    第二次加密
    public static String formPassToDBPass(String formPass, String salt) {
        String str = salt.charAt(1) + salt.charAt(7) + formPass + salt.charAt(6) + salt.charAt(4);
        return md5(str);
    }
//    整合
    public static String inputPassToDBPass(String inputPass, String salt) {
        return formPassToDBPass(inputPassToFormPass(inputPass), salt);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("1243", "fufdsfsi24kj"));
    }
}
