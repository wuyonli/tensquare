package com.itheima.articleCrawler.pipeline;

import com.itheima.articleCrawler.dao.ArticleDao;
import com.itheima.articleCrawler.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.IdWorker;

import java.util.Date;

/**
 * 入库类
 */
@Component
public class ArticleDbPipeline implements Pipeline{



    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ArticleDao articleDao;

    /**
     * 频道id，set方法设置进来
     */
    private String channelid;
    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        Article a = new Article();
        a.setId(idWorker.nextId()+"");//id
        a.setTitle(resultItems.get("title"));//文章的标题
        a.setContent(resultItems.get("content"));//文章详情
        a.setCreatetime(new Date());//创建时间
        a.setState("1");//是否使用
        a.setThumbup(0);//点赞数
        a.setVisits(0);//访问数

        a.setChannelid(channelid);//频道id

        articleDao.save(a);
    }
}
