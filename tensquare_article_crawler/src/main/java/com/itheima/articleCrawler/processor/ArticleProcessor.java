package com.itheima.articleCrawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Component
public class ArticleProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().regex("https://my.oschina.net/u/[0-9]+/blog/[0-9]+").all());

        //往pipeline传递值
        String title = page.getHtml().xpath("//*[@id=\"mainScreen\"]/div/div[1]/div/div[2]/div[1]/div[2]/h2/text()").toString();
        String content = page.getHtml().xpath("//*[@id=\"articleContent\"]").toString();//正文
        if(title!=null && title.length()>0 && content !=null && content.length()>0){
            page.putField("title",title);
            page.putField("content",content);
        }else{
            //如果为空跳过
            page.setSkip(true);//跳过当前页面
        }

    }


    /**
     * 设置参数
     */
    @Override
    public Site getSite() {
        return Site.me().setTimeOut(1000).setTimeOut(2000).setRetryTimes(3);
    }
}
