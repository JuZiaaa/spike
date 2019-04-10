package com.juzi.activemq;

import com.juzi.domain.User;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.RedisService;
import com.juzi.service.GoodsService;
import com.juzi.service.SpikeService;
import com.juzi.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author: juzi
 * @date: 2019/4/10
 * @time: 0:05
 */
@Component
public class MqConsumer {

    @Autowired
    SpikeService spikeService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;

    @JmsListener(destination = MqConfig.SPIKE_QUEUE)
    public void receiveMsg(String msg){
        SpikeMessage message = RedisService.stringToBean(msg,SpikeMessage.class);
        long goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount() <= 0){
            return;
        }
        //判断用户是否已秒杀过商品
        if(spikeService.isSpike(user,goodsId)){
            return;
        }
        //减库存，下订单，写入秒杀订单
        spikeService.doSpike(user,goodsId);
    }

}
