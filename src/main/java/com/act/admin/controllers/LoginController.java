package com.act.admin.controllers;

import act.app.ActionContext;
import act.controller.Controller;
import com.act.admin.constraints.MainConsts;
import com.act.admin.forms.LoginForm;
import com.act.admin.services.LoginService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.Before;
import org.osgl.mvc.annotation.ResponseStatus;
import org.osgl.mvc.result.BadRequest;
import org.osgl.mvc.result.Result;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-26
 * Time: 10:43 PM
 */

public class LoginController extends BaseController implements MainConsts {

    private static Logger logger = L.get(LoginController.class);

    @Inject
    private LoginService loginService;

    @Before
    public void checkLogin(H.Session session){
        if(StringUtils.isNotBlank(session.get("adminid"))){
            throw Controller.Util.redirect("MainController.home");
        }
    }

    public Result loginIndex(H.Session session) {
        if (null == session.get("adminid")) {
            session.put("adminid", null);
        }
        return render("/login.html");
    }

    @ResponseStatus(200)
    public Result loginHandler(H.Session session, @Valid LoginForm loginForm, ActionContext context) {

        if (context.hasViolation()) {
            JSONObject error = getViolationErrMsg(context);
            return renderJson(buildErrorResult("", error));
        }

        LoginResult loginResult = loginService.handleLogin(session, context, loginForm);

        switch (loginResult) {
            case LOGIN_SUCCESS:
                return renderJson(buildSuccessResult("登录成功"));
            case CAPTCHA_FAILED:
                return renderJson(buildErrorResult("验证码错误", null));
            case LOGIN_FAILED:
                return renderJson(buildErrorResult("登录失败,请重试.", null));
            case USERNAME_PWD_FAILED:
                return renderJson(buildErrorResult("用户名或密码错误", null));
            default:
                throw new BadRequest();
        }

    }


}
