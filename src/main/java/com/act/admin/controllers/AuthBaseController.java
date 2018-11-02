package com.act.admin.controllers;

import act.app.ActionContext;
import act.controller.Controller;
import com.act.admin.annotation.IgnorePermissionCheck;
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

        for (AdminResourcesModel adminResources : loginAdmin.adminRole.adminRoleResources) {
            adminResourcesFunList.add(adminResources.sourceFunction);
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
        logger.debug(actionPath);
        boolean ignorePermissionCheck = false;
        try {
            String classsName = actionPath.substring(0, actionPath.lastIndexOf("."));
            String methodName = actionPath.substring(actionPath.lastIndexOf(".") + 1, actionPath.length());
            Method[] methods = Class.forName(classsName).getMethods();
            //判断指定方法是否有IgnorePermissionCheck注解
            for (Method method :methods) {
                if (methodName.equals(method.getName()) && null != method.getAnnotation(IgnorePermissionCheck.class)) {
                    ignorePermissionCheck = true;
                    break;
                }
            }
            //下面的判断方式简单，但是有坑。有参数的方法判断有问题
            //ignorePermissionCheck = Class.forName(classsName).getMethod(methodName).isAnnotationPresent(IgnorePermissionCheck.class);

            if (ignorePermissionCheck) {
                return true;
            } else {
                actionPath = actionPath.replace("com.act.admin.controllers.", "");
                if (hasPermission(actionPath)) {
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

    public boolean hasPermission(String actionPath) {

        List<AdminResourcesModel> adminRoleResources = loginAdmin.adminRole.adminRoleResources;

        for (AdminResourcesModel adminResources : adminRoleResources) {
            if (actionPath.equals(adminResources.sourceFunction) || actionPath.equals(adminResources.sourceFunction + "Handler")) {
                return true;
            }
        }

        return false;
    }

}
