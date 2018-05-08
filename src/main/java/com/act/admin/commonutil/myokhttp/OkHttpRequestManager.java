package com.act.admin.commonutil.myokhttp;

/*-
 * #%L
 * act_admin
 * %%
 * Copyright (C) 2018 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import act.app.App;
import act.conf.AppConfig;
import okhttp3.*;
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by shaofangjie on 2017/6/16.
 */
public class OkHttpRequestManager {
    private Logger logger = L.get(OkHttpRequestManager.class);
    // okHttp 使用文档 https://github.com/square/okhttp/wiki/Calls
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static volatile OkHttpRequestManager mInstance;//单例引用
    private OkHttpClient mOkHttpClient;//okHttpClient 实例

    /**
     * 初始化RequestManager
     */
    private OkHttpRequestManager() {
        AppConfig conf = App.instance().config();
        Object connectTimeoutConfig = conf.get("myokhttp.connectTimeout");
        int connectTimeout = null == connectTimeoutConfig ? 10 : Integer.valueOf(connectTimeoutConfig.toString());
        Object readTimeoutConfig = conf.get("myokhttp.readTimeout");
        int readTimeout = null == readTimeoutConfig ? 10 : Integer.valueOf(readTimeoutConfig.toString());
        Object writeTimeoutConfig = conf.get("myokhttp.writeTimeout");
        int writeTimeout = null == writeTimeoutConfig ? 10 : Integer.valueOf(writeTimeoutConfig.toString());
        //初始化OkHttpClient
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(readTimeout, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }

    /**
     * 获取单例引用
     *
     * @return
     */
    public static OkHttpRequestManager getInstance() {
        OkHttpRequestManager inst = mInstance;
        if (inst == null) {
            synchronized (OkHttpRequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new OkHttpRequestManager();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * okHttp get同步请求
     *
     * @param requestUrl 接口地址
     * @param paramsMap  请求参数
     * @param headers    请求头信息
     */
    public String requestGetBySyn(String requestUrl, Map<String, String> paramsMap, Map<String, String> headers, String charset) {
        try {
            if (paramsMap.size() > 0) {
                //补全请求地址
                String params = getParams(paramsMap);
                requestUrl = String.format("%s?%s", requestUrl, params);
            }
            //创建一个请求
            Request request = addHeaders(headers).url(requestUrl).build();
            //执行请求
            Response response = mOkHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new MyOkHttpException("Unexpected response " + response);
            }
            logger.debug("Response is %s with requestUrl %s", response, requestUrl);
            byte[] rawResult = response.body().bytes();
            if (null == charset) {
                charset = "UTF8";
            }
            return new String(rawResult, charset);
        } catch (Exception e) {
            logger.error("Exception of requestGetBySyn with requestUrl: " + requestUrl + e.getMessage());
            return null;
        }
    }

    private String postBySyn(Request.Builder builder, RequestBody body) {
        String result = null;
        try {
            final Request request = builder.post(body).build();
            //创建一个Call
            final Call call = mOkHttpClient.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (!response.isSuccessful()) {
                throw new MyOkHttpException("Unexpected response " + response);
            }
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception of postBySy", e.getMessage());
        }
        return result;
    }

    /**
     * okHttp post同步请求 stream
     *
     * @param requestUrl 接口地址
     * @param mediaType  发送的post数据格式, 为null，使用Json格式
     * @param content    发送的数据
     * @param headers    请求参数, 为null，则不传递header
     */
    public String requestPostBySyn(String requestUrl, MediaType mediaType, String content, Map<String, String> headers) {
        String result = null;
        if (null != content) {
            if (null == mediaType) {
                mediaType = MEDIA_TYPE_JSON;
            }
            result = postBySyn(addHeaders(headers).url(requestUrl), RequestBody.create(mediaType, content));
        } else {
            logger.error("参数不完整，content不能为空....");
        }
        return result;
    }

    /**
     * okHttp post同步请求 stream
     *
     * @param requestUrl 接口地址
     * @param paramsMap  请求参数
     * @param headers    请求参数, 为null，则不传递header
     */
    public String requestPostBySyn(String requestUrl, Map<String, String> paramsMap, Map<String, String> headers) {
        String result = null;
        try {
            if (null != paramsMap && paramsMap.size() > 0) {
                String params = getParams(paramsMap);
                result = requestPostBySyn(requestUrl, MEDIA_TYPE_JSON, params, headers);
            } else {
                logger.error("请求内容不能为空，请求路径为 " + requestUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception of requestPostBySyn with url: " + requestUrl + e.getMessage());
        }
        return result;
    }

    //处理参数
    private String getParams(Map<String, String> paramsMap) throws UnsupportedEncodingException {
        StringBuilder params = new StringBuilder();
        int pos = 0;
        for (String key : paramsMap.keySet()) {
            if (pos > 0) {
                params.append("&");
            }
            //对参数进行URLEncoder
            params.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
            pos++;
        }
        return params.toString();
    }


    /**
     * okHttp post同步请求 文件
     *
     * @param requestUrl 接口地址
     * @param mediaType  发送的file数据格式, 为null，使用Json格式
     * @param file       请求的body体
     * @param headers    请求参数, 为null，则不传递header
     */
    public String requestPostBySyn(String requestUrl, MediaType mediaType, File file, Map<String, String> headers) {
        if (null == mediaType) {
            mediaType = MEDIA_TYPE_JSON;
        }
        return postBySyn(addHeaders(headers).url(requestUrl), RequestBody.create(mediaType, file));
    }

    /**
     * okHttp post同步请求表单提交
     *
     * @param requestUrl 接口地址
     * @param paramsMap  表单数据
     * @param headers    请求header参数, 为null，则不传递header
     */

    public String requestPostBySynWithForm(String requestUrl, Map<String, String> paramsMap, Map<String, String> headers) {
        if (null != paramsMap && paramsMap.size() > 0) {
            //创建一个FormBody.Builder
            FormBody.Builder formBuilder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                formBuilder.add(key, paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = formBuilder.build();
            return postBySyn(addHeaders(headers).url(requestUrl), formBody);
        }
        logger.error("post参数不能为空, 请求url：%s", requestUrl);
        return null;
    }

    /**
     * okHttp post同步请求upload form提交
     *
     * @param requestUrl 接口地址
     * @param paramsMap  表单数据
     * @param mediaType  上传文件的类型 如image/png
     * @param localFile  要上传的本地文件
     * @param uploadKey  要上传的文件在form表单中的key
     * @param headers    请求header参数, 为null，则不传递header
     */
    public String requestPostBySynWithMultipart(String requestUrl, Map<String, String> paramsMap, MediaType mediaType, File localFile, String uploadKey, Map<String, String> headers) {
        if (null != paramsMap && paramsMap.size() > 0) {
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (String key : paramsMap.keySet()) {
                if (key == uploadKey) {
                    multipartBuilder.addFormDataPart(uploadKey, paramsMap.get(key), RequestBody.create(mediaType, localFile));
                } else {
                    multipartBuilder.addFormDataPart(key, paramsMap.get(key));
                }
            }
            return postBySyn(addHeaders(headers).url(requestUrl), multipartBuilder.build());
        }
        logger.error("post参数不能为空, 请求url：%s", requestUrl);
        return null;
    }

    /**
     * okHttp get异步请求
     *
     * @param requestUrl 接口地址
     * @param paramsMap  请求参数
     * @param headers    请求头信息
     * @param callback   回调接口
     */
    public void requestGetByAsyn(String requestUrl, Map<String, String> paramsMap, Map<String, String> headers, Callback callback) {
        try {
            if (null != paramsMap && paramsMap.size() > 0) {
                //补全请求地址
                String params = getParams(paramsMap);
                requestUrl = String.format("%s?%s", requestUrl, params);
            }
            final Request request = addHeaders(headers).url(requestUrl).build();
            mOkHttpClient.newCall(request).enqueue(callback);
        } catch (Exception e) {
            logger.error("Exception of requestGetByAsyn with url: " + requestUrl + e.getMessage());
        }
    }

    /**
     * okHttp post异步请求
     *
     * @param requestUrl 接口地址
     * @param paramsMap  请求参数
     * @param headers    请求头信息
     * @param callback   回调接口
     */
    public void requestPostByAsyn(String requestUrl, Map<String, String> paramsMap, Map<String, String> headers, Callback callback) {
        try {
            if (null != paramsMap && paramsMap.size() > 0) {
                String params = getParams(paramsMap);
                final Request request = addHeaders(headers).post(RequestBody.create(MEDIA_TYPE_JSON, params)).build();
                mOkHttpClient.newCall(request).enqueue(callback);
            } else {
                logger.error("请求内容不能为空，请求路径为 " + requestUrl);
            }
        } catch (Exception e) {
            logger.error("Exception of requestPostByAsyn with url: " + requestUrl + e.getMessage());
        }
    }

    /**
     * okHttp post异步请求
     *
     * @param requestUrl 接口地址
     * @param mediaType 接口地址
     * @param content  请求参数
     * @param headers    请求头信息
     * @param callback   回调接口
     */
    public void requestPostByAsyn(String requestUrl, MediaType mediaType, String content, Map<String, String> headers, Callback callback) {
        if (null == mediaType) {
            mediaType = MEDIA_TYPE_JSON;
        }
        final Request request = addHeaders(headers).post(RequestBody.create(mediaType, content)).build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders(Map<String, String> headers) {
        Request.Builder builder = new Request.Builder();
        if (null != headers && headers.size() > 0) {
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }
        return builder;
    }
}
