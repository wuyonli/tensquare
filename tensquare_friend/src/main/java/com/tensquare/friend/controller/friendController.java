package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class friendController {
    // 注入service
    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    // 添加好友
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addfriend(@PathVariable String friendid,@PathVariable String type){
        // 从request域中取出token
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return new Result(true, StatusCode.OK,"未登录或无权限访问");
        }
        // 如果是喜欢
        if (type.equals("1")){
            if (friendService.addFriend(claims.getId(),friendid)== 0){
                return new Result(true, StatusCode.OK,"已经添加此过好友");
            }
        }else {
            // 不喜欢 (拉入黑名单)
            friendService.addNoFriend(claims.getId(),friendid);// 向不喜欢的列表中添加
        }
        return new Result(true, StatusCode.OK, "操作成功");
    }

    // 删除好友
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deletefriend(@PathVariable String friendid){
      // 权限认证
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims == null){
            return new Result(true, StatusCode.OK, "未登录或者无权访问");
        }
        friendService.deleteFriend(claims.getId(),friendid);
        return new Result(true, StatusCode.OK, "删除成功!");
}

}
