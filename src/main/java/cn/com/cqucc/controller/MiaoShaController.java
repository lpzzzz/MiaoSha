package cn.com.cqucc.controller;

import cn.com.cqucc.domain.MiaoshaOrder;
import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.domain.OrderInfo;
import cn.com.cqucc.rabbitmq.MQSender;
import cn.com.cqucc.rabbitmq.MiaoshaMessage;
import cn.com.cqucc.redis.GoodsKey;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.result.CodeMsg;
import cn.com.cqucc.result.Result;
import cn.com.cqucc.service.GoodsService;
import cn.com.cqucc.service.MiaoShaService;
import cn.com.cqucc.service.OrderService;
import cn.com.cqucc.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoShaService miaoShaService;


    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;


    /**
     * GET 与 POST 有什么区别 ？
     * Get是幂等的 代表的是从服务端获取数据无论调用多少次不会对服务器端数据造成影响
     * POST 代表向服务端提交数据
     */
    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // 加载缓存 系统初始化的时候就将商品数量加载进来

        // 减库存

        Long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock, "" + goodsId);
        if (stock < 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);


        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);

        }

        // 入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);

        mqSender.sendMiaoShaMessage(mm);

        return Result.success(0);// 排队中

        /*
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return Result.error(CodeMsg.MIAO_SHA_OVER);

        }

        // 判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);


        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);

        }

        // 减库存 下订单 写入秒杀订单 这个操作必须在一个事务中进行 如果一步其他两步一起成功
        // 一步失败其他的一起失败 因此将其搞到一个service中进行

        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);

        return Result.success(orderInfo);

        */


    }

    /**
     * 实现这个方法  系统初始化的时候
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) {
            return;
        }


        for (GoodsVo goods : goodsVos) {
            // 系统启动的时候将商品数量加载到redis缓存中
            redisService.set(GoodsKey.getMiaoShaGoodsStock, "" + goods.getId(), goods.getStockCount());
        }
    }


    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result  =miaoShaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
