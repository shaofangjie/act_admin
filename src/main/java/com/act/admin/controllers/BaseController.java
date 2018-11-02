package com.act.admin.controllers;

import act.app.ActionContext;
import act.conf.AppConfig;
import act.controller.Controller;
import act.storage.StorageServiceManager;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.osgl.Osgl;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import org.osgl.mvc.annotation.ResponseStatus;
import org.osgl.mvc.result.Result;
import org.osgl.storage.ISObject;
import org.osgl.storage.IStorageService;
import org.osgl.util.IO;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.osgl.storage.ISObject.ATTR_CONTENT_TYPE;
import static org.osgl.storage.ISObject.ATTR_FILE_NAME;

/**
 * Created by shaofangjie on 2017/5/5.
 */
public class BaseController extends Controller.Util {

    private static Logger logger = L.get(BaseController.class);

    @Inject
    private AppConfig conf;

    public Result loginOut(H.Session session) {
        session.clear();
        throw Controller.Util.redirect("LoginController.loginIndex");
    }

    private static void noCache(H.Response response) {
        long time = System.currentTimeMillis();
        response.header("Pragma", "No-cache");
        response.header("Cache-Control", "no-cache");
        response.header("Last-Modified", String.valueOf(time));
        response.header("Date", String.valueOf(time));
        response.header("Expires", String.valueOf(time));
    }

    public Result captcha(H.Response response, H.Session session) throws IOException {

        return binary(Osgl.visitor(out -> {
            try {
                noCache(response);
                response.header("Content-Type", "image/jpeg");
                ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(100, 38, 4, 3);
                session.cache("captcha", captcha.getCode(), 300);
                logger.debug(captcha.getCode());
                captcha.write(out.asOutputStream());
                IO.close(out.asOutputStream());
            } catch (Exception e) {
                return null;
            }
            return null;
        }));

    }

    @ResponseStatus(200)
    public JSONObject upload(H.Request request, ISObject file, StorageServiceManager ssMgr) {
        if (null != file) {
            String allowedFileType = conf.get("upload.file.type").toString();
            if (StringUtils.isEmpty(allowedFileType)) {
                return buildErrorResult("请配置允许上传的文件类型", null);
            }
            String contentType = file.getAttribute(ATTR_CONTENT_TYPE);
            String fileName = file.getAttribute(ATTR_FILE_NAME);
            if (null != contentType) {
                List<String> allowFileTypeList = Arrays.asList(allowedFileType.split("\\s*,\\s*"));
                String subContentType = contentType.substring(contentType.lastIndexOf("/") + 1);
                if (allowFileTypeList.contains(subContentType)) {
                    IStorageService storageService = ssMgr.storageService("upload");
                    String key = storageService.getKey();
                    file = storageService.put(key, file);
                    JSONObject success = new JSONObject();
                    success.put("url", file.getUrl());
                    success.put("name", fileName);
                    if (contentType.startsWith("image/")) {
                        success.put("image", true);
                    }
                    return buildSuccessResult(success);
                }
                return buildErrorResult("文件类型不支持", null);
            }
            return buildErrorResult("无法获得上传文件的类型", null);
        }
        return buildErrorResult("请选择文件", null);
    }

    public JSONObject getViolationErrMsg(ActionContext context) {
        JSONObject error = new JSONObject();
        context.violations().forEach((k, v) -> {
            error.put(k, v.getMessageTemplate());
        });
        return error;
    }

    public String getViolationErrStr(ActionContext context) {
        StringBuilder msg = new StringBuilder();
        context.violations().forEach((k, v) -> {
            msg.append(v.getMessageTemplate());
            msg.append(";");
        });
        return msg.toString();
    }

    public JSONObject buildSuccessResult(JSONObject extraInfo) {
        JSONObject result = buildSuccessResult("");
        result.put("msg", extraInfo);
        return result;
    }

    public JSONObject buildSuccessResult(String msg) {
        JSONObject result = new JSONObject();
        result.put("errcode", 0);
        result.put("msg", msg);
        return result;
    }

    public JSONObject buildErrorResult(String msg, JSONObject detailErr) {
        JSONObject result = new JSONObject();
        result.put("errcode", 1);
        if (null == detailErr) {
            result.put("detail", 0);
            result.put("msg", msg);
        } else {
            result.put("detail", 1);
            result.put("msg", detailErr);
        }
        return result;
    }

    public JSONObject buildTableResult(int code, String msg, int count, List<Object> pageData) {

        JSONObject result = new JSONObject();
        result.put("data", pageData);
        result.put("count", count);
        result.put("code", code);
        result.put("msg", msg);

        return result;

    }

}
