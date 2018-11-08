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

    public List<AdminResourcesModel> getAllResourcesList() {

        return AdminResourcesModel.find.query().fetchLazy("sourcePid").findList();

    }

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

        List<AdminResourcesModel> adminResourcesList = admin.getAdminRole().getAdminRoleResources();

        JSONObject menuJson = new JSONObject();

        JSONArray topLevelMenuJson = new JSONArray();
        for (AdminResourcesModel topLevelResources : adminResourcesList) {
            JSONObject topLevelMenu = new JSONObject();
            if (0 == topLevelResources.getSourceType() && topLevelResources.isEnabled() && null == topLevelResources.getSourcePid()) {
                topLevelMenu.put("id", topLevelResources.getId());
                topLevelMenu.put("order", topLevelResources.getSourceOrder());
                topLevelMenu.put("url", topLevelResources.getSourceUrl());
                topLevelMenu.put("name", topLevelResources.getSourceName());
                topLevelMenu.put("iconfont", topLevelResources.getIconfont());
                topLevelMenuJson.add(topLevelMenu);
            }
            JSONArray senondLevelMenuJson = new JSONArray();
            for (AdminResourcesModel secondLevelResources : adminResourcesList) {
                JSONObject secondLevelMenu = new JSONObject();
                if (0 == secondLevelResources.getSourceType() && secondLevelResources.isEnabled() && null != secondLevelResources.getSourcePid() && secondLevelResources.getSourcePid().getId().equals(topLevelResources.getId())) {
                    secondLevelMenu.put("id", secondLevelResources.getId());
                    secondLevelMenu.put("order", secondLevelResources.getSourceOrder());
                    secondLevelMenu.put("url", secondLevelResources.getSourceUrl());
                    secondLevelMenu.put("name", secondLevelResources.getSourceName());
                    secondLevelMenu.put("iconfont", secondLevelResources.getIconfont());
                    senondLevelMenuJson.add(secondLevelMenu);
                }
                JSONArray threeLevelMenuJson = new JSONArray();
                for (AdminResourcesModel threeLevelResources : adminResourcesList) {
                    JSONObject threeLevelMenu = new JSONObject();
                    if (0 == threeLevelResources.getSourceType() && threeLevelResources.isEnabled() && null != threeLevelResources.getSourcePid() && threeLevelResources.getSourcePid().getId().equals(secondLevelResources.getId())) {
                        threeLevelMenu.put("id", threeLevelResources.getId());
                        threeLevelMenu.put("order", threeLevelResources.getSourceOrder());
                        threeLevelMenu.put("url", threeLevelResources.getSourceUrl());
                        threeLevelMenu.put("name", threeLevelResources.getSourceName());
                        threeLevelMenu.put("iconfont", threeLevelResources.getIconfont());
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
