package com.juzi.controller;

import com.juzi.result.CodeMsg;
import com.juzi.result.Result;
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


    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","测试页面");
        return "hello";
    }

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
