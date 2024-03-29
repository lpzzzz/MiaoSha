package cn.com.cqucc.redis;

import cn.com.cqucc.redis.impl.BasePrefix;

public class GoodsKey extends BasePrefix {

	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
	public static GoodsKey getMiaoShaGoodsStock= new GoodsKey(0, "gs");


}
