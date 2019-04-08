package com.juzi.controller;

import com.juzi.vo.LoginVo;
import com.juzi.result.Result;
import com.juzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 19:35
 */
@Controller
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private UserService userService;

    /**
     * 跳转登录页面
     * @return
     */
    @RequestMapping(value = "to_login")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping(value = "/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        String token = userService.login(response,loginVo);
        return Result.success(token);
    }



}
