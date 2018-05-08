package com.act.admin.commonutil;

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

import com.act.admin.commonutil.iplocation.IpUtil;
import com.act.admin.commonutil.myokhttp.OkHttpRequestManager;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osgl.http.H;
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by shaofangjie on 15/9/18.
 */
public class LocationUtil {

    private static Logger logger = L.get(LocationUtil.class);

    public String getLocationByIP(final String ip) {

        String result;
        if (StringUtils.isEmpty(ip)) {
            return null;
        }
        try {
            result = IpUtil.getInstance().getIPAdress(ip);//输入查询的IP地址
        } catch (Exception e) {
            result = "IP归属地接口异常";
            logger.error("IP归属地接口异常", e);

        }

        return result;
    }

    public String getRealIp(H.Request request) {

        String ipRex = "^(?:(?:1[0-9][0-9]\\.)|(?:2[0-4][0-9]\\.)|(?:25[0-5]\\.)|(?:[1-9][0-9]\\.)|(?:[0-9]\\.)){3}(?:(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5])|(?:[1-9][0-9])|(?:[0-9]))$";

        String ip = request.header("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.header("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.ip();
        }

        if (ip.length() > 15) {
            int pos = ip.indexOf(",");
            if (pos > 0) {
                ip = ip.substring(0, pos);
            }
        }

        if (!Pattern.matches(ipRex, ip)) {
            ip = "0.0.0.0";
        }

        return ip;

    }

    public String getLocationByPhone(final String phone) {

        if (StringUtils.isEmpty(phone) || phone.length() != 11) {
            return "手机号码错误";
        }

        return this.getLocationByphoneOnline(phone);

    }

    private String getLocationByphoneOnline(final String phone) {

        //http://www.ip138.com:8080/search.asp?action=mobile&mobile=18601242222
        String phoneCode = "";
        if (phone.length() == 11) {
            phoneCode = phone.substring(0, 7);
        } else {
            return "手机号码错误";
        }

        String requestUrl = "http://www.ip138.com:8080/search.asp?action=mobile&mobile=" + phone;
        String location = "";

        OkHttpRequestManager requestManager = OkHttpRequestManager.getInstance();
        Map<String, String> params = new HashMap<>();
        String responseResult = requestManager.requestGetBySyn(requestUrl, params, null, "GBK");

        logger.debug(responseResult);

        Document doc = Jsoup.parse(responseResult);

        Elements locationElements = doc.select("table tbody tr.tdc td.tdc2");

        if (0 == locationElements.size()) {
            location = "归属地未知";
        }

        logger.debug(location);

        return location;
    }

}
