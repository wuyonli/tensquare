package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.Searchservice;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private Searchservice searchservice;

    /**
     * 文章搜索
     */
    @RequestMapping(value = "/search/{page}/{size}" ,method = RequestMethod.GET)
    public Result search(@PathVariable int page, @PathVariable int size, @RequestParam(value="keyword") String keyword){
        System.out.println("keyword="+keyword);
        Page<Article> pageData = searchservice.search(keyword,page,size);
        return new Result(true, StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }
}
