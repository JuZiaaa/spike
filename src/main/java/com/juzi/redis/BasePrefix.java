package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 23:50
 */
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix){
        //0默认为永不过期
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds,String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

        @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className +":" + prefix;
    }
}
