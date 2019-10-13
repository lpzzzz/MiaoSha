package cn.com.cqucc.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidtorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobileSrc) {

        // 使用正则表达式进行判断
        if (StringUtils.isEmpty(mobileSrc)) {
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(mobileSrc);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("14787005750"));
    }
}
