package com.juzi.service;

import com.juzi.dao.UserDao;
import com.juzi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 0:57
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUserById(int id){
        return userDao.getUserById(id);
    }
}
