package com.act.admin.forms;

import com.act.admin.constraints.RegexpConsts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 12:56 AM
 */

public class LoginForm {

    @NotNull(message = "帐号不能为空")
    @Pattern(regexp = RegexpConsts.USERNAME, message = "帐号只能为4-20位的数字字母下划线组合,且不能以下划线开头.")
    private String userName;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = RegexpConsts.MD5PADDWD, message = "密码格式错误")
    private String password;
    @NotNull(message = "验证码不能为空")
    @Pattern(regexp = RegexpConsts.CAPTCHA, message = "验证码格式错误")
    private String captcha;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String passwd) {
        this.password = passwd;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
