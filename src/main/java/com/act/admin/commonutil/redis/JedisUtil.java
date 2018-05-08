package com.act.admin.commonutil.redis;

import org.apache.commons.lang3.StringUtils;
import org.osgl.logging.L;
import org.osgl.logging.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {

    private static Logger logger = L.get(JedisUtil.class);

    private volatile static JedisUtil jedisUtil;
    private final JedisPool pool;

    private JedisUtil() {

        // 创建jedis池配置实例
        JedisPoolConfig config = new JedisPoolConfig();
        String host = RedisConfig.getHost().get();
        int port = RedisConfig.getPort().get();
        int timeout = RedisConfig.getTimeout().get();
        String username = RedisConfig.getUsername().get();
        String password = RedisConfig.getPassword().get();
        int database = RedisConfig.getDatabase().get();

        // 设置池配置项值
        String maxTotal = RedisConfig.getMaxTotal().get();
        config.setMaxTotal(Integer.parseInt(maxTotal));

        String maxIdle = RedisConfig.getMaxIdle().get();
        config.setMaxIdle(Integer.parseInt(maxIdle));

        String minIdle = RedisConfig.getMinIdle().get();
        config.setMinIdle(Integer.parseInt(minIdle));

        String maxWaitMillis = RedisConfig.getMaxWaitMillis().get();
        config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));

        String testOnBorrow = RedisConfig.getTestOnBorrow().get();
        config.setTestOnBorrow("true".equals(testOnBorrow));

        String testOnReturn = RedisConfig.getTestOnReturn().get();
        config.setTestOnReturn("true".equals(testOnReturn));

        logger.debug("host->" + host + ",port->" + port + ",timeout=" + timeout + ",username:" + username + ",password:" + password);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            pool = new JedisPool(config, host, port, timeout, password, database, username);
        } else if (StringUtils.isBlank(username) && StringUtils.isNotBlank(password)) {
            pool = new JedisPool(config, host, port, timeout, password, database, null);
        } else if (StringUtils.isBlank(username) && StringUtils.isBlank(password)) {
            pool = new JedisPool(config, host, port, timeout, null, database, null);
        } else {
            pool = null;
        }

    }

    public static JedisUtil getInstance() {
        if (jedisUtil == null) {
            synchronized (JedisUtil.class) {
                if (jedisUtil == null) {
                    try {
                        jedisUtil = new JedisUtil();
                    } catch (Exception e) {
                        logger.debug("创建jedis连接池失败" + e.getMessage());
                    }

                }
            }
        }
        return jedisUtil;
    }

    public Jedis getResource() {

        return pool.getResource();
    }

    public void destroy() {
        // when closing your application:
        logger.debug("jedis destroy");
        pool.destroy();
    }

    public void close() {
        pool.close();
    }

    private static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }

    private static void returnBrokenResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnBrokenResource(jedis);
        }
    }

    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(String key, int timeOut, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key, timeOut, value);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(String key, Object object) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key.getBytes(), serialize(object));
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String set(String key, int timeOut, Object object) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key.getBytes(), timeOut, serialize(object));
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String hmsetObject(String key, HashMap<byte[], byte[]> hashMap) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key.getBytes(), hashMap);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String hmset(String key, HashMap<String, String> hashMap) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hmset(key, hashMap);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return "0";
        } finally {
            returnResource(pool, jedis);
        }
    }

    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return 0L;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Set keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.keys(pattern);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return null;
        } finally {
            returnResource(pool, jedis);
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        String value = null;
        try {
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Object getObject(String key) {
        Jedis jedis = null;
        Object obj;
        try {
            jedis = pool.getResource();
            byte[] value = jedis.get(key.getBytes());
            if (null == value) {
                return null;
            }
            obj = unserizlize(value);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
            return null;
        } finally {
            returnResource(pool, jedis);
        }
        return obj;
    }

    public List<byte[]> hmgetObject(String key, byte[]... fields) {
        Jedis jedis = null;
        List<byte[]> value = null;
        try {
            jedis = pool.getResource();
            value = jedis.hmget(key.getBytes(), fields);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Map<String, String> hmgetall(String key) {
        Jedis jedis = null;
        Map<String, String> value = null;
        try {
            jedis = pool.getResource();
            value = jedis.hgetAll(key);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public List<byte[]> hmvals(String key) {
        Jedis jedis = null;
        List<byte[]> value = null;
        try {
            jedis = pool.getResource();
            value = jedis.hvals(key.getBytes());
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Long hincrby(String key, String field, Long incrNumber) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = pool.getResource();
            value = jedis.hincrBy(key, field, incrNumber);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Long del(String key) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = pool.getResource();
            value = jedis.del(key);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Long hmdel(String key) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = pool.getResource();
            value = jedis.hdel(key);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Long delObject(String key) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = pool.getResource();
            value = jedis.del(key.getBytes());
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public Long publish(String channel, String message) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = pool.getResource();
            jedis.publish(channel, message);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
        return value;
    }

    public void subcribe(JedisPubSub jedisPubSub, String channel) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channel);
        } catch (Exception e) {
            returnBrokenResource(pool, jedis);
            e.printStackTrace();
        } finally {
            returnResource(pool, jedis);
        }
    }

    //序列化
    public static byte[] serialize(Object obj) {
        ObjectOutputStream obi = null;
        ByteArrayOutputStream bai = null;
        try {
            bai = new ByteArrayOutputStream();
            obi = new ObjectOutputStream(bai);
            obi.writeObject(obj);
            byte[] byt = bai.toByteArray();
            return byt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //反序列化
    public static Object unserizlize(byte[] byt) {
        ObjectInputStream oii = null;
        ByteArrayInputStream bis = null;
        bis = new ByteArrayInputStream(byt);
        try {
            oii = new ObjectInputStream(bis);
            Object obj = oii.readObject();
            return obj;
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


}
