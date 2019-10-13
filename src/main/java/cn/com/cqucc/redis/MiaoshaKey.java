package cn.com.cqucc.redis;

import cn.com.cqucc.redis.impl.BasePrefix;

public class MiaoshaKey extends BasePrefix {

	private MiaoshaKey(String prefix) {
		super(prefix);
	}
	public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
}
