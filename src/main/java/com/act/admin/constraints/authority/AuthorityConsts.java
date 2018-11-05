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

}
