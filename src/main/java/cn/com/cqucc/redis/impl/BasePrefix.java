package cn.com.cqucc.redis.impl;

import cn.com.cqucc.redis.KeyPrefix;

public abstract class BasePrefix implements KeyPrefix {

    private int expireSeconds; // 过期时间
    private String prefix; // 前缀


    public BasePrefix(String prefix) { // 0 代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 过期时间
     *
     * @return
     */
    @Override
    public int expireSeconds() { // 默认 0 代表永不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {  //
        String simpleName = getClass().getSimpleName();
        return simpleName + ":" + prefix;
    }
}
