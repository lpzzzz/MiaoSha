package cn.com.cqucc.service;

import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.domain.OrderInfo;
import cn.com.cqucc.vo.GoodsVo;

import java.util.List;

public interface MiaoShaService {


    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods);


    public long getMiaoshaResult(Long userId, long goodsId);

    public void reset(List<GoodsVo> goodsList);
}
