package com.act.admin.services;

import act.Act;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminResourcesModel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.ebean.Expr;
import org.osgl.http.H;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 2:57 AM
 */

public class BaseService {

    public boolean captchaVerify(H.Session session, String captcha) {
        if (Act.isDev()) {
            return true;
        }
        return captcha.equalsIgnoreCase(session.cached("captcha"));
    }

    public AdminModel getAdminByUserName(String userName) {
        return AdminModel.find.query().where(Expr.eq("userName", userName)).findOne();
    }

    public AdminModel getAdminById(Long id) {
        return AdminModel.find.query().fetchLazy("adminRole.adminRoleResources").where(Expr.eq("id", id)).findOne();
    }

    public JSONObject getMenuJsonByAdminId(Long adminId) {

        AdminModel admin = getAdminById(adminId);

        List<AdminResourcesModel> adminResourcesList = admin.adminRole.adminRoleResources;

        JSONObject menuJson = new JSONObject();

        JSONArray topLevelMenuJson = new JSONArray();
        for (AdminResourcesModel topLevelResources : adminResourcesList) {
            JSONObject topLevelMenu = new JSONObject();
            if (null == topLevelResources.sourcePid) {
                topLevelMenu.put("id", topLevelResources.getId());
                topLevelMenu.put("order", topLevelResources.getId());
                topLevelMenu.put("url", topLevelResources.sourceUrl);
                topLevelMenu.put("name", topLevelResources.sourceName);
                topLevelMenu.put("iconfont", topLevelResources.iconfont);
                topLevelMenuJson.add(topLevelMenu);
            }
            JSONArray senondLevelMenuJson = new JSONArray();
            for (AdminResourcesModel secondLevelResources : adminResourcesList) {
                JSONObject secondLevelMenu = new JSONObject();
                if (null != secondLevelResources.sourcePid && secondLevelResources.sourcePid.getId().equals(topLevelResources.getId())) {
                    secondLevelMenu.put("id", secondLevelResources.getId());
                    secondLevelMenu.put("order", secondLevelResources.getId());
                    secondLevelMenu.put("url", secondLevelResources.sourceUrl);
                    secondLevelMenu.put("name", secondLevelResources.sourceName);
                    secondLevelMenu.put("iconfont", secondLevelResources.iconfont);
                    senondLevelMenuJson.add(secondLevelMenu);
                }
                JSONArray threeLevelMenuJson = new JSONArray();
                for (AdminResourcesModel threeLevelResources : adminResourcesList) {
                    JSONObject threeLevelMenu = new JSONObject();
                    if (null != threeLevelResources.sourcePid && threeLevelResources.sourcePid.getId().equals(secondLevelResources.getId())) {
                        threeLevelMenu.put("id", secondLevelResources.getId());
                        threeLevelMenu.put("order", secondLevelResources.getId());
                        threeLevelMenu.put("url", secondLevelResources.sourceUrl);
                        threeLevelMenu.put("name", secondLevelResources.sourceName);
                        threeLevelMenu.put("iconfont", secondLevelResources.iconfont);
                        threeLevelMenuJson.add(threeLevelMenu);
                    }
                }
                secondLevelMenu.put("sub", threeLevelMenuJson);
            }
            topLevelMenu.put("sub", senondLevelMenuJson);
        }

        menuJson.put("menu", topLevelMenuJson);

        return menuJson;
    }

}
