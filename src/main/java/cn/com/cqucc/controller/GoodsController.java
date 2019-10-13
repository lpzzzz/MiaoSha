package cn.com.cqucc.controller;


import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.redis.GoodsKey;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.result.Result;
import cn.com.cqucc.service.GoodsService;
import cn.com.cqucc.service.MiaoShaUserService;
import cn.com.cqucc.vo.GoodsDetailVo;
import cn.com.cqucc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private MiaoShaUserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping(value = "/to_list", produces = "text/html")
    public String toList(Model model, MiaoshaUser user,
                         HttpServletResponse response, HttpServletRequest request) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail2/{goodsId}")
    public String toDetail2(Model model, MiaoshaUser user, @PathVariable("goodsId") Long id) {
        model.addAttribute("user", user);
        // 查询商品详情列表
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(id);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime(); // 开始时间
        long endTime = goods.getEndDate().getTime();//结束时间

        long currentTime = System.currentTimeMillis();

        int miaoShaStatus = 0; // 秒杀状态

        int remainSeconds = 0;

        if (startTime < currentTime) { // 秒杀还未开始
            miaoShaStatus = 0;
            remainSeconds = (int) ((startTime - currentTime) / 1000);
        } else if (currentTime > startTime) {// 秒杀结束
            miaoShaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀中
            miaoShaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus", miaoShaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }


    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(Model model, MiaoshaUser user, @PathVariable("goodsId") Long goodsId) {
        model.addAttribute("user", user);
        // 查询商品详情列表
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime(); // 开始时间
        long endTime = goods.getEndDate().getTime();//结束时间

        long currentTime = System.currentTimeMillis();

        int miaoShaStatus = 0; // 秒杀状态

        int remainSeconds = 0;

        if (startTime < currentTime) { // 秒杀还未开始
            miaoShaStatus = 0;
            remainSeconds = (int) ((startTime - currentTime) / 1000);
        } else if (currentTime > startTime) {// 秒杀结束
            miaoShaStatus = 2;
            remainSeconds = -1;
        } else { // 秒杀中
            miaoShaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus", miaoShaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setMiaoshaStatus(miaoShaStatus);
        vo.setRemainSeconds(remainSeconds);
        return Result.success(vo);
    }

}
