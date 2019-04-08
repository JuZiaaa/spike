package com.juzi.service;

import com.juzi.vo.LoginVo;
import com.juzi.exception.GlobalException;
import com.juzi.dao.UserDao;
import com.juzi.domain.User;
import com.juzi.redis.RedisService;
import com.juzi.redis.UserKey;
import com.juzi.result.CodeMsg;
import com.juzi.util.MD5Util;
import com.juzi.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: juzi
 * @date: 2019/4/4
 * @time: 0:57
 */
@Service
public class UserService {

    public static final String COOKI_NAME_TOKEN = "token";

    @Autowired
    UserDao userDao;
    @Autowired
    RedisService redisService;


    public User getUserById(long id){
        //取缓存
        User user = redisService.get(UserKey.getById,"" + id,User.class);
        if(null != user){
            return user;
        }

        //取数据库
        user = userDao.getUserById(id);
        //存缓存
        if(null != user){
            redisService.set(UserKey.getById,"" + id,user);
        }
        return user;
    }


    public String login(HttpServletResponse response, LoginVo loginVo) {
        if(null == loginVo){
           throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String psd = loginVo.getPassword();
        //判断用户是否存在
        User user = getUserById(Long.parseLong(mobile));
        if(null == user){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPsd = user.getPassword();
        String dbSalt = user.getSalt();
        String formPsd = MD5Util.formPsdToDBPsd(psd,dbSalt);
        if(!formPsd.equals(dbPsd)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成token
        String token = UUIDUtil.uuid();
        addCookie(response,token,user);

        return token;
    }

    private void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    public User getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        User user = redisService.get(UserKey.token, token, User.class);
        //延长有效期
        if(user != null) {
            addCookie(response, token, user);
        }
        return user;
    }
}
