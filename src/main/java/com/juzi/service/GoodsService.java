package com.juzi.service;

import com.juzi.dao.GoodsDao;
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

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

}
