package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 23:48
 */
public interface KeyPrefix {
    public int expireSeconds();

    public String getPrefix();
}
