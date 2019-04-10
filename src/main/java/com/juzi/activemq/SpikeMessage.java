package com.juzi.activemq;

import com.juzi.domain.User;

/**
 * @author: juzi
 * @date: 2019/4/10
 * @time: 20:50
 */
public class SpikeMessage {
    private User user;
    private long goodsId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
