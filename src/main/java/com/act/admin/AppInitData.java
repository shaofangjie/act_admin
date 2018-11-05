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
                adminResources1.setSourcePid(null);
                adminResources1.setSourceType(0);
                adminResources1.setIconfont("layui-icon-group");
                adminResources1.setSourceName("权限管理");
                adminResources1.setSourceUrl("/authority/AdminResources/index");
                adminResources1.setSourceFunction("authority.AuthorityResourcesController.index");
                adminResources1.setEnabled(true);
                adminResources1.setSourceOrder(1);
                adminResources1.save();

                AdminResourcesModel adminResources2 = new AdminResourcesModel();
                adminResources2.setSourcePid(adminResources1);
                adminResources2.setSourceType(0);
                adminResources2.setIconfont("layui-icon-right");
                adminResources2.setSourceName("权限资源");
                adminResources2.setSourceUrl("/authority/AdminResources/index");
                adminResources2.setSourceFunction("authority.AuthorityResourcesController.index");
                adminResources2.setEnabled(true);
                adminResources2.setSourceOrder(1);
                adminResources2.save();

                AdminResourcesModel adminResources3 = new AdminResourcesModel();
                adminResources3.setSourcePid(adminResources2);
                adminResources3.setSourceType(1);
                adminResources3.setIconfont("layui-icon-right");
                adminResources3.setSourceName("权限资源列表");
                adminResources3.setSourceUrl("/authority/AdminResources/list");
                adminResources3.setSourceFunction("authority.AuthorityResourcesController.list");
                adminResources3.setEnabled(true);
                adminResources3.setSourceOrder(2);
                adminResources3.save();

                AdminResourcesModel adminResources4 = new AdminResourcesModel();
                adminResources4.setSourcePid(adminResources2);
                adminResources4.setSourceType(2);
                adminResources4.setIconfont("layui-icon-right");
                adminResources4.setSourceName("添加权限资源");
                adminResources4.setSourceUrl("/authority/AdminResources/add");
                adminResources4.setSourceFunction("authority.AuthorityResourcesController.add");
                adminResources4.setEnabled(true);
                adminResources4.setSourceOrder(3);
                adminResources4.save();

                AdminResourcesModel adminResources5 = new AdminResourcesModel();
                adminResources5.setSourcePid(adminResources2);
                adminResources5.setSourceType(2);
                adminResources5.setIconfont("layui-icon-right");
                adminResources5.setSourceName("修改权限资源");
                adminResources5.setSourceUrl("/authority/AdminResources/edit");
                adminResources5.setSourceFunction("authority.AuthorityResourcesController.edit");
                adminResources5.setEnabled(true);
                adminResources5.setSourceOrder(4);
                adminResources5.save();

                AdminResourcesModel adminResources6 = new AdminResourcesModel();
                adminResources6.setSourcePid(adminResources2);
                adminResources6.setSourceType(2);
                adminResources6.setIconfont("layui-icon-right");
                adminResources6.setSourceName("删除权限资源");
                adminResources6.setSourceUrl("/authority/AdminResources/del");
                adminResources6.setSourceFunction("authority.AuthorityResourcesController.del");
                adminResources6.setEnabled(true);
                adminResources6.setSourceOrder(5);
                adminResources6.save();

                //初始化角色信息
                AdminRoleModel adminRole = new AdminRoleModel();
                adminRole.setRoleName("超级管理员");
                List<AdminResourcesModel> adminRoleResources = new ArrayList<>();
                adminRoleResources.add(adminResources1);
                adminRoleResources.add(adminResources2);
                adminRoleResources.add(adminResources3);
                adminRoleResources.add(adminResources4);
                adminRoleResources.add(adminResources5);
                adminRoleResources.add(adminResources6);
                adminRole.setAdminRoleResources(adminRoleResources);
                adminRole.setLock(true);
                adminRole.save();

                AdminModel admin = new AdminModel();
                admin.setUserName("admin");
                admin.setNickName("超级管理员");
                admin.setPassword(DigestUtil.sha256Hex(DigestUtil.md5Hex("admin")));
                admin.setAdminRole(adminRole);
                admin.setEnabled(true);
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
