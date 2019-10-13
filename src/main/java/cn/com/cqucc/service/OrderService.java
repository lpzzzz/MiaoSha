package cn.com.cqucc.service;

import cn.com.cqucc.domain.MiaoshaOrder;
import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.domain.OrderInfo;
import cn.com.cqucc.vo.GoodsVo;

public interface OrderService {

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods);

    public OrderInfo getOrderById(long orderId);

    public void deleteOrders();
}
