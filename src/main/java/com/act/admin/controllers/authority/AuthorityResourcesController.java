package com.act.admin.controllers.authority;

import act.app.ActionContext;
import com.act.admin.constraints.RegexpConsts;
import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.controllers.AuthBaseController;
import com.act.admin.forms.authority.ResourceAddForm;
import com.act.admin.forms.authority.ResourceSearchForm;
import com.act.admin.models.authority.AdminResourcesModel;
import com.act.admin.results.authority.AdminResourceResult;
import com.act.admin.services.authority.AuthorityResourcesService;
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

public class AuthorityResourcesController extends AuthBaseController implements AuthorityConsts {

    private static Logger logger = L.get(AuthorityResourcesController.class);

    @Inject
    private AuthorityResourcesService authorityResourcesService;

    public Result index() {
        return render("/authority/adminResourcesIndex.html");
    }

    @ResponseStatus(200)
    public Result list(@Valid ResourceSearchForm resourceSearchForm, @Pattern(regexp = RegexpConsts.PAGE, message = "页码格式不合法") String page, @Pattern(regexp = RegexpConsts.LIMIT, message = "分页条数格式不合法") String limit, ActionContext context) {

        if (context.hasViolation()) {
            String error = getViolationErrStr(context);
            return renderJson(buildTableResult(1, error, 0, null));
        }

        PagedList<AdminResourcesModel> adminResourcePageList = authorityResourcesService.getAdminResourcePageList(resourceSearchForm, Integer.parseInt(page), Integer.parseInt(limit));

        List<Object> adminResourceResultList = new ArrayList<>();
        for (AdminResourcesModel adminResources : adminResourcePageList.getList()) {
            AdminResourceResult adminResourceResult = new AdminResourceResult();
            adminResourceResult.setId(adminResources.getId());
            adminResourceResult.setSourceType(adminResources.sourceType);
            adminResourceResult.setSourceName(adminResources.sourceName);
            adminResourceResult.setSourceFunction(adminResources.sourceFunction);
            adminResourceResult.setSourceOrder(adminResources.sourceOrder);
            adminResourceResult.setEnabled(adminResources.enabled ? 1 : 0);
            adminResourceResultList.add(adminResourceResult);
        }

        JSONObject adminResourceJson = buildTableResult(0, "", adminResourcePageList.getTotalCount(), adminResourceResultList);


        return renderJson(adminResourceJson);
    }

    public Result add() {

        List<Map<String, String>> allParentResources = authorityResourcesService.getAllParentResources();

        return render("/authority/adminResourceAdd.html", allParentResources);
    }

    public Result addHandler(@Valid ResourceAddForm resourceAddForm, ActionContext context) {

        if (context.hasViolation()) {
            JSONObject error = getViolationErrMsg(context);
            return renderJson(buildErrorResult("", error));
        }

        AdminResourceAddResult adminResourceAddResult = authorityResourcesService.adminResourceSave(resourceAddForm);

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

}
