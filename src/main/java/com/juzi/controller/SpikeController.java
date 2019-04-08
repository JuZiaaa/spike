package com.juzi.controller;

import com.juzi.domain.OrderInfo;
import com.juzi.domain.User;
import com.juzi.result.CodeMsg;
import com.juzi.service.GoodsService;
import com.juzi.service.OrderService;
import com.juzi.service.SpikeService;
import com.juzi.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 1:38
 */
@Controller
@RequestMapping("/spike")
public class SpikeController {

    @Autowired
    SpikeService spikeService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping(value = "/do_spike")
    public String spike(Model model,User user, @RequestParam("goodsId")long goodsId){

        model.addAttribute("user",user);
        //判断用户是否登录
        if(null == user){
           return "login";
        }
        //查询库存
        int stock = spikeService.queryStock(goodsId);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        if(stock <= 0){
            model.addAttribute("errmsg",CodeMsg.MIAO_SHA_OVER.getMsg());
            return "spike_fail";
        }
        //判断用户是否已秒杀过商品
        if(spikeService.isSpike(user,goodsId)){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "spike_fail";
        }
        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo = spikeService.doSpike(user,goodsId);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goodsVo);
        return "order_detail";
    }
}
