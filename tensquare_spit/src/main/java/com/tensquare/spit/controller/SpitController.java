package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {
    // 导入service
    @Autowired
    private SpitService spitService;

    // findAll
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> spitList = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",spitList);
    }

    // findOne
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findOne(@PathVariable String id){
        Spit spit = spitService.findOne(id);
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }
    // add
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    //update
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id ,@RequestBody Spit spit){
        spit.setId(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    // delete
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        spitService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    // 根据上级ID 查询吐槽数据  (没有parentId 是 吐槽 有parentId的是吐槽对应的评论)
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
    Page<Spit> pagedata = spitService.comment(parentid,page,size);
    return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pagedata.getTotalElements(),pagedata.getContent()) );

    }

    @Autowired
    private RedisTemplate redisTemplate;

    // 吐槽点赞
    @RequestMapping(value = "/thumbup/{id}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id){
        //模拟当前登录用户ID
        String userid = "1001";
        // 从redis中读取该用户的id
        String flag = (String) redisTemplate.opsForValue().get("thumbup_" + userid + "_" + id);
        if (flag != null && flag.equals("1")){  // 已经点过赞了

            return new Result(false,StatusCode.REPEAT_ERROR,"亲,您已经点过赞了哦!~~~");
        }
        // 没点过赞
        spitService.thumbup(id);
       redisTemplate.opsForValue().set("thumbup_" + userid + "_" + id,"1",1L, TimeUnit.DAYS);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}
