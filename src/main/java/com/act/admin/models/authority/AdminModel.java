package com.act.admin.models.authority;

import com.act.admin.models.BaseModel;
import io.ebean.Finder;
import io.ebean.annotation.Index;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-26
 * Time: 2:47 AM
 */

@Entity
@Index(columnNames = {"user_name"})
@Table(name = "admin")
public class AdminModel extends BaseModel {

    public static final Finder<Long, AdminModel> find = new Finder<Long, AdminModel>(AdminModel.class);

    @Column(name = "user_name", length = 100, unique = true, nullable = false)
    private String userName;

    @Column(name = "password", length = 255, unique = false, nullable = false)
    private String password;

    @Column(name = "nick_name", length = 100, unique = false, nullable = true)
    private String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private AdminRoleModel adminRole;

    @Column(name = "enabled")
    private boolean enabled ;

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

    public AdminRoleModel getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(AdminRoleModel adminRole) {
        this.adminRole = adminRole;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
