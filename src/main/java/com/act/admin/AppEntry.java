package com.act.admin;

import act.Act;
import act.controller.Controller;
import act.job.OnAppStart;
import com.act.admin.models.authority.AdminRoleResourcesModel;
import io.ebean.Ebean;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.Result;
import org.osgl.util.E;

public class AppEntry extends Controller.Util {

    @GetAction
    public Result home() {
        return ok();
    }

    public static void main(String[] args) throws Exception {
        Act.start("ACT_ADMIN");
    }

    @OnAppStart
    public void onStart() {

        initMenu();


    }

    private void initMenu() {

        if (AdminRoleResourcesModel.find.query().findCount() == 0) {

            try {
                Ebean.beginTransaction();

                AdminRoleResourcesModel adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 0;
                adminRoleResources.sourceType = 0;
                adminRoleResources.sourceName = "权限管理";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/index";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.index";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 1;
                adminRoleResources.sourceType = 0;
                adminRoleResources.sourceName = "权限资源";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/index";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.index";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 1;
                adminRoleResources.sourceType = 1;
                adminRoleResources.sourceName = "权限资源列表";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/list";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.list";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 1;
                adminRoleResources.sourceType = 0;
                adminRoleResources.sourceName = "添加权限资源";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/add";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.add";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 1;
                adminRoleResources.sourceType = 0;
                adminRoleResources.sourceName = "修改权限资源";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/edit";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.edit";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                adminRoleResources = new AdminRoleResourcesModel();
                adminRoleResources.sourcePid = 1;
                adminRoleResources.sourceType = 0;
                adminRoleResources.sourceName = "删除权限资源";
                adminRoleResources.sourceUrl = "/admin/authority/AdminRoleSources/del";
                adminRoleResources.sourceFunction = "authority.AdminsRoleSources.del";
                adminRoleResources.enabled = true;
                adminRoleResources.sourceOrder = 1;
                adminRoleResources.save();

                Ebean.commitTransaction();
            } catch (Exception ex) {
                ex.printStackTrace();
                Ebean.rollbackTransaction();
            } finally {
                Ebean.endTransaction();
            }
        }
    }

}
