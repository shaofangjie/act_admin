package com.act.admin;

import act.Act;
import act.controller.Controller;
import act.job.OnAppStart;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.models.authority.AdminResourcesModel;
import io.ebean.Ebean;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.Result;

import java.util.ArrayList;
import java.util.List;

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

        if (AdminResourcesModel.find.query().findCount() == 0) {

            try {
                Ebean.beginTransaction();

                //初始化权限资源信息
                AdminResourcesModel adminRoleResources1 = new AdminResourcesModel();
                adminRoleResources1.sourcePid = null;
                adminRoleResources1.sourceType = 0;
                adminRoleResources1.sourceName = "权限管理";
                adminRoleResources1.sourceUrl = "/admin/authority/AdminRoleSources/index";
                adminRoleResources1.sourceFunction = "authority.AdminsRoleSources.index";
                adminRoleResources1.enabled = true;
                adminRoleResources1.sourceOrder = 1;
                adminRoleResources1.save();

                AdminResourcesModel adminRoleResources2 = new AdminResourcesModel();
                adminRoleResources2.sourcePid = adminRoleResources1;
                adminRoleResources2.sourceType = 0;
                adminRoleResources2.sourceName = "权限资源";
                adminRoleResources2.sourceUrl = "/admin/authority/AdminRoleSources/index";
                adminRoleResources2.sourceFunction = "authority.AdminsRoleSources.index";
                adminRoleResources2.enabled = true;
                adminRoleResources2.sourceOrder = 1;
                adminRoleResources2.save();

                AdminResourcesModel adminRoleResources3 = new AdminResourcesModel();
                adminRoleResources3.sourcePid = adminRoleResources1;
                adminRoleResources3.sourceType = 1;
                adminRoleResources3.sourceName = "权限资源列表";
                adminRoleResources3.sourceUrl = "/admin/authority/AdminRoleSources/list";
                adminRoleResources3.sourceFunction = "authority.AdminsRoleSources.list";
                adminRoleResources3.enabled = true;
                adminRoleResources3.sourceOrder = 1;
                adminRoleResources3.save();

                AdminResourcesModel adminRoleResources4 = new AdminResourcesModel();
                adminRoleResources4.sourcePid = adminRoleResources1;
                adminRoleResources4.sourceType = 0;
                adminRoleResources4.sourceName = "添加权限资源";
                adminRoleResources4.sourceUrl = "/admin/authority/AdminRoleSources/add";
                adminRoleResources4.sourceFunction = "authority.AdminsRoleSources.add";
                adminRoleResources4.enabled = true;
                adminRoleResources4.sourceOrder = 1;
                adminRoleResources4.save();

                AdminResourcesModel adminRoleResources5 = new AdminResourcesModel();
                adminRoleResources5.sourcePid = adminRoleResources1;
                adminRoleResources5.sourceType = 0;
                adminRoleResources5.sourceName = "修改权限资源";
                adminRoleResources5.sourceUrl = "/admin/authority/AdminRoleSources/edit";
                adminRoleResources5.sourceFunction = "authority.AdminsRoleSources.edit";
                adminRoleResources5.enabled = true;
                adminRoleResources5.sourceOrder = 1;
                adminRoleResources5.save();

                AdminResourcesModel adminRoleResources6 = new AdminResourcesModel();
                adminRoleResources6.sourcePid = adminRoleResources1;
                adminRoleResources6.sourceType = 0;
                adminRoleResources6.sourceName = "删除权限资源";
                adminRoleResources6.sourceUrl = "/admin/authority/AdminRoleSources/del";
                adminRoleResources6.sourceFunction = "authority.AdminsRoleSources.del";
                adminRoleResources6.enabled = true;
                adminRoleResources6.sourceOrder = 1;
                adminRoleResources6.save();

                //初始化角色信息
                AdminRoleModel adminRole = new AdminRoleModel();
                adminRole.roleName = "超级管理员";
                List<AdminResourcesModel> adminRoleResources = new ArrayList<>();
                adminRoleResources.add(adminRoleResources1);
                adminRoleResources.add(adminRoleResources2);
                adminRoleResources.add(adminRoleResources3);
                adminRoleResources.add(adminRoleResources4);
                adminRoleResources.add(adminRoleResources5);
                adminRoleResources.add(adminRoleResources6);
                adminRole.adminRoleResources = adminRoleResources;
                adminRole.isLock = true;
                adminRole.save();

                AdminModel admin = new AdminModel();
                admin.userName = "admin";
                admin.nickName = "超级管理员";
                admin.password = DigestUtil.sha256Hex(DigestUtil.md5Hex("admin"));
                admin.adminRole = adminRole;
                admin.enabled = true;
                admin.save();

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
