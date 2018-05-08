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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import java.io.IOException;

/**
 * Created by shaofangjie on 2017/6/16.
 * 具体的回调类
 */
public class LocationPhoneCallBack implements Callback {
    private Logger logger = L.get(LocationPhoneCallBack.class);
    private String phone;

    @Override
    public void onFailure(Call call, IOException e) {
        logger.error("IP138 手机归属地接口异常");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        byte[] rawResult = response.body().bytes();
        String responseBody = new String(rawResult, "GBK");

        Document doc = Jsoup.parse(responseBody);

        Elements locationElements = doc.select("table tbody tr.tdc td.tdc2");



        if (0 == locationElements.size()) {
            logger.debug("归属地未知");
        } else {
            logger.debug("####################" + locationElements.get(1).text());
            logger.debug("####################" + locationElements.get(2).text());
        }
    }
}
