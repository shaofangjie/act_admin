package com.act.admin.services.authority;

import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.forms.authority.ResourceSearchForm;
import com.act.admin.models.authority.AdminResourcesModel;
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
 * Date: 2018-10-31
 * Time: 10:09 PM
 */
public class AuthorityResourcesService extends BaseService implements AuthorityConsts {

    private static Logger logger = L.get(AuthorityResourcesService.class);

    public PagedList<AdminResourcesModel> getAdminResourcePageList(final ResourceSearchForm resourceSearchForm, final int page, final int limit) {

        try {
            Ebean.beginTransaction();

            Junction<AdminResourcesModel> adminResourcesModelJunction = AdminResourcesModel.find.query().where().conjunction();

            if (S.isNotBlank(resourceSearchForm.getResourceName())) {
                adminResourcesModelJunction.add(Expr.eq("sourceName", resourceSearchForm.getResourceName()));
            }
            //排序
            if ("asc".equals(resourceSearchForm.getOrderDir())) {
                adminResourcesModelJunction.order().asc(resourceSearchForm.getOrderColumn());
            } else {
                adminResourcesModelJunction.order().desc(resourceSearchForm.getOrderColumn());
            }


            //分页参数
            adminResourcesModelJunction.setFirstRow((page - 1) * limit);
            adminResourcesModelJunction.setMaxRows(limit);

            PagedList<AdminResourcesModel> pagedList = adminResourcesModelJunction.findPagedList();
            pagedList.loadCount();

            Ebean.commitTransaction();

            return pagedList;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询后台资源列表出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return null;

    }

}
