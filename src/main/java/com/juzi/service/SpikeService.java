package com.juzi.service;

import com.juzi.dao.SpikeDao;
import com.juzi.domain.OrderInfo;
import com.juzi.domain.SpikeOrder;
import com.juzi.domain.User;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.RedisService;
import com.juzi.redis.SpikeKey;
import com.juzi.util.UUIDUtil;
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
    RedisService redisService;
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
        if(!reduceStockByGoodsId(goodsId)){
            //商品秒杀结束
            setGoodSOver(goodsId);
            return null;
        }
        return orderService.createOrder(user.getId(),goodsId);
    }

    public boolean getGoodSOver(long goodSOver) {
        return redisService.exists(SpikeKey.isGoodsOver,"" + goodSOver);
    }
    public void setGoodSOver(long goodSOver) {
        redisService.set(SpikeKey.isGoodsOver,"" + goodSOver,true);
    }

    /**
     * 根据goodsId减少一个库存
     * @param goodsId
     */
    private boolean reduceStockByGoodsId(long goodsId) {
        return spikeDao.reduceStockByGoodsId(goodsId) > 0;
    }

    public int queryStock(long goodsId) {
        return spikeDao.queryStock(goodsId);
    }

    public long getSpikeResult(Long id, long goodsId) {
        //查询秒杀订单
        SpikeOrder order = orderService.getSpikeOrderByUserIdAndGoodsId(id,goodsId);
        if(null != order){
            return order.getOrderId();
        }else {
            if(getGoodSOver(goodsId)){
                //秒杀失败
                return -1;
            }else {
                //继续轮询
                return 0;
            }
        }
    }



    public boolean checkSpikePath(Long id, long goodsId,String path) {
        String oldPath = redisService.get(SpikeKey.getSpikePath,id +","+goodsId , String.class);
        return oldPath.equals(path);
    }
    public String createSpikePath(Long id, long goodsId) {
        //生成随机数
        String path = UUIDUtil.uuid();
        redisService.set(SpikeKey.getSpikePath,id +","+goodsId , path);
        return path;
    }
}
