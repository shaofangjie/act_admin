package com.act.admin.services;

import act.app.ActionContext;
import cn.hutool.crypto.digest.DigestUtil;
import com.act.admin.constraints.MainConsts;
import com.act.admin.controllers.BaseController;
import com.act.admin.forms.LoginForm;
import com.act.admin.models.authority.AdminModel;
import io.ebean.Ebean;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 2:56 AM
 */

public class LoginService extends BaseService implements MainConsts {

    private static Logger logger = L.get(LoginService.class);

    public LoginResult handleLogin(H.Session session, ActionContext context, final LoginForm loginForm) {

        if (!captchaVerify(session, loginForm.getCaptcha())) {
            return LoginResult.CAPTCHA_FAILED;
        }

        try {
            Ebean.beginTransaction();

            AdminModel admin = getAdminByUserName(loginForm.getUserName().trim());

            if (null == admin) {
                Ebean.commitTransaction();
                return LoginResult.USERNAME_PWD_FAILED;
            }

            if (DigestUtil.sha256Hex(loginForm.getPassword().trim()).equals(admin.getPassword())) {
                session.put("adminid",admin.getId());
                context.login(admin.getUserName().toLowerCase());
                Ebean.commitTransaction();
                return LoginResult.LOGIN_SUCCESS;
            } else {
                Ebean.commitTransaction();
                return LoginResult.USERNAME_PWD_FAILED;
            }

        } catch (Exception ex) {
            logger.error("登录查询失败: %s", ex);
            Ebean.rollbackTransaction();
            return LoginResult.LOGIN_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

}
