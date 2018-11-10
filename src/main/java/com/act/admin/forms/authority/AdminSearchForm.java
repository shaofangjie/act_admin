package com.act.admin.forms.authority;


import com.act.admin.commonutil.validation.EmptyOrPattern;
import com.act.admin.constraints.RegexpConsts;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 10:09 PM
 */

public class AdminSearchForm {

    @EmptyOrPattern(regexp = RegexpConsts.SEARCH, message = "用户名格式不合法")
    private String userName;
    @EmptyOrPattern(regexp = RegexpConsts.SEARCH, message = "昵称格式不合法")
    private String nickName;
    @EmptyOrPattern(regexp = RegexpConsts.NUM, message = "角色ID格式不合法")
    private String roleId;
    @EmptyOrPattern(regexp = RegexpConsts.ORDERCOLUMN, message = "排序字段不合法")
    private String orderColumn;
    @EmptyOrPattern(regexp = RegexpConsts.ORDERDIR, message = "排序方式不合法")
    private String orderDir;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderDir() {
        return orderDir;
    }

    public void setOrderDir(String orderDir) {
        this.orderDir = orderDir;
    }
}
