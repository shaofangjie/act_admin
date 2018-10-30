package com.act.admin.constraints;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 12:56 AM
 */

public final class RegexpConsts {
    public static final String USERNAME = "^[a-zA-Z0-9]\\w{3,19}$"; //4-20位的数字字母下划线组合,且不能以下划线开头.
    public static final String MD5PADDWD = "^[a-zA-Z0-9]{32}$"; //MD5加密后的密码格式
    public static final String CAPTCHA = "^[a-zA-Z0-9]{4}$"; //四位字母数字组合验证码
}
