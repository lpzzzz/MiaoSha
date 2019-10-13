package cn.com.cqucc.redis;

import cn.com.cqucc.redis.impl.BasePrefix;

public class OrderKey extends BasePrefix {


    public OrderKey(String prefix) {
        super(prefix);
    }

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
