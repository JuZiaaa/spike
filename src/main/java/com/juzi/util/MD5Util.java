package com.juzi.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 22:44
 */
public class MD5Util {

    //固定salt
    private static final String salt = "1a2b3c4d";

    public static String md5(String md){
        return DigestUtils.md5Hex(md);
    }


    /**
     *
     * 加密密码转换为数据库密码
     * @param fromPsd
     * @param salt
     * @return
     */
    public static String formPsdToDBPsd(String fromPsd,String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + fromPsd + salt.charAt(3) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        System.out.println(str);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPsdToDBPsd(formPass, saltDB);
        return dbPass;
    }
    public static void main(String[] args) {

        System.out.println(inputPassToDbPass("123456","1a2b3c"));
    }
}
