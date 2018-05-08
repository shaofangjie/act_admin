package com.act.admin.commonutil.iplocation;

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

/**
 * Created by shaofangjie on 15/10/21.
 */
public class IPByteArray {

    private byte[] byteArray;

    public IPByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public void read(int position, byte[] bytes) {
        int p = position;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = read(p);
            p++;
        }
    }

    public byte read(int position) {
        return byteArray[position];
    }

}
