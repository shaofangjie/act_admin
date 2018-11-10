package com.act.admin.services.authority;

import cn.hutool.crypto.digest.DigestUtil;
import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.forms.authority.AdminAddForm;
import com.act.admin.forms.authority.AdminEditForm;
import com.act.admin.forms.authority.AdminSearchForm;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.services.BaseService;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.Junction;
import io.ebean.PagedList;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.S;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:22 PM
 */
public class AdminService extends BaseService implements AuthorityConsts {

    private static Logger logger = L.get(AdminService.class);

    public PagedList<AdminModel> getAdminPageList(final AdminSearchForm adminSearchForm, final int page, final int limit) {

        try {
            Ebean.beginTransaction();

            Junction<AdminModel> adminModelJunction = AdminModel.find.query().where().conjunction();

            //查询条件拼接
            if (S.isNotBlank(adminSearchForm.getUserName())) {
                adminModelJunction.add(Expr.eq("userName", adminSearchForm.getUserName()));
            }
            if (S.isNotBlank(adminSearchForm.getNickName())) {
                adminModelJunction.add(Expr.like("nickName", "%" + adminSearchForm.getNickName() + "%"));
            }
            if (S.isNotBlank(adminSearchForm.getRoleId()) && !"0".equals(adminSearchForm.getRoleId())) {
                adminModelJunction.add(Expr.eq("adminRole.id", adminSearchForm.getRoleId()));
            }

            //排序
            if ("asc".equals(adminSearchForm.getOrderDir())) {
                adminModelJunction.order().asc(adminSearchForm.getOrderColumn());
            } else {
                adminModelJunction.order().desc(adminSearchForm.getOrderColumn());
            }

            //分页参数
            adminModelJunction.setFirstRow((page - 1) * limit);
            adminModelJunction.setMaxRows(limit);

            PagedList<AdminModel> pagedList = adminModelJunction.findPagedList();
            pagedList.loadCount();

            Ebean.commitTransaction();

            return pagedList;

        } catch (Exception ex) {
            logger.error("查询管理员列表出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return null;

    }

    public AdminAddResult adminSave(final AdminAddForm adminAddForm) {
        try {
            Ebean.beginTransaction();

            AdminRoleModel adminRole = AdminRoleModel.find.query()
                    .where(Expr.eq("id", adminAddForm.getRoleId()))
                    .findOne();

            if (null == adminRole) {
                Ebean.commitTransaction();
                return AdminAddResult.ROLE_NOT_EXIST;
            }

            AdminModel admin = AdminModel.find.query().where(Expr.eq("userName", adminAddForm.getUserName())).findOne();

            if (null != admin) {
                Ebean.commitTransaction();
                return AdminAddResult.USER_EXIST;
            } else {
                AdminModel newAdmin = new AdminModel();
                newAdmin.setUserName(adminAddForm.getUserName());
                newAdmin.setPassword(DigestUtil.sha256Hex(adminAddForm.getPassword().trim()));
                newAdmin.setNickName(adminAddForm.getNickName());
                newAdmin.setAdminRole(adminRole);
                newAdmin.setEnabled(null != adminAddForm.getEnable() && "1".equals(adminAddForm.getEnable()));
                newAdmin.setLock(false);

                newAdmin.save();
            }

            Ebean.commitTransaction();
            return AdminAddResult.ADD_SUCCESS;
        } catch (Exception ex) {
            logger.error("保存管理员出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminAddResult.ADD_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminEditResult adminUpdate(final AdminEditForm adminEditForm) {

        try {
            Ebean.beginTransaction();

            AdminModel admin = getAdminById(adminEditForm.getAdminId());

            AdminRoleModel adminRole = AdminRoleModel.find.query()
                    .where(Expr.eq("id", adminEditForm.getRoleId()))
                    .findOne();

            if (null == admin) {
                Ebean.commitTransaction();
                return AdminEditResult.ADMIN_NOT_EXIST;
            }

            if (null == adminRole) {
                Ebean.commitTransaction();
                return AdminEditResult.ROLE_NOT_EXIST;
            }

            if (admin.isLock()) {
                Ebean.commitTransaction();
                return AdminEditResult.CANT_EDIT;
            }

            admin.setNickName(adminEditForm.getNickName());
            admin.setPassword(DigestUtil.sha256Hex(adminEditForm.getPassword().trim()));
            admin.setEnabled(null != adminEditForm.getEnable() && "1".equals(adminEditForm.getEnable()));
            admin.setAdminRole(adminRole);

            admin.update();

            Ebean.commitTransaction();
            return AdminEditResult.EDIT_SUCCESS;
        } catch (Exception ex) {
            logger.error("修改管理员出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminEditResult.EDIT_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public List<AdminRoleModel> getAllRoleList() {

        try {
            Ebean.beginTransaction();

            List<AdminRoleModel> adminRoleList = AdminRoleModel.find.all();

            Ebean.commitTransaction();

            return adminRoleList;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询所有启用角色出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return null;
        } finally {
            Ebean.endTransaction();
        }

    }

}
