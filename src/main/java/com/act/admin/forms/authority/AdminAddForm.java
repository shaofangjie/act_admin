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

public class AdminAddForm {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = RegexpConsts.USERNAME, message = "用户名格式不合法")
    private String userName;
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = RegexpConsts.MD5PADDWD, message = "密码格式不合法")
    private String password;
    @NotNull(message = "昵称不能为空")
    @Pattern(regexp = RegexpConsts.NICKNAME, message = "昵称格式不合法")
    private String nickName;
    @Pattern(regexp = RegexpConsts.NUM, message = "状态格式不合法")
    private String enable;
    @NotNull(message = "角色不能为空")
    @Pattern(regexp = RegexpConsts.ID, message = "角色格式不合法")
    private String roleId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
