package com.tensquare.qa.client;

import entity.Result;
import entity.StatusCode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 //指定 失败调用时 该指定按个类
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)
public interface LabelClient {
    // 查询一个  必须写全 访问的地址
    @RequestMapping(value = "/label/{id}" , method = RequestMethod.GET)
    // 这里 @PathVariable  必须写上("id")
    public Result findById(@PathVariable("id") String id);
}
