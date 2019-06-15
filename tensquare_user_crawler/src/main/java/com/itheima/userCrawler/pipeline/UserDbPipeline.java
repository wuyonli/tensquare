package com.itheima.userCrawler.pipeline;

import com.itheima.userCrawler.dao.UserDao;
import com.itheima.userCrawler.pojo.User;
import com.itheima.userCrawler.utils.DownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.IdWorker;

import java.io.IOException;

/**
 * 入库类
 */
@Component
public class UserDbPipeline implements Pipeline {

    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserDao userDao;

    @Value("${avatar.savePath}")
    private String savePath;


    @Override
    public void process(ResultItems r, Task task) {
        String avatar = r.get("avatar");
        String nickname = r.get("nickName");
        System.out.println(avatar);
        System.out.println(nickname);
        //文件名称
        // https://oscimg.oschina.net/oscnet/up-5bafb1c77d0dcbb69045b3318e98ae7c.jpg
        String fileName = avatar.substring(avatar.lastIndexOf("/") + 1);//文件名称
        //插入用户
        try {
            User u = new User();
            u.setId(idWorker.nextId() + "");
            u.setNickname(nickname);
            u.setAvatar(fileName);
            userDao.save(u);

            //把图片下载到本地
            DownloadUtil.download(avatar, fileName, savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
