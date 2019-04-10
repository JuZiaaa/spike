package com.juzi.redis;

/**
 * @author: juzi
 * @date: 2019/4/10
 * @time: 22:19
 */
public class OrderKey extends BasePrefix {
    public static OrderKey getSpikeOrderByUserIdAndGoodsId = new OrderKey("goug");
    private OrderKey(String prefix) {
        super( prefix);
    }

}
