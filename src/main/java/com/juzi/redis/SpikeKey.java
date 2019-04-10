package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/10
 * @time: 22:12
 */
public class SpikeKey extends BasePrefix{

    private SpikeKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SpikeKey isGoodsOver = new SpikeKey(0, "go");

    public static SpikeKey getSpikePath = new SpikeKey(60, "gsp");

}
