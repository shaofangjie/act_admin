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
    private AdminResourcesModel sourcePid;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "admin_role_resources", joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<AdminRoleModel> adminRoles;

    @Column(name = "source_type", columnDefinition = "integer default 0")
    private int sourceType; //资源类型 0:目录 1:菜单 2:功能

    @Column(name = "iconfont", length = 100, unique = false, nullable = true)
    private String iconfont; //图标

    @Column(name = "source_name", length = 100, unique = false, nullable = false)
    private String sourceName; //资源名

    @Column(name = "source_url", length = 255, unique = false, nullable = true)
    private String sourceUrl; //资源路径

    @Column(name = "source_function", length = 255, unique = false, nullable = true)
    private String sourceFunction; //资源方法

    @Column(name = "enabled")
    private boolean enabled; //是否启用

    @Column(name = "source_order", columnDefinition = "integer default 0")
    private int sourceOrder; //排序ID

    @Column(name = "is_lock")
    private boolean Lock; //是否锁定不允许修改

    public AdminResourcesModel getSourcePid() {
        return sourcePid;
    }

    public void setSourcePid(AdminResourcesModel sourcePid) {
        this.sourcePid = sourcePid;
    }

    public List<AdminRoleModel> getAdminRoles() {
        return adminRoles;
    }

    public void setAdminRoles(List<AdminRoleModel> adminRoles) {
        this.adminRoles = adminRoles;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceFunction() {
        return sourceFunction;
    }

    public void setSourceFunction(String sourceFunction) {
        this.sourceFunction = sourceFunction;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getSourceOrder() {
        return sourceOrder;
    }

    public void setSourceOrder(int sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    public boolean isLock() {
        return Lock;
    }

    public void setLock(boolean lock) {
        Lock = lock;
    }
}
