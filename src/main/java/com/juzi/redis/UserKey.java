package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 23:55
 */
public class UserKey extends BasePrefix{

    public static final int TOKEN_EXPRIRE = 3600 * 24 * 2;

    private String prefix;

    private UserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
        this.prefix = prefix;
    }

   public static UserKey token = new UserKey(TOKEN_EXPRIRE,"tk");
   public static UserKey getById = new UserKey(0,"id");
}
