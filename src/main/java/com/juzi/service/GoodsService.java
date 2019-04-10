package com.juzi.service;

import com.juzi.dao.GoodsDao;
import com.juzi.redis.GoodsKey;
import com.juzi.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.juzi.vo.GoodsVo;


/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 0:07
 */
@Service
public class GoodsService {


    @Autowired
    GoodsDao goodsDao;

    @Autowired
    RedisService redisService;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        //查询redis
        GoodsVo goodsVo = redisService.get(GoodsKey.getGoodsDetail,"" + goodsId,GoodsVo.class);

        if(null == goodsVo){
            goodsVo = goodsDao.getGoodsVoByGoodsId(goodsId);
            redisService.set(GoodsKey.getGoodsDetail,"" + goodsId, goodsVo);

        }
        return goodsVo;
    }

}
