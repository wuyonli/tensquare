package com.itheima.userCrawler.task;


import com.itheima.userCrawler.pipeline.UserDbPipeline;
import com.itheima.userCrawler.processor.UserProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * 文章任务类
 */
@Component
public class UserTask {
    @Autowired
    private UserDbPipeline userDbPipeline;
    @Autowired
    private RedisScheduler redisScheduler;
    @Autowired
    private UserProcessor userProcessor;
    @Value("${article.url1}")
    private String url;

    /**
     * 爬取文章: 爬取数据库分类
     */
    @Scheduled(cron = "0 10 18 * * ?")
    public void webArticleTask(){

        //开启蜘蛛爬取内容
        Spider.create(userProcessor)
                .addUrl(url)
                .addPipeline(userDbPipeline)
                .setScheduler(redisScheduler)
                .start();
    }
}
