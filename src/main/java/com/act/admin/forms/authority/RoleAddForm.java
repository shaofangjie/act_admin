package com.act.admin.forms.authority;

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

public class RoleAddForm {

    @NotNull(message = "角色名不能为空")
    @Pattern(regexp = RegexpConsts.ROLENAME, message = "角色名格式不合法")
    private String roleName;
    @NotNull(message = "权限不能为空")
    @Pattern(regexp = RegexpConsts.ROLERESOURCE, message = "权限格式不合法")
    private String authStr;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

}
