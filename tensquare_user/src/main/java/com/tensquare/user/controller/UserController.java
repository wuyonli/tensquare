package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.pojo.Admin;
import io.jsonwebtoken.Claims;
import io.netty.handler.codec.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.IdWorker;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;
	
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		Claims claims = (Claims)request.getAttribute("admin_claims");
		if(claims==null){
			//没有权限
			return new Result(false,StatusCode.AUTHORIZATION_ERROR,"你不是管理员，权限不足");
		}

		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}



	// 发送短信验证码
	@RequestMapping(value="/sendsms/{mobile}",method= RequestMethod.POST)
	public Result sendsms(@PathVariable String mobile){
		userService.sendsms(mobile);
		return new Result(true,StatusCode.OK,"短信发送成功，请注意查收");
	}

	// 用户注册
	@RequestMapping(value="/register/{code}",method= RequestMethod.POST)
	public Result register(@PathVariable String code,@RequestBody User user){
		// 获取redis中的验证码
		String vcode = (String) redisTemplate.opsForValue().get("sms_" + user.getMobile());
		if ( vcode == null || vcode.equals("")){
			return new Result(true,StatusCode.AUTHORIZATION_ERROR,"验证码已过期!");
		}
		// 判断用户验证码是否正确
		if (vcode.equalsIgnoreCase(code)){
			userService.register(user);
			return new Result(true,StatusCode.OK,"注册成功!");
		}else {
			return new Result(true,StatusCode.AUTHORIZATION_ERROR,"验证码不正确,请重新输入!");
		}

	}


	// 用户登陆
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		User user1 = userService.login(user);
		if (user1 == null){
			return new Result(true,StatusCode.USER_PWD_ERROR,"手机号码或者密码错误!");
		}

		//如何认证，后面再看？
		//1 利用jjwt生成token加密字符串（往载荷加入roles角色）
		String token = jwtUtil.createJWT(user1.getId(),user1.getLoginname(),"user");
		//2 直接把token字符串返回前端
		Map data = new HashMap();
		data.put("name",user1.getLoginname());
		data.put("token",token);
		data.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
		return new Result(true,StatusCode.OK,"用户登陆成功",data);
	}

	// 增粉丝数
	@RequestMapping(value="/incfans/{userid}/{x}",method=RequestMethod.POST)
	public void incFanscount(@PathVariable String userid,@PathVariable int x){
		userService.incFanscount(userid,x);
	}

	/**
	 * 增加关注数
	 * @param userid
	 * @param x
	 */
	@RequestMapping(value="/incfollow/{userid}/{x}",method=RequestMethod.POST)
	public void incFollowcount(@PathVariable String userid,@PathVariable int x){
		userService.incFollowcount(userid,x);
	}

}
