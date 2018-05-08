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


import org.osgl.logging.L;
import org.osgl.logging.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;


public class IPLocationUtil {

    private static Logger logger = L.get(IPLocationUtil.class);

    /**
     * 将Classpath下文件转为Byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] getByteArrayFromClasspathFile(String filePath) {

        //FileInputStream fin = new FileInputStream(file);
        InputStream inputStream = null;
        byte[] array = null;
        try {
            //InputStream inputStream = ClassLoaderUtil.getResourceAsStream(fileName, IPLocationUtil.class);
            inputStream = new FileInputStream(filePath);
            array = getByteArrayFromInputStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("InputStream错误", e);
        }
        return array;
    }

    /**
     * 将InputStream转换为Byte数组
     *
     * @param inputStream
     * @return
     */
    public static byte[] getByteArrayFromInputStream(InputStream inputStream) {
        byte[] array = null;
        try {
            array = new byte[inputStream.available()];
            inputStream.read(array, 0, array.length);
        } catch (IOException e) {
            logger.error("将InputStream转换为Byte数组错误", e);
        }
        return array;
    }

    /**
     * 从ip的字符串形式得到字节数组形式
     *
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     */
    public static byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            logger.error("从ip的字符串形式得到字节数组形式报错"+e.toString());
        }
        return ret;
    }

    /**
     * 根据某种编码方式将字节数组转换成字符串
     *
     * @param b        字节数组
     * @param offset   要转换的起始位置
     * @param len      要转换的长度
     * @param encoding 编码方式
     * @return 如果encoding不支持，返回一个缺省编码的字符串
     */
    public static String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(b, offset, len);
        }
    }
}

