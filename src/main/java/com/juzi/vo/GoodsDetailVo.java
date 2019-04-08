package com.juzi.vo;

import com.juzi.domain.User;

public class GoodsDetailVo {
	private int spikeStatus = 0;
	private int remainSeconds = 0;
	private GoodsVo goods ;
	private User user;
	public int getSpikeStatus() {
		return spikeStatus;
	}
	public void setSpikeStatus(int spikeStatus) {
		this.spikeStatus = spikeStatus;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
