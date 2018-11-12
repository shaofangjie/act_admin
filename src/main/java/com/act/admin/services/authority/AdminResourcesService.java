package com.act.admin.services.authority;

import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.forms.authority.ResourceAddForm;
import com.act.admin.forms.authority.ResourceEditForm;
import com.act.admin.forms.authority.ResourceSearchForm;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.services.BaseService;
import io.ebean.Ebean;
import io.ebean.Expr;
import io.ebean.Junction;
import io.ebean.PagedList;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.util.S;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 10:09 PM
 */
public class AdminResourcesService extends BaseService implements AuthorityConsts {

    private static Logger logger = L.get(AdminResourcesService.class);

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
            logger.error("查询后台资源列表出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return null;

    }

    public AdminResourceAddResult adminResourceSave(final ResourceAddForm resourceAddForm) {
        try {
            Ebean.beginTransaction();

            AdminResourcesModel parentResource = null;
            AdminResourcesModel newResource = new AdminResourcesModel();

            if ("0".equals(resourceAddForm.getResourcePid())) {
                newResource.setSourcePid(null);
            } else {
                parentResource = AdminResourcesModel.find.byId(Long.parseLong(resourceAddForm.getResourcePid()));
                if (null == parentResource) {
                    Ebean.commitTransaction();
                    return AdminResourceAddResult.PARENT_IS_NULL;
                }
            }

            newResource.setSourcePid(parentResource);
            newResource.setSourceType(Integer.parseInt(resourceAddForm.getResourceType()));
            newResource.setEnabled(null != resourceAddForm.getEnable() && "1".equals(resourceAddForm.getEnable()));
            newResource.setSourceName(resourceAddForm.getResourceName());
            newResource.setSourceUrl(resourceAddForm.getResourceUrl());
            newResource.setSourceFunction(resourceAddForm.getResourceFun());
            newResource.setSourceOrder(Integer.parseInt(resourceAddForm.getResourceOrder()));
            newResource.setIconfont(resourceAddForm.getIconfont());
            newResource.save();

            AdminModel superAdmin = AdminModel.find.query()
                    .where(Expr.and(Expr.eq("userName", "admin"), Expr.eq("Lock", true)))
                    .findOne();
            if (null != superAdmin) {
                AdminRoleModel superAdminRole = superAdmin.getAdminRole();
                List<AdminResourcesModel> adminResources = superAdminRole.getAdminRoleResources();
                adminResources.add(newResource);
                superAdminRole.setAdminRoleResources(adminResources);
                superAdminRole.update();
            }

            Ebean.commitTransaction();
            return AdminResourceAddResult.ADD_SUCCESS;
        } catch (Exception ex) {
            logger.error("保存后台资源出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminResourceAddResult.ADD_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminResourceEditResult adminResourceUpdate(final ResourceEditForm resourceEditForm) {
        try {
            Ebean.beginTransaction();

            AdminResourcesModel adminResource = AdminResourcesModel.find.byId(Long.parseLong(resourceEditForm.getResourceId()));

            AdminResourcesModel parentResource = null;

            if (null == adminResource) {
                Ebean.commitTransaction();
                return AdminResourceEditResult.RESOURCE_IS_NULL;
            }

            if (resourceEditForm.getResourcePid().equals("0")) {
                adminResource.setSourcePid(null);
            } else {
                parentResource = AdminResourcesModel.find.byId(Long.parseLong(resourceEditForm.getResourcePid()));
                if (null == parentResource) {
                    Ebean.commitTransaction();
                    return AdminResourceEditResult.PARENT_IS_NULL;
                }
            }

            adminResource.setSourcePid(parentResource);
            adminResource.setSourceType(Integer.parseInt(resourceEditForm.getResourceType()));
            adminResource.setEnabled(null != resourceEditForm.getEnable() && "1".equals(resourceEditForm.getEnable()));
            adminResource.setSourceName(resourceEditForm.getResourceName());
            adminResource.setSourceUrl(resourceEditForm.getResourceUrl());
            adminResource.setSourceFunction(resourceEditForm.getResourceFun());
            adminResource.setSourceOrder(Integer.parseInt(resourceEditForm.getResourceOrder()));
            adminResource.setIconfont(resourceEditForm.getIconfont());

            adminResource.update();

            Ebean.commitTransaction();
            return AdminResourceEditResult.EDIT_SUCCESS;
        } catch (Exception ex) {
            logger.error("更新后台资源出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminResourceEditResult.EDIT_FAILED;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminResourceDelResult adminResourceDel(final AdminResourcesModel adminResources) {

        try {
            Ebean.beginTransaction();

            List<AdminResourcesModel> adminResourcesList = AdminResourcesModel.find.query().where(Expr.eq("sourcePid", adminResources)).findList();
            if (adminResourcesList.size() != 0) {
                Ebean.commitTransaction();
                return AdminResourceDelResult.CANT_DEL;
            }

            adminResources.delete();

            Ebean.commitTransaction();
            return AdminResourceDelResult.DEL_SUCCESS;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("删除后台资源出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return AdminResourceDelResult.DEL_FAILED;
        } finally {
            Ebean.endTransaction();
        }
    }

    public List<Map<String, String>> getAllParentResources() {

        try {
            Ebean.beginTransaction();

            List<AdminResourcesModel> allResourcesList = getAllResourcesList();

            List<Map<String, String>> allParentResources = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("id", "0");
            map.put("name", "顶级资源");
            map.put("type", "0");
            allParentResources.add(map);
            for (AdminResourcesModel topResource : allResourcesList) {
                if (null == topResource.getSourcePid()) {
                    Map<String, String> topMap = new HashMap<>();
                    topMap.put("id", topResource.getId().toString());
                    topMap.put("type", String.valueOf(topResource.getSourceType()));
                    topMap.put("name", "┝ " + topResource.getSourceName());
                    allParentResources.add(topMap);
                    for (AdminResourcesModel secondResource : allResourcesList) {
                        if ((0 == secondResource.getSourceType() || 1 == secondResource.getSourceType()) && null != secondResource.getSourcePid() && secondResource.getSourcePid().getId().equals(topResource.getId())) {
                            Map<String, String> secondMap = new HashMap<>();
                            secondMap.put("id", secondResource.getId().toString());
                            secondMap.put("type", String.valueOf(secondResource.getSourceType()));
                            secondMap.put("name", "&nbsp;&nbsp;┝ " + secondResource.getSourceName());
                            allParentResources.add(secondMap);
                            for (AdminResourcesModel threeResource : allResourcesList) {
                                if ((0 == threeResource.getSourceType() || 1 == threeResource.getSourceType() )&& null != threeResource.getSourcePid() && threeResource.getSourcePid().getId().equals(secondResource.getId())) {
                                    Map<String, String> threeMap = new HashMap<>();
                                    threeMap.put("id", threeResource.getId().toString());
                                    threeMap.put("type", String.valueOf(threeResource.getSourceType()));
                                    threeMap.put("name", "&nbsp;&nbsp;&nbsp;&nbsp;┝ " + threeResource.getSourceName());
                                    allParentResources.add(threeMap);
                                }
                            }
                        }
                    }
                }
            }

            Ebean.commitTransaction();

            return allParentResources;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询后台资源列表出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return null;
        } finally {
            Ebean.endTransaction();
        }

    }

    public AdminResourcesModel getAdminResourceById(String id) {

        try {
            Ebean.beginTransaction();

            AdminResourcesModel adminResources = AdminResourcesModel.find.query().fetchLazy("sourcePid").where(Expr.eq("id", Long.parseLong(id))).findOne();

            Ebean.commitTransaction();

            return adminResources;

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查询后台资源出现错误: %s", ex.getMessage());
            Ebean.rollbackTransaction();
            return null;
        } finally {
            Ebean.endTransaction();
        }
    }

}
