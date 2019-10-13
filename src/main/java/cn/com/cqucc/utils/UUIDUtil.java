package cn.com.cqucc.utils;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-",""); // 生成UUID并将中间的 - 进行替换
    }
}
