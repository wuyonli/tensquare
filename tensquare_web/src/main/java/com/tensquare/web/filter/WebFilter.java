package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
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
        System.out.println("过滤器执行了.......");
        //向header中添加鉴权令牌  因为请求头来此处还是有的,比如转发到了其他需要微服务上时,request 的
        // header 是会丢失的.对于那些需要全权限认证的微服务来说,无法验证权限.
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取header
        HttpServletRequest request = requestContext.getRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization != null){
            requestContext.addZuulRequestHeader("Authorization",authorization);
        }
        return null;
    }
}
