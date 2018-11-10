package com.act.admin.controllers;

import com.act.admin.annotation.IgnorePermissionCheck;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.services.MainService;
import com.alibaba.fastjson.JSONObject;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.result.Result;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:10 PM
 */

public class MainController extends AuthBaseController {

    private static Logger logger = L.get(MainController.class);

    @Inject
    private MainService mainService;

    @IgnorePermissionCheck()
    public Result home(H.Session session) {

        AdminModel admin = baseService.getAdminById(session.get("adminid"));

        JSONObject menuList = mainService.getMenuJsonByAdminId(admin.getId());

        return render("/main.html", menuList, admin);
    }

    @IgnorePermissionCheck()
    public Result dashBoard() {

        return render("/dashboard.html");
    }

}
