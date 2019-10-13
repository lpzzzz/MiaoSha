package cn.com.cqucc.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /**
     * 发送大服务器端的
     * @param inputPass
     * @return
     */
    public static String inputPassToFromPass(String inputPass) {
        String str = "" + salt.charAt(0)+salt.charAt(2) + inputPass + salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 存数据库的
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass , String salt) {
        String str = "" + salt.charAt(0)+salt.charAt(2) + formPass + salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    /**
     * 将表单密码转换为数据密码
     * @param inputPass
     * @param saltDb
     * @return
     */
    public static String inputPassToDBPass(String inputPass,String saltDb) {
        String formPass = inputPassToFromPass(inputPass);
        return formPassToDBPass(formPass, saltDb);
    }

    public static void main(String [] args) {
       /* System.out.println(inputPassToFromPass("123456"));

        System.out.println(formPassToDBPass("123456","1a2b3c4d"));*/
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}
