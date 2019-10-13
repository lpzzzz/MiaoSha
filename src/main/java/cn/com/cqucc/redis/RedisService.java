package cn.com.cqucc.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 获取键 所对应的值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        // 释放连接
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return stringToBean(str, clazz);// 将获取的值转换为 对应的类型
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 如何将一个字符串转化为一个bean对象
     *
     * @param str
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> tClass) {

        if (str == null || str.length() <= 0) {
            return null;
        }
        if (tClass == int.class || tClass == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (tClass == String.class) {
            return (T) str;
        } else if (tClass == long.class || tClass == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), tClass);
        }
    }


    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        // 释放连接
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            int expireSeconds = prefix.expireSeconds();

            if (expireSeconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, expireSeconds, str);
            }
            jedis.set(realKey, str);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将bean转换为String
     *
     * @param value
     * @param <T>
     * @return
     */
    public static  <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }

        Class<?> aClass = value.getClass();
        if (aClass == int.class || aClass == Integer.class) {
            return "" + value;
        } else if (aClass == String.class) {
            return (String) value;
        } else if (aClass == long.class || aClass == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 判断一个key是否存在
     *
     * @param prefix
     * @param key
     * @return
     */
    public boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     *增加值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(  realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 释放连接
     *
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 删除
     * */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            long ret =  jedis.del(key);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }
}
