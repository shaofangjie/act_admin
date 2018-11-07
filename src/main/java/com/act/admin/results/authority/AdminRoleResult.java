package com.act.admin.results.authority;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 11:18 PM
 */
public class AdminRoleResult {

    private Long id;
    private String roleName;
    private int lock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }
}
