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

import okhttp3.*;

import java.io.IOException;

/**
 * Created by shaofangjie on 2017/6/17.
 */

// Refer to https://github.com/square/okhttp/wiki/Recipes
public class AuthenticationClient {
    private final OkHttpClient client;

    public AuthenticationClient() {
        client = new OkHttpClient.Builder()
                .authenticator(new Authenticator() {
                    @Override public Request authenticate(Route route, Response response) throws IOException {
                        System.out.println("Authenticating for response: " + response);
                        System.out.println("Challenges: " + response.challenges());
                        String credential = Credentials.basic("jesse", "password1");
                        if (responseCount(response) >= 3) {
                            return null; // If we've failed 3 times, give up.
                        }
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }

                    private int responseCount(Response response) {
                        int result = 1;
                        while ((response = response.priorResponse()) != null) {
                            result++;
                        }
                        return result;
                    }
                }).build();
    }

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("http://publicobject.com/secrets/hellosecret.txt")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        System.out.println(response.body().string());
    }
}
