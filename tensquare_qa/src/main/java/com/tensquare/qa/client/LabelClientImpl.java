package com.tensquare.qa.client;

import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class LabelClientImpl implements LabelClient{

    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"熔断器启动了!");
    }
}
