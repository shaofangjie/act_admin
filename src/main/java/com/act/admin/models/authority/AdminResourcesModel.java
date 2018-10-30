package com.act.admin.models.authority;

import com.act.admin.models.BaseModel;
import io.ebean.Finder;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "admin_resources")
public class AdminResourcesModel extends BaseModel {

    public static final Finder<Long, AdminResourcesModel> find = new Finder<Long, AdminResourcesModel>(AdminResourcesModel.class);

    @ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AdminResourcesModel.class, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "source_pid")
    public AdminResourcesModel sourcePid;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "admin_role_resources", joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    public List<AdminRoleModel> adminRoles;

    @Column(name = "source_type", columnDefinition = "integer default 0")
    public int sourceType; //资源类型 0:功能 1:数据

    @Column(name = "iconfont", length = 100, unique = false, nullable = false)
    public String iconfont; //图标

    @Column(name = "source_name", length = 100, unique = false, nullable = false)
    public String sourceName; //资源名

    @Column(name = "source_url", length = 255, unique = false, nullable = false)
    public String sourceUrl; //资源路径

    @Column(name = "source_function", length = 255, unique = false, nullable = false)
    public String sourceFunction; //资源方法

    @Column(name = "enabled")
    public boolean enabled; //是否启用

    @Column(name = "source_order", columnDefinition = "integer default 0")
    public int sourceOrder; //排序ID

}
