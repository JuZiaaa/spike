package com.juzi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author: juzi
 * @date: 2019/4/8
 * @time: 1:39
 */
@Mapper
public interface SpikeDao {

    @Select("select count(0) from spike_order where user_id = #{userId} and goods_id = #{goodsId}")
    int isSpike(@Param("userId")long userId, @Param("goodsId")long goodsId);

    @Select("select stock_count from spike_goods where goods_id = #{goodsId}")
    int queryStock(long goodsId);

    @Update("update spike_goods set stock_count = stock_count - 1 where goods_id = #{goodsId}")
    void reduceStockByGoodsId(long goodsId);
}
