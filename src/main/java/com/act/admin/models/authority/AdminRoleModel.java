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
    public String roleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "adminRoles")
    public List<AdminResourcesModel> adminRoleResources;

    @Column(name = "is_lock")
    public boolean isLock ; //是否锁定不允许修改

}
