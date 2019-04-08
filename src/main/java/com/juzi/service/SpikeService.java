package com.juzi.service;

import com.juzi.dao.SpikeDao;
import com.juzi.domain.OrderInfo;
import com.juzi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 1:39
 */
@Service
public class SpikeService {

    @Autowired
    SpikeDao spikeDao;
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;

    public boolean isSpike(User user, long goodsId) {
        long userId = user.getId();
        int i = spikeDao.isSpike(userId,goodsId);
        return i > 0;
    }

    /**
     * 秒杀商品
     * @param user
     * @param goodsId
     */
    @Transactional
    public OrderInfo doSpike(User user, long goodsId) {
        //减少库存，下订单，写入秒杀订单
        reduceStockByGoodsId(goodsId);
        return orderService.createOrder(user.getId(),goodsId);
    }

    /**
     * 根据goodsId减少一个库存
     * @param goodsId
     */
    private void reduceStockByGoodsId(long goodsId) {
        spikeDao.reduceStockByGoodsId(goodsId);
    }

    public int queryStock(long goodsId) {
        return spikeDao.queryStock(goodsId);
    }
}
