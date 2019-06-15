package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/label")
//跨域的解决注解
@CrossOrigin
public class LabelController {
//    注入service
    @Autowired
    private LabelService labelService;

    // 查询所有
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        // 查询所有
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK,"操作成功",labelList);
    }
    // 查询一个
    @RequestMapping(value = "/{id}" , method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(id));
    }

    // 添加一个
    @RequestMapping(method = RequestMethod.POST)
    public void add(@RequestBody Label label){
       labelService.add(label);
    }

    // 更新一个
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result updata(@PathVariable String id,@RequestBody Label label){
        label.setId(id); // 以url中的id为主
        labelService.updata(label);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    // 删除一个
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id){
        labelService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    // 多条件动态查询
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result search(@RequestBody Map searchMap){
        List<Label> list = labelService.search(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    // 分页查询
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result search(@PathVariable int page,@PathVariable int size,@RequestBody Map searchMap){
        Page<Label> pageData = labelService.findSearch(page,size,searchMap);
        return new Result(
                true,
                StatusCode.OK,
                "查询成功",
                new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }
}
