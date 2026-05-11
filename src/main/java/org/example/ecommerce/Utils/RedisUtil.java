package org.example.ecommerce.Utils;

import org.example.ecommerce.Config.AppConfigContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private static JedisPool getPool() {
        return (JedisPool) AppConfigContext.getContext().getAttribute("jedisPool");
    }

    public static void save(String key, String value) {
        JedisPool pool = getPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        }
    }

    public static void saveWithTTL(String key, String value, int seconds) {
        JedisPool pool = getPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(key, seconds, value);
        }
    }

    public static String get(String key) {
        JedisPool pool = getPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        }
    }

    public static void delete(String key) {
        JedisPool pool = getPool();
        try (Jedis jedis = pool.getResource()) {
            jedis.del(key);
        }
    }

    public static boolean exists(String key) {
        JedisPool pool = getPool();
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }
}
