package com.juzi.service;

import com.juzi.dao.OrderDao;
import com.juzi.domain.OrderInfo;
import com.juzi.domain.SpikeOrder;
import com.juzi.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 2:05
 */
@Service
public class OrderService {

    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderDao orderDao;

    @Transactional
    public OrderInfo createOrder(Long id, long goodsId){
        //生成订单
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(id);
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsId(goodsId);
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsPrice(goodsVo.getSpikePrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(new Date());
        long orderId = orderDao.insert(orderInfo);

        //生成秒杀订单
        SpikeOrder spikeOrder = new SpikeOrder();
        spikeOrder.setOrderId(orderId);
        spikeOrder.setUserId(id);
        spikeOrder.setGoodsId(goodsId);
        orderDao.createSpikeOrder(spikeOrder);

        return orderInfo;
    }
}
