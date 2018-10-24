package com.act.admin.models.authority;

import com.act.admin.models.BaseModel;
import io.ebean.Finder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "admin_role_resources")
public class AdminRoleResourcesModel extends BaseModel {

    public static final Finder<Long, AdminRoleResourcesModel> find = new Finder<Long, AdminRoleResourcesModel>(AdminRoleResourcesModel.class);

    @Column(name = "source_pid", columnDefinition = "integer default 0")
    public int sourcePid; //父Id

    @Column(name = "source_type", columnDefinition = "integer default 0")
    public int sourceType; //资源类型 0:功能 1:数据

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
