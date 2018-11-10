package com.act.admin.forms.authority;

import com.act.admin.commonutil.validation.EmptyOrPattern;
import com.act.admin.constraints.RegexpConsts;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-11-05
 * Time: 8:47 PM
 */

public class AdminEditForm {

    @NotNull(message = "管理员ID不能为空")
    @Pattern(regexp = RegexpConsts.ID, message = "管理员ID格式不合法")
    private String adminId;
    @EmptyOrPattern(regexp = RegexpConsts.MD5PADDWD, message = "密码格式不合法")
    private String password;
    @NotNull(message = "昵称不能为空")
    @Pattern(regexp = RegexpConsts.NICKNAME, message = "昵称格式不合法")
    private String nickName;
    @Pattern(regexp = RegexpConsts.NUM, message = "状态格式不合法")
    private String enable;
    @NotNull(message = "角色不能为空")
    @Pattern(regexp = RegexpConsts.ID, message = "角色格式不合法")
    private String roleId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
