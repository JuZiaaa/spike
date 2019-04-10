package com.juzi.vo;

import com.juzi.domain.OrderInfo;

/**
 * @author: juzi
 * @date: 2019/4/9
 * @time: 20:56
 */
public class OrderDetailVo {

    private GoodsVo goods;
    private OrderInfo order;
    public GoodsVo getGoods() {
        return goods;
    }
    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }
    public OrderInfo getOrder() {
        return order;
    }
    public void setOrder(OrderInfo order) {
        this.order = order;
    }

}
