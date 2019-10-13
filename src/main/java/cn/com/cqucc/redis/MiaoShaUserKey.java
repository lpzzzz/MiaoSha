package cn.com.cqucc.redis;

import cn.com.cqucc.redis.impl.BasePrefix;

public class MiaoShaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 3600*24*24*2; // 有效期是两天
    public MiaoShaUserKey(String prefix) {
        super(prefix);
    }

    public MiaoShaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoShaUserKey token = new MiaoShaUserKey(TOKEN_EXPIRE,"tk");
    public static MiaoShaUserKey name = new MiaoShaUserKey("name");
    public static MiaoShaUserKey getById = new MiaoShaUserKey(0, "id");
}
