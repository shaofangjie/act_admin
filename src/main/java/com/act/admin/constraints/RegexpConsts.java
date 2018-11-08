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
    public static final String SOURCENAME = "^[\\u4E00-\\u9FA5A-Za-z0-9_]{3,17}$"; //4--18位字母数字汉字下划线组合
    public static final String ROLENAME = SOURCENAME; //4--18位字母数字汉字下划线组合
    public static final String ORDERCOLUMN = "^[a-zA-Z0-9_]{0,19}$"; //1--20位数字字母下划线组合
    public static final String ORDERDIR = ORDERCOLUMN;
    public static final String PAGE = "^[1-9]\\d*$"; //正整数
    public static final String LIMIT = PAGE;
    public static final String ID = PAGE;
    public static final String NUM = "^[0-9]*$"; //数字
    public static final String ROUTER = "^[A-Za-z0-9/._-]+$"; //路由
    public static final String FUNNAME = ROUTER;
    public static final String ICONFONT = "^[A-Za-z0-9-]+$"; //icon
    public static final String ROLERESOURCE = "^(\\d+,)*\\d+$"; //1,2,3,4
}
