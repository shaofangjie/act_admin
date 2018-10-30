package com.act.admin.services;

import act.Act;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminResourcesModel;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.ebean.Expr;
import org.osgl.http.H;

import java.util.Comparator;
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
        return AdminModel.find.query()
                .fetchLazy("adminRole.adminRoleResources")
                .fetchLazy("adminRole.adminRoleResources.sourcePid")
                .where(Expr.eq("id", id))
                .findOne();
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
                topLevelMenu.put("order", topLevelResources.sourceOrder);
                topLevelMenu.put("url", topLevelResources.sourceUrl);
                topLevelMenu.put("name", topLevelResources.sourceName);
                topLevelMenu.put("iconfont", topLevelResources.iconfont);
                topLevelMenuJson.add(topLevelMenu);
            }
            JSONArray senondLevelMenuJson = new JSONArray();
            for (AdminResourcesModel secondLevelResources : adminResourcesList) {
                JSONObject secondLevelMenu = new JSONObject();
                if (0 == secondLevelResources.sourceType && null != secondLevelResources.sourcePid && secondLevelResources.sourcePid.getId().equals(topLevelResources.getId())) {
                    secondLevelMenu.put("id", secondLevelResources.getId());
                    secondLevelMenu.put("order", secondLevelResources.sourceOrder);
                    secondLevelMenu.put("url", secondLevelResources.sourceUrl);
                    secondLevelMenu.put("name", secondLevelResources.sourceName);
                    secondLevelMenu.put("iconfont", secondLevelResources.iconfont);
                    senondLevelMenuJson.add(secondLevelMenu);
                }
                JSONArray threeLevelMenuJson = new JSONArray();
                for (AdminResourcesModel threeLevelResources : adminResourcesList) {
                    JSONObject threeLevelMenu = new JSONObject();
                    if (0 == threeLevelResources.sourceType && null != threeLevelResources.sourcePid && threeLevelResources.sourcePid.getId().equals(secondLevelResources.getId())) {
                        threeLevelMenu.put("id", threeLevelResources.getId());
                        threeLevelMenu.put("order", threeLevelResources.sourceOrder);
                        threeLevelMenu.put("url", threeLevelResources.sourceUrl);
                        threeLevelMenu.put("name", threeLevelResources.sourceName);
                        threeLevelMenu.put("iconfont", threeLevelResources.iconfont);
                        threeLevelMenuJson.add(threeLevelMenu);
                    }
                }
                threeLevelMenuJson.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
                secondLevelMenu.put("sub", threeLevelMenuJson);
            }
            senondLevelMenuJson.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
            topLevelMenu.put("sub", senondLevelMenuJson);
        }
        topLevelMenuJson.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
        menuJson.put("menu", topLevelMenuJson);

        return menuJson;
    }

}
