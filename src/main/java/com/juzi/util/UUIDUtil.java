package com.juzi.util;

import java.util.UUID;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 23:00
 */
public class UUIDUtil {


    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
