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
                adminResources1.setEnabled(true);
                adminResources1.setSourceOrder(1);
                adminResources1.save();

                AdminResourcesModel adminResources12 = new AdminResourcesModel();
                adminResources12.setSourcePid(adminResources1);
                adminResources12.setSourceType(1);
                adminResources12.setIconfont("layui-icon-right");
                adminResources12.setSourceName("管理员管理");
                adminResources12.setSourceUrl("/authority/Admin/index");
                adminResources12.setSourceFunction("authority.AdminController.index");
                adminResources12.setEnabled(true);
                adminResources12.setSourceOrder(1);
                adminResources12.save();
                AdminResourcesModel adminResources13 = new AdminResourcesModel();
                adminResources13.setSourcePid(adminResources12);
                adminResources13.setSourceType(2);
                adminResources13.setSourceName("管理员列表");
                adminResources13.setSourceUrl("/authority/Admin/list");
                adminResources13.setSourceFunction("authority.AdminController.list");
                adminResources13.setEnabled(true);
                adminResources13.setSourceOrder(1);
                adminResources13.save();
                AdminResourcesModel adminResources14 = new AdminResourcesModel();
                adminResources14.setSourcePid(adminResources12);
                adminResources14.setSourceType(2);
                adminResources14.setSourceName("添加管理员");
                adminResources14.setSourceUrl("/authority/Admin/add");
                adminResources14.setSourceFunction("authority.AdminController.add");
                adminResources14.setEnabled(true);
                adminResources14.setSourceOrder(2);
                adminResources14.save();
                AdminResourcesModel adminResources15 = new AdminResourcesModel();
                adminResources15.setSourcePid(adminResources12);
                adminResources15.setSourceType(2);
                adminResources15.setSourceName("修改管理员");
                adminResources15.setSourceUrl("/authority/Admin/edit");
                adminResources15.setSourceFunction("authority.AdminController.edit");
                adminResources15.setEnabled(true);
                adminResources15.setSourceOrder(3);
                adminResources15.save();
                AdminResourcesModel adminResources16 = new AdminResourcesModel();
                adminResources16.setSourcePid(adminResources12);
                adminResources16.setSourceType(2);
                adminResources16.setSourceName("删除管理员");
                adminResources16.setSourceUrl("/authority/Admin/del");
                adminResources16.setSourceFunction("authority.AdminController.del");
                adminResources16.setEnabled(true);
                adminResources16.setSourceOrder(4);
                adminResources16.save();

                AdminResourcesModel adminResources7 = new AdminResourcesModel();
                adminResources7.setSourcePid(adminResources1);
                adminResources7.setSourceType(1);
                adminResources7.setIconfont("layui-icon-right");
                adminResources7.setSourceName("权限角色");
                adminResources7.setSourceUrl("/authority/AdminRole/index");
                adminResources7.setSourceFunction("authority.AdminRoleController.index");
                adminResources7.setEnabled(true);
                adminResources7.setSourceOrder(1);
                adminResources7.save();
                AdminResourcesModel adminResources8 = new AdminResourcesModel();
                adminResources8.setSourcePid(adminResources7);
                adminResources8.setSourceType(2);
                adminResources8.setSourceName("权限角色列表");
                adminResources8.setSourceUrl("/authority/AdminRole/list");
                adminResources8.setSourceFunction("authority.AdminRoleController.list");
                adminResources8.setEnabled(true);
                adminResources8.setSourceOrder(1);
                adminResources8.save();
                AdminResourcesModel adminResources9 = new AdminResourcesModel();
                adminResources9.setSourcePid(adminResources7);
                adminResources9.setSourceType(2);
                adminResources9.setSourceName("添加权限角色");
                adminResources9.setSourceUrl("/authority/AdminRole/add");
                adminResources9.setSourceFunction("authority.AdminRoleController.add");
                adminResources9.setEnabled(true);
                adminResources9.setSourceOrder(1);
                adminResources9.save();
                AdminResourcesModel adminResources10 = new AdminResourcesModel();
                adminResources10.setSourcePid(adminResources7);
                adminResources10.setSourceType(2);
                adminResources10.setSourceName("修改权限角色");
                adminResources10.setSourceUrl("/authority/AdminRole/edit");
                adminResources10.setSourceFunction("authority.AdminRoleController.edit");
                adminResources10.setEnabled(true);
                adminResources10.setSourceOrder(1);
                adminResources10.save();
                AdminResourcesModel adminResources11 = new AdminResourcesModel();
                adminResources11.setSourcePid(adminResources7);
                adminResources11.setSourceType(2);
                adminResources11.setSourceName("删除权限角色");
                adminResources11.setSourceUrl("/authority/AdminRole/del");
                adminResources11.setSourceFunction("authority.AdminRoleController.del");
                adminResources11.setEnabled(true);
                adminResources11.setSourceOrder(1);
                adminResources11.save();

                AdminResourcesModel adminResources2 = new AdminResourcesModel();
                adminResources2.setSourcePid(adminResources1);
                adminResources2.setSourceType(1);
                adminResources2.setIconfont("layui-icon-right");
                adminResources2.setSourceName("权限资源");
                adminResources2.setSourceUrl("/authority/AdminResources/index");
                adminResources2.setSourceFunction("authority.AdminResourcesController.index");
                adminResources2.setEnabled(true);
                adminResources2.setSourceOrder(2);
                adminResources2.save();
                AdminResourcesModel adminResources3 = new AdminResourcesModel();
                adminResources3.setSourcePid(adminResources2);
                adminResources3.setSourceType(2);
                adminResources3.setSourceName("权限资源列表");
                adminResources3.setSourceUrl("/authority/AdminResources/list");
                adminResources3.setSourceFunction("authority.AdminResourcesController.list");
                adminResources3.setEnabled(true);
                adminResources3.setSourceOrder(2);
                adminResources3.save();
                AdminResourcesModel adminResources4 = new AdminResourcesModel();
                adminResources4.setSourcePid(adminResources2);
                adminResources4.setSourceType(2);
                adminResources4.setSourceName("添加权限资源");
                adminResources4.setSourceUrl("/authority/AdminResources/add");
                adminResources4.setSourceFunction("authority.AdminResourcesController.add");
                adminResources4.setEnabled(true);
                adminResources4.setSourceOrder(3);
                adminResources4.save();
                AdminResourcesModel adminResources5 = new AdminResourcesModel();
                adminResources5.setSourcePid(adminResources2);
                adminResources5.setSourceType(2);
                adminResources5.setSourceName("修改权限资源");
                adminResources5.setSourceUrl("/authority/AdminResources/edit");
                adminResources5.setSourceFunction("authority.AdminResourcesController.edit");
                adminResources5.setEnabled(true);
                adminResources5.setSourceOrder(4);
                adminResources5.save();
                AdminResourcesModel adminResources6 = new AdminResourcesModel();
                adminResources6.setSourcePid(adminResources2);
                adminResources6.setSourceType(2);
                adminResources6.setSourceName("删除权限资源");
                adminResources6.setSourceUrl("/authority/AdminResources/del");
                adminResources6.setSourceFunction("authority.AdminResourcesController.del");
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
                adminRoleResources.add(adminResources7);
                adminRoleResources.add(adminResources8);
                adminRoleResources.add(adminResources9);
                adminRoleResources.add(adminResources10);
                adminRoleResources.add(adminResources11);
                adminRoleResources.add(adminResources12);
                adminRoleResources.add(adminResources13);
                adminRoleResources.add(adminResources14);
                adminRoleResources.add(adminResources15);
                adminRoleResources.add(adminResources16);
                adminRole.setAdminRoleResources(adminRoleResources);
                adminRole.setLock(true);
                adminRole.save();

                AdminModel admin = new AdminModel();
                admin.setUserName("admin");
                admin.setNickName("超级管理员");
                admin.setPassword(DigestUtil.sha256Hex(DigestUtil.md5Hex("admin")));
                admin.setAdminRole(adminRole);
                admin.setEnabled(true);
                admin.setLock(true);
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
