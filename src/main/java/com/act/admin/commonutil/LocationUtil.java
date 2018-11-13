package com.act.admin.commonutil;

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

}
