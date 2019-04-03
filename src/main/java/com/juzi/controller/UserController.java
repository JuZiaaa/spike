package com.juzi.controller;

import com.juzi.domain.User;
import com.juzi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 1:00
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUser")
    public String getUserById(){
        User user = userService.getUserById(1);
        return user.getName();
    }
}
