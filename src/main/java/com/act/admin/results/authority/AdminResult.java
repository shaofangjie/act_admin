package com.act.admin.results.authority;

import cn.hutool.core.date.DateUtil;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 11:18 PM
 */
public class AdminResult {

    private Long id;
    private String userName;
    private String nickName;
    private String roleName;
    private int enable;
    private int lock;
    private String whenUpdated;
    private String whenCreated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Timestamp whenUpdated) {
        this.whenUpdated = DateUtil.formatDateTime(whenUpdated);
    }

    public String getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Timestamp whenCreated) {
        this.whenCreated = DateUtil.formatDateTime(whenCreated);
    }
}
