package com.itheima.articleCrawler.task;

import com.itheima.articleCrawler.pipeline.ArticleDbPipeline;
import com.itheima.articleCrawler.processor.ArticleProcessor;
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
public class ArticleTask {
    @Autowired
    private ArticleDbPipeline articleDbPipeline;
    @Autowired
    private RedisScheduler redisScheduler;
    @Autowired
    private ArticleProcessor articleProcessor;
    @Value("${article.url1}")
    private String url;

    /**
     * 爬取文章: 爬取数据库分类
     */
    @Scheduled(cron = "0 14 17 * * ?")
    public void webArticleTask(){
        //设置频道
        articleDbPipeline.setChannelid("db");
        //开启蜘蛛爬取内容
        Spider.create(articleProcessor)
                .addUrl(url)
                .addPipeline(articleDbPipeline)
                .setScheduler(redisScheduler)
                .start();
    }
}
