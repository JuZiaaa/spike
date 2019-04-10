package com.juzi.controller;

import com.juzi.activemq.MqProduct;
import com.juzi.activemq.SpikeMessage;
import com.juzi.domain.OrderInfo;
import com.juzi.domain.User;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.RedisService;
import com.juzi.result.CodeMsg;
import com.juzi.result.Result;
import com.juzi.service.GoodsService;
import com.juzi.service.OrderService;
import com.juzi.service.SpikeService;
import com.juzi.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 1:38
 */
@Controller
@RequestMapping("/spike")
public class SpikeController implements InitializingBean{

    @Autowired
    SpikeService spikeService;
    @Autowired
    OrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    MqProduct mqProduct;

    private HashMap<Long,Boolean> localOverMap = new HashMap<>();


    /**
     * 系统初始化,初始化商品
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.listGoodsVo();
        if(null != list && list.size() > 0 ){
            for(GoodsVo goodsVo : list){
                redisService.set(GoodsKey.getSpikeGoodsStock,"" + goodsVo.getId(),goodsVo.getStockCount());
                localOverMap.put(goodsVo.getId(),false);
            }
        }
    }

    @RequestMapping(value = "/do_spike")
    @ResponseBody
    public Result<Integer> spike(Model model, User user, @RequestParam("goodsId")long goodsId){

        model.addAttribute("user",user);
        //判断用户是否登录
        if(null == user){
           return Result.error(CodeMsg.SESSION_ERROR);
        }
        //查询内存
        if(localOverMap.get(goodsId)){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存
        Long stock = redisService.decr(GoodsKey.getSpikeGoodsStock,"" + goodsId);
        if(stock < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断用户是否已秒杀过商品
        if(spikeService.isSpike(user,goodsId)){
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        SpikeMessage spikeMessage = new SpikeMessage();
        spikeMessage.setUser(user);
        spikeMessage.setGoodsId(goodsId);
        mqProduct.sengSpikeMsg(spikeMessage);

        //返回消息：排队中
        return Result.success(0);
    }
    @RequestMapping(value = "/result")
    @ResponseBody
    public Result<Long> result(Model model, User user, @RequestParam("goodsId")long goodsId){
        model.addAttribute("user",user);
        //判断用户是否登录
        if(null == user){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = spikeService.getSpikeResult(user.getId(),goodsId);
        return Result.success(result);
    }

}
