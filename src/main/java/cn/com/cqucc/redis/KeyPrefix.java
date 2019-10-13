package cn.com.cqucc.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
