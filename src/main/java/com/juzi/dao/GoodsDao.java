package com.juzi.dao;

import com.juzi.domain.Goods;
import com.juzi.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {
	
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.spike_price from spike_goods mg left join goods g on mg.goods_id = g.id")
	List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.spike_price from spike_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	@Update("update spike_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
	int reduceStock(Goods g);

	@Update("update spike_goods set stock_count = #{stockCount} where goods_id = #{goodsId}")
	int resetStock(Goods g);
	
}
