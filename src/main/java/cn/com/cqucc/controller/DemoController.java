package cn.com.cqucc.controller;


import cn.com.cqucc.domain.User;
import cn.com.cqucc.rabbitmq.MQReceiver;
import cn.com.cqucc.rabbitmq.MQSender;
import cn.com.cqucc.redis.RedisService;
import cn.com.cqucc.redis.UserKey;
import cn.com.cqucc.result.CodeMsg;
import cn.com.cqucc.result.Result;
import cn.com.cqucc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    MQSender mqSender;


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "hello springBoot!";
    }

     /*
        Controller分为两大类
            1、rest api json输出
            2. 页面

        编写Result类实现对返回Controller返回结果集的封装
      */

    @GetMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("success");
    }


    @GetMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @GetMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "张三");
        return "hello"; // 由于配置了thymeleaf的配置所以我们这里可以直接返回 页面名称
    }


    /**
     * 测试数据库的连接情况
     *
     * @param id
     * @return
     */
    @GetMapping("/findById")
    @ResponseBody
    public Result<User> findById(Integer id) {
        User user = userService.findById(id);
        return Result.success(user);
    }


    @GetMapping("/save")
    @ResponseBody
    public Result<Boolean> save() {
        return Result.success(userService.save());
    }


    @GetMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User values = redisService.get(UserKey.getById,""+1, User.class);
        return Result.success(values);
    }


    @GetMapping("/redis/set")
    @ResponseBody
    public Result<User> redisSet() {
        User user = new User(2,"22222");
        Boolean flag = redisService.set(UserKey.getById,""+2, user);
        User value = redisService.get(UserKey.getById,""+2, User.class);
        return Result.success(value);
    }




}
