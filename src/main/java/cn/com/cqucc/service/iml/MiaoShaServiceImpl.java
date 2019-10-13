package cn.com.cqucc.service.iml;

import cn.com.cqucc.domain.MiaoshaOrder;
import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.domain.OrderInfo;
import cn.com.cqucc.redis.MiaoshaKey;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.service.GoodsService;
import cn.com.cqucc.service.MiaoShaService;
import cn.com.cqucc.service.OrderService;
import cn.com.cqucc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoShaServiceImpl implements MiaoShaService {


    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        //order_info maiosha_order

        // 当减库存成功才生成订单
        if (success) {
            return orderService.createOrder(user, goods);
        } else {

            // 如果
            setGoodsOver(goods.getId());
            return null;
        }
    }

    @Override
    public long getMiaoshaResult(Long userId, long goodsId) {

        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {//秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }


    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }
}
