package com.juzi.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 19:54
 */

public class ValidationUtil {
    private static final Pattern mobile_pattern = Pattern.compile("^1[34578]\\d{9}");


    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }
}
