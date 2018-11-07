package com.act.admin.services.authority;

import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.forms.authority.RoleSearchForm;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.services.BaseService;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.Junction;
import io.ebean.PagedList;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.S;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:22 PM
 */
public class AdminRoleService extends BaseService implements AuthorityConsts {

    private static Logger logger = L.get(AdminRoleService.class);

    public PagedList<AdminRoleModel> getAdminRolePageList(final RoleSearchForm roleSearchForm, final int page, final int limit) {

        try {
            Ebean.beginTransaction();

            Junction<AdminRoleModel> adminRoleModelJunction = AdminRoleModel.find.query().where().conjunction();

            if (S.isNotBlank(roleSearchForm.getRoleName())) {
                adminRoleModelJunction.add(Expr.eq("roleName", roleSearchForm.getRoleName()));
            }
            //排序
            if ("asc".equals(roleSearchForm.getOrderDir())) {
                adminRoleModelJunction.order().asc(roleSearchForm.getOrderColumn());
            } else {
                adminRoleModelJunction.order().desc(roleSearchForm.getOrderColumn());
            }

            //分页参数
            adminRoleModelJunction.setFirstRow((page - 1) * limit);
            adminRoleModelJunction.setMaxRows(limit);

            PagedList<AdminRoleModel> pagedList = adminRoleModelJunction.findPagedList();
            pagedList.loadCount();

            Ebean.commitTransaction();

            return pagedList;

        } catch (Exception ex) {
            logger.error("查询权限角色列表出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return null;

    }

}
