package com.act.admin;

import act.Act;
import act.controller.Controller;
import act.inject.DefaultValue;
import act.util.Output;
import com.alibaba.fastjson.JSONObject;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.mvc.result.Ok;
import org.osgl.mvc.result.Result;

public class AppEntry extends Controller.Util {

    @GetAction
    public Result home() {
        return ok();
    }

    public static void main(String[] args) throws Exception {
        Act.start("ACT_ADMIN");
    }

}
