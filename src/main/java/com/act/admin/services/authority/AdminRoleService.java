package com.act.admin.services.authority;

import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.forms.authority.RoleAddForm;
import com.act.admin.forms.authority.RoleEditForm;
import com.act.admin.forms.authority.RoleSearchForm;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.services.BaseService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.Junction;
import io.ebean.PagedList;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.S;

import java.util.*;

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

    public JSONObject getResourceTreeJson(AdminRoleModel adminRole) {

        try {
            Ebean.beginTransaction();

            List<AdminResourcesModel> allResourcesList = getAllResourcesList();

            List<AdminResourcesModel> hasResourcesList = new ArrayList<>();

            if (null != adminRole) {
                hasResourcesList = adminRole.getAdminRoleResources();
            }

            JSONArray resourcesTreeArr = new JSONArray();
            for (AdminResourcesModel topResource : allResourcesList) {
                if (null == topResource.getSourcePid()) {
                    JSONObject topMap = new JSONObject();
                    topMap.put("value", topResource.getId().toString());
                    topMap.put("name", topResource.getSourceName());
                    topMap.put("order", topResource.getSourceOrder());
                    topMap.put("checked", hasResourcesList.contains(topResource));
                    JSONArray topList = new JSONArray();
                    for (AdminResourcesModel secondResource : allResourcesList) {
                        if (secondResource.isEnabled() && null != secondResource.getSourcePid() && secondResource.getSourcePid().getId().equals(topResource.getId())) {
                            JSONObject secondMap = new JSONObject();
                            secondMap.put("value", secondResource.getId().toString());
                            secondMap.put("name", secondResource.getSourceName());
                            secondMap.put("order", secondResource.getSourceOrder());
                            secondMap.put("checked", hasResourcesList.contains(secondResource));
                            JSONArray secondList = new JSONArray();
                            for (AdminResourcesModel threeResource : allResourcesList) {
                                if (threeResource.isEnabled() && null != threeResource.getSourcePid() && threeResource.getSourcePid().getId().equals(secondResource.getId())) {
                                    JSONObject threeMap = new JSONObject();
                                    threeMap.put("value", threeResource.getId().toString());
                                    threeMap.put("name", threeResource.getSourceName());
                                    threeMap.put("order", threeResource.getSourceOrder());
                                    threeMap.put("checked", hasResourcesList.contains(threeResource));
                                    JSONArray threeList = new JSONArray();
                                    for (AdminResourcesModel fourResource : allResourcesList) {
                                        if (fourResource.isEnabled() && null != fourResource.getSourcePid() && fourResource.getSourcePid().getId().equals(threeResource.getId())) {
                                            JSONObject fourMap = new JSONObject();
                                            fourMap.put("value", fourResource.getId().toString());
                                            fourMap.put("name", fourResource.getSourceName());
                                            fourMap.put("order", fourResource.getSourceOrder());
                                            fourMap.put("checked", hasResourcesList.contains(fourResource));
                                            threeList.add(fourMap);
                                        }
                                    }
                                    if (threeList.size() > 0) {
                                        threeList.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
                                        threeMap.put("list", threeList);
                                    }
                                    secondList.add(threeMap);
                                }
                            }
                            if (secondList.size() > 0) {
                                secondList.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
                                secondMap.put("list", secondList);
                            }
                            topList.add(secondMap);
                        }
                        if (topList.size() > 0) {
                            topList.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
                            topMap.put("list", topList);
                        }
                    }
                    resourcesTreeArr.add(topMap);
                }
            }
            Ebean.commitTransaction();

            JSONObject resourcesTree = new JSONObject();

            resourcesTree.put("code", 0);
            resourcesTree.put("msg", "ok");
            resourcesTreeArr.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("order")));
            resourcesTree.put("auth", resourcesTreeArr);

            return resourcesTree;

        } catch (Exception ex) {
            logger.error("查询权限资源树出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return null;
    }

    public AdminRoleAddResult adminRoleSave(RoleAddForm roleAddForm) {

        try {
            Ebean.beginTransaction();

            String str[] = roleAddForm.getAuthStr().split(",");
            Object resourceIds[] = new Object[str.length];
            for(int i=0;i<str.length;i++) {
                resourceIds[i] = Integer.parseInt(str[i]);
            }

            if (resourceIds.length == 0) {
                return AdminRoleAddResult.RESOURCE_IS_NULL;
            }

            List<AdminResourcesModel> adminResourceslList = AdminResourcesModel.find.query().where(Expr.in("id", resourceIds)).findList();

            AdminRoleModel adminRole = new AdminRoleModel();
            adminRole.setRoleName(roleAddForm.getRoleName());
            adminRole.setAdminRoleResources(adminResourceslList);
            adminRole.setLock(false);
            adminRole.save();

            Ebean.commitTransaction();
            return AdminRoleAddResult.ADD_SUCCESS;
        } catch (Exception ex) {
            logger.error("添加权限角色出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminRoleAddResult.ADD_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminRoleEditResult adminRoleUpdate(RoleEditForm roleEditForm) {

        try {
            Ebean.beginTransaction();

            AdminRoleModel adminRole = getAdminRoleById(roleEditForm.getRoleId());

            if (null == adminRole) {
                return AdminRoleEditResult.ROLE_NOT_EXIST;
            }

            if (adminRole.isLock()) {
                return AdminRoleEditResult.CANT_EDIT;
            }

            String str[] = roleEditForm.getAuthStr().split(",");
            Object resourceIds[] = new Object[str.length];
            for(int i=0;i<str.length;i++) {
                resourceIds[i] = Integer.parseInt(str[i]);
            }

            if (resourceIds.length == 0) {
                return AdminRoleEditResult.RESOURCE_IS_NULL;
            }

            List<AdminResourcesModel> adminResourceslList = AdminResourcesModel.find.query().where(Expr.in("id", resourceIds)).findList();

            adminRole.setRoleName(roleEditForm.getRoleName());
            adminRole.setAdminRoleResources(adminResourceslList);
            adminRole.setLock(false);
            adminRole.update();

            Ebean.commitTransaction();
            return AdminRoleEditResult.EDIT_SUCCESS;
        } catch (Exception ex) {
            logger.error("添加权限角色出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminRoleEditResult.EDIT_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminRoleModel getAdminRoleById(String roleId) {

        try {
            Ebean.beginTransaction();

            AdminRoleModel adminRole = AdminRoleModel.find.query().fetchLazy("adminRoleResources").where(Expr.eq("id", Long.parseLong(roleId))).findOne();

            Ebean.commitTransaction();

            return adminRole;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询后台角色出现错误: %s" + ex.getMessage());
            Ebean.rollbackTransaction();
            return null;
        } finally {
            Ebean.endTransaction();
        }

    }

}
