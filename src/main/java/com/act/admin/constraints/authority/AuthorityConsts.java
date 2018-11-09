package com.act.admin.constraints.authority;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 3:00 AM
 */
public interface AuthorityConsts {

    enum AdminResourceAddResult {
        ADD_FAILED,
        ADD_SUCCESS,
        PARENT_IS_NULL
    }

    enum AdminResourceEditResult {
        EDIT_FAILED,
        EDIT_SUCCESS,
        PARENT_IS_NULL,
        RESOURCE_IS_NULL
    }

    enum AdminResourceDelResult {
        DEL_FAILED,
        DEL_SUCCESS,
        CANT_DEL
    }

    enum AdminRoleAddResult {
        ADD_FAILED,
        ADD_SUCCESS,
        RESOURCE_IS_NULL
    }

    enum AdminRoleEditResult {
        EDIT_FAILED,
        EDIT_SUCCESS,
        ROLE_NOT_EXIST,
        CANT_EDIT,
        RESOURCE_IS_NULL
    }

    enum AdminRoleDelResult {
        DEL_FAILED,
        DEL_SUCCESS,
        CANT_DEL
    }

    enum AdminAddResult {
        ADD_FAILED,
        ADD_SUCCESS,
        USER_EXIST,
        ROLE_NOT_EXIST
    }

}
