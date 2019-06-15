package com.tensquare.user.filter;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 执行进行Controller的方法之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1 使用jjwt解析token字符串，是否合法（是否过期）
        //1.1 判断请求头是否有Authorization
        String auth = request.getHeader("Authorization");
        if(auth!=null){
            //1.2 判断auth是否以Bearer开头
            if(auth.startsWith("Bearer")){
                //1.3 截取出Token字符串
                String token = auth.substring(7);

                //1.4 解析Token字符串是否合法（是否过期）
                Claims body = jwtUtil.parseJWT(token);

                //验证通过啦
                if(body!=null){
                    //2 从载荷获取roles，如果roles是管理员
                    if(body.get("roles").equals("admin")){
                        //给标记
                        request.setAttribute("admin_claims",body);
                    }
                }
            }
        }

        //放行
        return true;
    }
}