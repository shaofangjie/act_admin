package com.act.admin.validateviolation;

import act.app.ActionContext;
import act.handler.ValidateViolationAdvice;
import com.act.admin.controllers.BaseController;
import org.osgl.mvc.result.RenderJSON;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: shaofangjie
 * Date: 2018-11-05
 * Time: 8:57 PM
 */

public class LayTableVaildateAdvice implements ValidateViolationAdvice {

    @Inject
    BaseController baseController;

    @Override
    public Object onValidateViolation(Map<String, ConstraintViolation> violations, ActionContext context) {

        String error = baseController.getViolationErrStr(context);

        throw new RenderJSON(baseController.buildTableResult(1, error, 0, null));
    }
}
