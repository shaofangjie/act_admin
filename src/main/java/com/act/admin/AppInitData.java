package com.act.admin;

import cn.hutool.crypto.digest.DigestUtil;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.models.authority.AdminRoleModel;
import io.ebean.Ebean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-29
 * Time: 2:21 AM
 */

public class AppInitData {

    public void initMenu() {

        if (AdminResourcesModel.find.query().findCount() == 0) {

            try {
                Ebean.beginTransaction();

                //初始化权限资源信息
                AdminResourcesModel adminResources1 = new AdminResourcesModel();
                adminResources1.sourcePid = null;
                adminResources1.sourceType = 0;
                adminResources1.iconfont = "&#xe726;";
                adminResources1.sourceName = "权限管理";
                adminResources1.sourceUrl = "/authority/AdminResources/index";
                adminResources1.sourceFunction = "authority.AdminResources.index";
                adminResources1.enabled = true;
                adminResources1.sourceOrder = 1;
                adminResources1.save();

                AdminResourcesModel adminResources2 = new AdminResourcesModel();
                adminResources2.sourcePid = adminResources1;
                adminResources2.sourceType = 0;
                adminResources2.iconfont = "&#xe6a7;";
                adminResources2.sourceName = "权限资源";
                adminResources2.sourceUrl = "/authority/AdminResources/index";
                adminResources2.sourceFunction = "authority.AdminResources.index";
                adminResources2.enabled = true;
                adminResources2.sourceOrder = 1;
                adminResources2.save();

                AdminResourcesModel adminResources3 = new AdminResourcesModel();
                adminResources3.sourcePid = adminResources1;
                adminResources3.sourceType = 1;
                adminResources3.iconfont = "&#xe6a7;";
                adminResources3.sourceName = "权限资源列表";
                adminResources3.sourceUrl = "/authority/AdminResources/list";
                adminResources3.sourceFunction = "authority.AdminResources.list";
                adminResources3.enabled = true;
                adminResources3.sourceOrder = 1;
                adminResources3.save();

                AdminResourcesModel adminResources4 = new AdminResourcesModel();
                adminResources4.sourcePid = adminResources1;
                adminResources4.sourceType = 0;
                adminResources4.iconfont = "&#xe6a7;";
                adminResources4.sourceName = "添加权限资源";
                adminResources4.sourceUrl = "/authority/AdminResources/add";
                adminResources4.sourceFunction = "authority.AdminResources.add";
                adminResources4.enabled = true;
                adminResources4.sourceOrder = 1;
                adminResources4.save();

                AdminResourcesModel adminResources5 = new AdminResourcesModel();
                adminResources5.sourcePid = adminResources1;
                adminResources5.sourceType = 0;
                adminResources5.iconfont = "&#xe6a7;";
                adminResources5.sourceName = "修改权限资源";
                adminResources5.sourceUrl = "/authority/AdminResources/edit";
                adminResources5.sourceFunction = "authority.AdminResources.edit";
                adminResources5.enabled = true;
                adminResources5.sourceOrder = 1;
                adminResources5.save();

                AdminResourcesModel adminResources6 = new AdminResourcesModel();
                adminResources6.sourcePid = adminResources1;
                adminResources6.sourceType = 0;
                adminResources6.iconfont = "&#xe6a7;";
                adminResources6.sourceName = "删除权限资源";
                adminResources6.sourceUrl = "/authority/AdminResources/del";
                adminResources6.sourceFunction = "authority.AdminResources.del";
                adminResources6.enabled = true;
                adminResources6.sourceOrder = 1;
                adminResources6.save();

                //初始化角色信息
                AdminRoleModel adminRole = new AdminRoleModel();
                adminRole.roleName = "超级管理员";
                List<AdminResourcesModel> adminRoleResources = new ArrayList<>();
                adminRoleResources.add(adminResources1);
                adminRoleResources.add(adminResources2);
                adminRoleResources.add(adminResources3);
                adminRoleResources.add(adminResources4);
                adminRoleResources.add(adminResources5);
                adminRoleResources.add(adminResources6);
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
