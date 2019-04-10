package com.juzi.access;

import com.juzi.domain.User;
import com.juzi.exception.GlobalException;
import com.juzi.redis.AccessKey;
import com.juzi.redis.RedisService;
import com.juzi.result.CodeMsg;
import com.juzi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: juzi
 * @date: 2019/4/11
 * @time: 3:12
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            User user = getUser(request, response);
            UserContext.setUser(user);
            HandlerMethod method = (HandlerMethod)handler;
            AccessLimit accessLimit = method.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean b = accessLimit.needLogin();

            String key = request.getRequestURI();
            if(b){
                if(null == user){
                    throw new GlobalException(CodeMsg.SESSION_ERROR);
                }
                key += "_" + user.getId();
            }
            AccessKey accessKey = AccessKey.withExpire(seconds);
            Integer count = redisService.get(accessKey,key,Integer.class);

            if(null == count){
                redisService.set(accessKey,key,1);
            }else if(count > 0 && count < maxCount){
                redisService.incr(accessKey,key);
            }else {
                throw new GlobalException(CodeMsg.ACCESS_LIMIT_REACHED);
            }
        }
        return true;
    }

    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(UserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, UserService.COOKI_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(null == cookies){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
