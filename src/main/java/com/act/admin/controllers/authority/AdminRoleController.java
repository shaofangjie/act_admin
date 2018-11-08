package com.act.admin.controllers.authority;

import act.handler.ValidateViolationAdvisor;
import com.act.admin.annotation.SpecifiedPermission;
import com.act.admin.constraints.RegexpConsts;
import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.controllers.AuthBaseController;
import com.act.admin.forms.authority.RoleAddForm;
import com.act.admin.forms.authority.RoleSearchForm;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.results.authority.AdminRoleResult;
import com.act.admin.services.authority.AdminRoleService;
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

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-10-28
 * Time: 5:18 PM
 */

public class AdminRoleController extends AuthBaseController implements AuthorityConsts {

    private static Logger logger = L.get(AdminRoleController.class);

    @Inject
    private AdminRoleService adminRoleService;

    public Result index() {
        return render("/authority/adminRoleIndex.html");
    }

    @ResponseStatus(200)
    @ValidateViolationAdvisor(LayTableVaildateAdvice.class)
    public Result list(@Valid RoleSearchForm roleSearchForm, @Pattern(regexp = RegexpConsts.PAGE, message = "页码格式不合法") String page, @Pattern(regexp = RegexpConsts.LIMIT, message = "分页条数格式不合法") String limit) {

        PagedList<AdminRoleModel> adminRolePageList = adminRoleService.getAdminRolePageList(roleSearchForm, Integer.parseInt(page), Integer.parseInt(limit));

        List<Object> adminRoleResultList = new ArrayList<>();
        for (AdminRoleModel adminRole : adminRolePageList.getList()) {
            AdminRoleResult adminRoleResult = new AdminRoleResult();
            adminRoleResult.setId(adminRole.getId());
            adminRoleResult.setRoleName(adminRole.getRoleName());
            adminRoleResult.setAdminNum(adminRole.getAdmin().size());
            adminRoleResult.setLock(adminRole.isLock() ? 1 : 0);
            adminRoleResultList.add(adminRoleResult);
        }

        JSONObject adminResourceJson = buildTableResult(0, "", adminRolePageList.getTotalCount(), adminRoleResultList);

        return renderJson(adminResourceJson);

    }

    @SpecifiedPermission(value = "authority.AdminRoleController.add")
    public Result resourceTree() {

        JSONObject resourceTreeJson = adminRoleService.getResourceTreeJson();

        return renderJson(resourceTreeJson);
    }

    public Result add() {

        return render("/authority/adminRoleAdd.html");

    }

    @SpecifiedPermission(value = "authority.AdminRoleController.add")
    public Result addHandler(@Valid RoleAddForm roleAddForm) {

        badRequestIfNull(roleAddForm);

        AdminRoleAddResult adminRoleAddResult = adminRoleService.adminRoleSave(roleAddForm);

        switch (adminRoleAddResult) {
            case ADD_SUCCESS:
                return renderJson(buildSuccessResult("添加成功"));
            case RESOURCE_IS_NULL:
                return renderJson(buildErrorResult("角色权限不能为空", null));
            case ADD_FAILED:
                return renderJson(buildErrorResult("添加失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }



}
