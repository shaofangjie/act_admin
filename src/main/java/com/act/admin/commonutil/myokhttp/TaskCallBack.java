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
import org.osgl.logging.L;
import org.osgl.logging.Logger;

import java.io.IOException;

/**
 * Created by shaofangjie on 2017/8/22.
 * 具体的回调类
 */
public class TaskCallBack implements Callback {
    private Logger logger = L.get(TaskCallBack.class);

    @Override
    public void onFailure(Call call, IOException e) {
        logger.error("请求失败");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (null != response.body()) {
            logger.debug(response.body().string());
            response.body().close();
            logger.debug("请求成功");
        } else {
            logger.error("请求失败");
        }

    }
}
