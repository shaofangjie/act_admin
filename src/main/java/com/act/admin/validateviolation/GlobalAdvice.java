package com.act.admin.validateviolation;

import act.app.ActionContext;
import act.handler.ValidateViolationAdvice;
import cn.hutool.json.JSONObject;
import com.act.admin.controllers.BaseController;
import org.osgl.util.C;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-11-05
 * Time: 8:59 PM
 */

public class GlobalAdvice implements ValidateViolationAdvice {

    @Inject
    BaseController baseController;

    @Override
    public Object onValidateViolation(Map<String, ConstraintViolation> violations, ActionContext context) {
        if (context.hasViolation()) {
            com.alibaba.fastjson.JSONObject error = baseController.getViolationErrMsg(context);
            return baseController.buildErrorResult("", error);
        }
        return null;
    }
}
