package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 23:55
 */
public class UserKey extends BasePrefix{
    private UserKey(String prefix) {
        super(prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

   public static UserKey getById = new UserKey(30,"id");
   public static UserKey getByName = new UserKey(30,"name");
}
