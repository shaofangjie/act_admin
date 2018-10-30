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
    private BaseService baseService;
    @Inject
    private AdminModel loginAdmin;

    @Before
    public void authCheck(H.Request request, H.Session session, ActionContext context) {

        this.authorizationCheck(request, session);

        this.getLoginAdminInfo(session);

        this.checkPermission(context);

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
        boolean ignorePermissionCheck;
        try {
            String classsName = actionPath.substring(0, actionPath.lastIndexOf("."));
            String methodName = actionPath.substring(actionPath.lastIndexOf(".") + 1, actionPath.length());
            ignorePermissionCheck = Class.forName(classsName).getMethod(methodName).isAnnotationPresent(IgnorePermissionCheck.class);
            if (ignorePermissionCheck) {
                return true;
            }
        } catch (Exception e) {
            actionPath = actionPath.replace("com.act.admin.controllers.", "");
            if (hasPermission(actionPath)) {
                return true;
            } else {
                throw Controller.Util.redirect("MainController.home");
            }
        }
        throw Controller.Util.redirect("MainController.home");
    }

    private boolean hasPermission(String actionPath) {

        List<AdminResourcesModel> adminRoleResources = loginAdmin.adminRole.adminRoleResources;
        List<String> resourceFunList = new ArrayList<>();

        for (AdminResourcesModel adminResources : adminRoleResources) {
            resourceFunList.add(adminResources.sourceFunction);
        }

        return resourceFunList.contains(actionPath);
    }

}
