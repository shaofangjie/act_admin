package com.act.admin.controllers.authority;

import act.handler.ValidateViolationAdvisor;
import com.act.admin.annotation.SpecifiedPermission;
import com.act.admin.constraints.RegexpConsts;
import com.act.admin.constraints.authority.AuthorityConsts;
import com.act.admin.controllers.AuthBaseController;
import com.act.admin.forms.authority.AdminAddForm;
import com.act.admin.forms.authority.AdminEditForm;
import com.act.admin.forms.authority.AdminSearchForm;
import com.act.admin.models.authority.AdminModel;
import com.act.admin.models.authority.AdminRoleModel;
import com.act.admin.results.authority.AdminResult;
import com.act.admin.services.authority.AdminService;
import com.act.admin.validateviolation.LayTableVaildateAdvice;
import com.alibaba.fastjson.JSONObject;
import io.ebean.PagedList;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.ResponseContentType;
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

public class AdminController extends AuthBaseController implements AuthorityConsts {

    private static Logger logger = L.get(AdminController.class);

    @Inject
    private AdminService adminService;

    @ResponseContentType(H.MediaType.HTML)
    public Result index() {

        List<AdminRoleModel> adminRoleList = adminService.getAllRoleList();

        return render("/authority/adminIndex.html", adminRoleList);
    }

    @ResponseStatus(200)
    @ValidateViolationAdvisor(LayTableVaildateAdvice.class)
    public Result list(@Valid AdminSearchForm adminSearchForm, @Pattern(regexp = RegexpConsts.PAGE, message = "页码格式不合法") String page, @Pattern(regexp = RegexpConsts.LIMIT, message = "分页条数格式不合法") String limit) {

        PagedList<AdminModel> adminPageList = adminService.getAdminPageList(adminSearchForm, Integer.parseInt(page), Integer.parseInt(limit));

        List<Object> adminResultList = new ArrayList<>();
        for (AdminModel admin : adminPageList.getList()) {
            AdminResult adminResult = new AdminResult();
            adminResult.setId(admin.getId());
            adminResult.setUserName(admin.getUserName());
            adminResult.setNickName(admin.getNickName());
            adminResult.setRoleName(admin.getAdminRole().getRoleName());
            adminResult.setWhenUpdated(admin.getWhenUpdated());
            adminResult.setWhenCreated(admin.getWhenCreated());
            adminResult.setEnable(admin.isEnabled() ? 1 : 0);
            adminResult.setLock(admin.isLock() ? 1 : 0);
            adminResultList.add(adminResult);
        }

        JSONObject adminJson = buildTableResult(0, "", adminPageList.getTotalCount(), adminResultList);

        return renderJson(adminJson);

    }

    @ResponseContentType(H.MediaType.HTML)
    public Result add() {

        List<AdminRoleModel> adminRoleList = adminService.getAllRoleList();

        return render("/authority/adminAdd.html", adminRoleList);
    }

    @SpecifiedPermission(value = "authority.AdminController.add")
    public Result addHandler(@Valid AdminAddForm adminAddForm) {

        badRequestIfNull(adminAddForm);

        AdminAddResult adminAddResult = adminService.adminSave(adminAddForm);

        switch (adminAddResult) {
            case ADD_SUCCESS:
                return renderJson(buildSuccessResult("添加成功"));
            case ROLE_NOT_EXIST:
                return renderJson(buildErrorResult("角色不存在", null));
            case ADD_FAILED:
                return renderJson(buildErrorResult("添加失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }

    @ResponseContentType(H.MediaType.HTML)
    public Result edit(@Valid @Pattern(regexp = RegexpConsts.ID, message = "管理员ID格式不合法") String adminId) {

        List<AdminRoleModel> adminRoleList = adminService.getAllRoleList();

        AdminModel admin = adminService.getAdminById(adminId);

        notFoundIfNull(admin);

        return render("/authority/adminEdit.html", adminRoleList, admin);
    }

    @SpecifiedPermission(value = "authority.AdminController.edit")
    public Result editHandler(@Valid AdminEditForm adminEditForm) {

        badRequestIfNull(adminEditForm);

        AdminEditResult adminEditResult = adminService.adminUpdate(adminEditForm);

        switch (adminEditResult) {
            case EDIT_SUCCESS:
                return renderJson(buildSuccessResult("修改成功"));
            case ROLE_NOT_EXIST:
                return renderJson(buildErrorResult("角色不存在", null));
            case ADMIN_NOT_EXIST:
                return renderJson(buildErrorResult("管理员不存在", null));
            case CANT_EDIT:
                return renderJson(buildErrorResult("管理员不可修改", null));
            case EDIT_FAILED:
                return renderJson(buildErrorResult("修改失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }

    public Result del(@Valid @Pattern(regexp = RegexpConsts.ID, message = "管理员ID格式不合法") String adminId) {

        AdminModel admin = adminService.getAdminById(adminId);

        notFoundIfNull(admin);

        AdminDelResult adminDelResult = adminService.adminDel(admin);

        switch (adminDelResult) {
            case DEL_SUCCESS:
                return renderJson(buildSuccessResult("删除成功"));
            case CANT_DEL:
                return renderJson(buildErrorResult("超级管理员,不允许删除.", null));
            case DEL_FAILED:
                return renderJson(buildErrorResult("删除失败,请重试.", null));
            default:
                throw new BadRequest();
        }

    }


}
