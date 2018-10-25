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
    public String userName;

    @Column(name = "password", length = 255, unique = false, nullable = false)
    public String password;

    @Column(name = "nick_name", length = 100, unique = false, nullable = true)
    public String nickName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public AdminRoleModel adminRole;

    @Column(name = "enabled")
    public boolean enabled ;

}
