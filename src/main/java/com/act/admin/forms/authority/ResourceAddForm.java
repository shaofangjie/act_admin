package com.act.admin.forms.authority;

import com.act.admin.commonutil.validation.EmptyOrPattern;
import com.act.admin.constraints.RegexpConsts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-11-02
 * Time: 2:17 AM
 */

public class ResourceAddForm {

    @NotNull(message = "父级资源不能为空")
    @Pattern(regexp = RegexpConsts.NUM, message = "父级资源格式不合法")
    private String resourcePid;
    @NotNull(message = "资源类型不能为空")
    @Pattern(regexp = RegexpConsts.NUM, message = "资源类型格式不合法")
    private String resourceType;
    @Pattern(regexp = RegexpConsts.NUM, message = "状态格式不合法")
    private String enable;
    @NotNull(message = "资源图标不能为空")
    @Pattern(regexp = RegexpConsts.ICONFONT, message = "资源图标不合法")
    private String iconfont;
    @NotNull(message = "资源名格不能为空")
    @Pattern(regexp = RegexpConsts.SOURCENAME, message = "资源名格式不合法")
    private String resourceName;
    @NotNull(message = "资源路由不能为空")
    @Pattern(regexp = RegexpConsts.ROUTER, message = "资源路由格式不合法")
    private String resourceUrl;
    @NotNull(message = "资源方法不能为空")
    @Pattern(regexp = RegexpConsts.FUNNAME, message = "资源方法格式不合法")
    private String resourceFun;
    @NotNull(message = "资源排序不能为空")
    @Pattern(regexp = RegexpConsts.NUM, message = "资源排序格式不合法")
    private String resourceOrder;

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    public String getResourcePid() {
        return resourcePid;
    }

    public void setResourcePid(String resourcePid) {
        this.resourcePid = resourcePid;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceFun() {
        return resourceFun;
    }

    public void setResourceFun(String resourceFun) {
        this.resourceFun = resourceFun;
    }

    public String getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(String resourceOrder) {
        this.resourceOrder = resourceOrder;
    }
}
