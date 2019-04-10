package com.juzi.service;

import com.juzi.dao.SpikeDao;
import com.juzi.domain.OrderInfo;
import com.juzi.domain.SpikeOrder;
import com.juzi.domain.User;
import com.juzi.exception.GlobalException;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.RedisService;
import com.juzi.redis.SpikeKey;
import com.juzi.result.CodeMsg;
import com.juzi.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

    public BufferedImage createVerifyCode(User user, long goodsId) {

        if(null == user && goodsId <= 0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();

        //把验证码存到redis
        Integer rnd = calc(verifyCode);
        redisService.set(SpikeKey.getSpikeVerifyCode,user.getId() +","+goodsId,rnd );
        return image;

    }

    private Integer calc(String verifyCode) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(verifyCode);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(Long id, long goodsId, Integer verifyCode) {
        Integer i = redisService.get(SpikeKey.getSpikeVerifyCode,id +","+goodsId,Integer.class);
        if(null == i){
            throw new GlobalException(CodeMsg.REQUEST_ILLEGAL);
        }
        if(i.equals(verifyCode)){
            redisService.delete(SpikeKey.getSpikeVerifyCode,id +","+goodsId);
            return true;
        }
        return false;
    }
}
