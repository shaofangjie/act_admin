package com.act.admin.commonutil.redis;

import act.app.conf.AutoConfig;
import org.osgl.$;
import org.osgl.util.Const;

@AutoConfig("redis")
public class RedisConfig {

    private static final Const<String> host = $.constant("");
    private static final Const<Integer> port = $.constant(0);
    private static final Const<String> username = $.constant("");
    private static final Const<String> password = $.constant("");
    private static final Const<Integer> timeout = $.constant(0);
    private static final Const<Integer> database = $.constant(0);

    private static final Const<String> pool_maxTotal = $.constant("");
    private static final Const<String> pool_maxIdle = $.constant("");
    private static final Const<String> pool_minIdle = $.constant("");
    private static final Const<String> pool_maxWaitMillis = $.constant("");
    private static final Const<String> pool_testOnBorrow = $.constant("");
    private static final Const<String> pool_testOnReturn = $.constant("");

    public static Const<String> getHost() {
        return host;
    }

    public static Const<Integer> getPort() {
        return port;
    }

    public static Const<String> getPassword() {
        return password;
    }

    public static Const<String> getUsername() {
        return username;
    }

    public static Const<Integer> getTimeout() {
        return timeout;
    }

    public static Const<Integer> getDatabase() {
        return database;
    }

    public static Const<String> getMaxTotal() {
        return pool_maxTotal;
    }

    public static Const<String> getMaxIdle() {
        return pool_maxIdle;
    }

    public static Const<String> getMinIdle() {
        return pool_minIdle;
    }

    public static Const<String> getMaxWaitMillis() {
        return pool_maxWaitMillis;
    }

    public static Const<String> getTestOnBorrow() {
        return pool_testOnBorrow;
    }

    public static Const<String> getTestOnReturn() {
        return pool_testOnReturn;
    }
}
