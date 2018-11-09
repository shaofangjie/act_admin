package com.act.admin.constraints;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:19 PM
 */
public interface MainConsts {

    enum LoginResult {
        CAPTCHA_FAILED,
        USERNAME_PWD_FAILED,
        LOGIN_FAILED,
        USER_DISABLE,
        LOGIN_SUCCESS
    }

}
