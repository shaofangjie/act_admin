package com.act.admin.models.authority;

import com.act.admin.models.BaseModel;
import io.ebean.Finder;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-26
 * Time: 2:35 AM
 */

@Entity
@Table(name = "admin_role")
public class AdminRoleModel extends BaseModel {

    public static final Finder<Long, AdminRoleModel> find = new Finder<Long, AdminRoleModel>(AdminRoleModel.class);

    @Column(name = "role_name", length = 100, unique = true, nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "adminRole", fetch = FetchType.LAZY)
    private List<AdminModel> admin;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "adminRoles")
    private List<AdminResourcesModel> adminRoleResources;

    @Column(name = "is_lock")
    private boolean Lock ; //是否锁定不允许修改

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<AdminModel> getAdmin() {
        return admin;
    }

    public void setAdmin(List<AdminModel> admin) {
        this.admin = admin;
    }

    public List<AdminResourcesModel> getAdminRoleResources() {
        return adminRoleResources;
    }

    public void setAdminRoleResources(List<AdminResourcesModel> adminRoleResources) {
        this.adminRoleResources = adminRoleResources;
    }

    public boolean isLock() {
        return Lock;
    }

    public void setLock(boolean lock) {
        Lock = lock;
    }
}
