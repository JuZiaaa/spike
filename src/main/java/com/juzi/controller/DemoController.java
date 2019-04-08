package com.juzi.controller;

import com.juzi.domain.User;
import com.juzi.redis.KeyPrefix;
import com.juzi.redis.RedisService;
import com.juzi.redis.UserKey;
import com.juzi.result.CodeMsg;
import com.juzi.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: juzi
 * @date: 2019/4/3
 * @time: 21:59
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","测试页面");
        return "hello";
    }

//    @RequestMapping("/setAndGet")
//    @ResponseBody
//    public Result<String> setAndGet(){
//        User user =new User();
//        user.setId(1);
//        user.setName("1111");
//        redisService.set(UserKey.getById,"1",user);
//        User user1 = redisService.get(UserKey.getById,"1",User.class);
//        return Result.success(user1.getName());
//    }

    @RequestMapping("/getSuccess")
    @ResponseBody
    public Result<String> getSuccess(){
        return Result.success("成功了");
    }

    @ResponseBody
    @RequestMapping("/getError")
    public Result<String> geterror(){
        return Result.error(CodeMsg.SERVER_ERROR,"失败了");
    }
}
