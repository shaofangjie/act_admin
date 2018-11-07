package com.act.admin.controllers;

import act.app.ActionContext;
import act.controller.Controller;
import com.act.admin.annotation.IgnorePermissionCheck;
import com.act.admin.annotation.SpecifiedPermission;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.services.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.Before;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:53 PM
 */
public class AuthBaseController extends BaseController {

    private static Logger logger = L.get(AuthBaseController.class);

    @Inject
    private ActionContext context;
    @Inject
    private BaseService baseService;
    @Inject
    private AdminModel loginAdmin;

    @Before
    public void authCheck(H.Request request, H.Session session) {

        this.authorizationCheck(request, session);

        this.getLoginAdminInfo(session);

        this.checkPermission(context);

        this.renderArgs();

    }

    private void renderArgs() {

        List<String> adminResourcesFunList = new ArrayList<>();

        for (AdminResourcesModel adminResources : loginAdmin.getAdminRole().getAdminRoleResources()) {
            adminResourcesFunList.add(adminResources.getSourceFunction());
        }

        context.renderArg("adminResourcesFunList", adminResourcesFunList);
    }

    private void getLoginAdminInfo(H.Session session) {
        String adminId = session.get("adminid").trim();
        loginAdmin = baseService.getAdminById(Long.parseLong(adminId));
    }

    private void authorizationCheck(H.Request request, H.Session session) {
        if (StringUtils.isBlank(session.get("adminid"))) {
            String requestPath = request.path();
            if ("/".equals(requestPath) || "/index".equals(requestPath)) {
                throw Controller.Util.redirect("LoginController.loginIndex");
            } else {
                throw forbidden();
            }
        }
    }

    private boolean checkPermission(ActionContext context) {
        String actionPath = context.actionPath().trim();
        String checkActionPath = actionPath.replace("com.act.admin.controllers.", "");
        logger.debug(actionPath);
        boolean ignorePermissionCheck = false;
        try {
            String classsName = actionPath.substring(0, actionPath.lastIndexOf("."));
            String methodName = actionPath.substring(actionPath.lastIndexOf(".") + 1, actionPath.length());
            Method[] methods = Class.forName(classsName).getMethods();
            //判断指定方法是否有 IgnorePermissionCheck SpecifiedPermission 注解
            for (Method method :methods) {
                IgnorePermissionCheck ignorePermissionCheckAnnotation = method.getAnnotation(IgnorePermissionCheck.class);
                SpecifiedPermission specifiedPermissionAnnotation = method.getAnnotation(SpecifiedPermission.class);
                if (methodName.equals(method.getName()) && null != ignorePermissionCheckAnnotation) {
                    ignorePermissionCheck = true;
                    break;
                }
                if (methodName.equals(method.getName()) && null != specifiedPermissionAnnotation) {
                    checkActionPath = specifiedPermissionAnnotation.value();
                    break;
                }
            }

            if (ignorePermissionCheck) {
                return true;
            } else {
                if (hasPermission(checkActionPath)) {
                    return true;
                } else {
                    throw forbidden();
                }
            }

        } catch (Exception e) {
            logger.debug("%s权限校验异常%s", actionPath, e);
            throw forbidden();
        }
    }

    private boolean hasPermission(String actionPath) {

        List<AdminResourcesModel> adminRoleResources = loginAdmin.getAdminRole().getAdminRoleResources();

        for (AdminResourcesModel adminResources : adminRoleResources) {
            if (actionPath.equals(adminResources.getSourceFunction())) {
                return true;
            }
        }

        return false;
    }

}
