package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 过滤器类型，在什么时候执行这个过滤器
     * pre  : 在到达过滤器执行
     * post:  在过滤器之后执行
     * route：在网关执行中
     * error: 当出错的时候执行
     */
    @Override
    public String filterType() {
        return "pre";
    }
    /**
     * 过滤器执行的顺序：  数值越小，越先执行，都是正整数 (用于有多个过滤器)
     */
    @Override
    public int filterOrder() {
        return 0;
    }
    /**
     * 是否启用：
     *  true： 可用
     *  false: 不可用
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 核心代码逻辑
     * return null 代表放行
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("zuul过滤器.");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        // http://192.168.207:6868/user/login?a=1,b=2  url 表示 http://192.168.207:6868/user/login
        // uri  表示:  /user/login
        String url = request.getRequestURL().toString();
        System.out.println("url:"+ url);
        String uri = request.getRequestURI().toString();
        System.out.println("uri:"+ uri);
        // 这是对管理员后台登录页面请求的放行.(由于没有请求头信息携带过来,,)
        if (url.indexOf("/admin/login")>0){
            System.out.println("登录页面:" + url);
            return null;
        }

        //  获取token
        String authHeader = request.getHeader("Authorization"); //获取头信息
       // 使用 jwtutil解析
        Claims claims = null;
        try {
            if (authHeader != null && authHeader.length() > 7){
                claims = jwtUtil.parseJWT(authHeader.substring(7));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 如果是管理员,放行
        if (claims != null && "admin".equals(claims.get("roles"))){
            currentContext.addZuulRequestHeader("Authorization",authHeader);
            return null;
        }
        //如果不是管理员，终止访问
        currentContext.setSendZuulResponse(false); //不会代理转发到微服务了
        currentContext.setResponseBody("您不是管理员，权限不足！");
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");

        return null;
    }
}
