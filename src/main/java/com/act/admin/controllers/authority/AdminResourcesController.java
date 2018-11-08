package com.act.admin.controllers.authority;

import act.handler.ValidateViolationAdvisor;
import com.act.admin.annotation.SpecifiedPermission;
import com.act.admin.constraints.RegexpConsts;
import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.controllers.AuthBaseController;
import com.act.admin.forms.authority.ResourceAddForm;
import com.act.admin.forms.authority.ResourceEditForm;
import com.act.admin.forms.authority.ResourceSearchForm;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.results.authority.AdminResourceResult;
import com.act.admin.services.authority.AdminResourcesService;
import com.act.admin.validateviolation.LayTableVaildateAdvice;
import com.alibaba.fastjson.JSONObject;
import io.ebean.PagedList;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.ResponseStatus;
import org.osgl.mvc.result.BadRequest;
import org.osgl.mvc.result.Result;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-31
 * Time: 2:38 AM
 */

public class AdminResourcesController extends AuthBaseController implements AuthorityConsts {

    private static Logger logger = L.get(AdminResourcesController.class);

    @Inject
    private AdminResourcesService adminResourcesService;

    public Result index() {
        return render("/authority/adminResourcesIndex.html");
    }

    @ResponseStatus(200)
    @ValidateViolationAdvisor(LayTableVaildateAdvice.class)
    public Result list(@Valid ResourceSearchForm resourceSearchForm, @Pattern(regexp = RegexpConsts.PAGE, message = "页码格式不合法") String page, @Pattern(regexp = RegexpConsts.LIMIT, message = "分页条数格式不合法") String limit) {

        PagedList<AdminResourcesModel> adminResourcePageList = adminResourcesService.getAdminResourcePageList(resourceSearchForm, Integer.parseInt(page), Integer.parseInt(limit));

        List<Object> adminResourceResultList = new ArrayList<>();
        for (AdminResourcesModel adminResources : adminResourcePageList.getList()) {
            AdminResourceResult adminResourceResult = new AdminResourceResult();
            adminResourceResult.setId(adminResources.getId());
            adminResourceResult.setSourceType(adminResources.getSourceType());
            adminResourceResult.setSourceName(adminResources.getSourceName());
            adminResourceResult.setSourceFunction(adminResources.getSourceFunction());
            adminResourceResult.setSourceOrder(adminResources.getSourceOrder());
            adminResourceResult.setEnabled(adminResources.isEnabled() ? 1 : 0);
            adminResourceResultList.add(adminResourceResult);
        }

        JSONObject adminResourceJson = buildTableResult(0, "", adminResourcePageList.getTotalCount(), adminResourceResultList);


        return renderJson(adminResourceJson);
    }

    public Result add() {

        List<Map<String, String>> allParentResources = adminResourcesService.getAllParentResources();

        return render("/authority/adminResourceAdd.html", allParentResources);
    }

    @SpecifiedPermission(value = "authority.AdminResourcesController.add")
    public Result addHandler(@Valid ResourceAddForm resourceAddForm) {

        badRequestIfNull(resourceAddForm);

        AdminResourceAddResult adminResourceAddResult = adminResourcesService.adminResourceSave(resourceAddForm);

        switch (adminResourceAddResult) {
            case ADD_SUCCESS:
                return renderJson(buildSuccessResult("添加成功"));
            case PARENT_IS_NULL:
                return renderJson(buildErrorResult("父级资源不存在", null));
            case ADD_FAILED:
                return renderJson(buildErrorResult("添加失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }

    public Result edit(@Valid @Pattern(regexp = RegexpConsts.SOURCEID, message = "资源ID格式不合法") String id) {

        AdminResourcesModel adminResource = adminResourcesService.getAdminResourceById(id);

        notFoundIfNull(adminResource);

        List<Map<String, String>> allParentResources = adminResourcesService.getAllParentResources();

        return render("/authority/adminResourceEdit.html", allParentResources, adminResource);

    }

    @SpecifiedPermission(value = "authority.AdminResourcesController.edit")
    public Result editHandler(@Valid ResourceEditForm resourceEditForm) {

        badRequestIfNull(resourceEditForm);

        AdminResourceEditResult adminResourceEditResult = adminResourcesService.adminResourceUpdate(resourceEditForm);

        switch (adminResourceEditResult) {
            case EDIT_SUCCESS:
                return renderJson(buildSuccessResult("修改成功"));
            case RESOURCE_IS_NULL:
                return renderJson(buildErrorResult("资源ID不存在", null));
            case PARENT_IS_NULL:
                return renderJson(buildErrorResult("父级资源不存在", null));
            case EDIT_FAILED:
                return renderJson(buildErrorResult("修改失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }

    public Result del(@Valid @Pattern(regexp = RegexpConsts.SOURCEID, message = "资源ID格式不合法") String id) {

        AdminResourcesModel adminResource = adminResourcesService.getAdminResourceById(id);

        notFoundIfNull(adminResource);

        AdminResourceDelResult adminResourceDelResult = adminResourcesService.adminResourceDel(adminResource);

        switch (adminResourceDelResult) {
            case DEL_SUCCESS:
                return renderJson(buildSuccessResult("删除成功"));
            case CANT_DEL:
                return renderJson(buildErrorResult("存在下级资源不允许删除", null));
            case DEL_FAILED:
                return renderJson(buildErrorResult("删除失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }

}
