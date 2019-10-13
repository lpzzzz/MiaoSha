package cn.com.cqucc.controller;


import cn.com.cqucc.domain.MiaoshaUser;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info (Model model , MiaoshaUser user) {
        return Result.success(user);
    }
}
