package com.itheima.userCrawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Component
public class UserProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().regex("https://my.oschina.net/u/[0-9]+/blog/[0-9]+").all());

        //往pipeline传递值
        String nickName = page.getHtml().xpath("//*[@id=\"mainScreen\"]/div/div[1]/div/div[2]/div[1]/div[2]/div[2]/div[1]/a/span/text()").toString();
        String avatar = page.getHtml().xpath("//*[@id=\"mainScreen\"]/div/div[1]/div/div[2]/div[1]/div[2]/div[2]/div[1]/a/div/").css("img","src").toString();//正文
        if(nickName!=null && nickName.length()>0 && avatar !=null && avatar.length()>0){
            //头像和昵称
            // https://oscimg.oschina.net/oscnet/up-5bafb1c77d0dcbb69045b3318e98ae7c.jpg!/both/50x50
            String imageUrl = avatar.substring(0, avatar.indexOf("!")); // 图片地址
            page.putField("nickName",nickName);
            page.putField("avatar",imageUrl);
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
