package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NOFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NOFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    @Transactional
    public Long addFriend(String userid, String friendid) {
        // 判断你曾经是否添加过好友 ,如果加过 返回 0
        if (friendDao.selectCount(userid,friendid)>0){
            return 0L;
        }
        String flag = "0";

        // 判断对方是否加过你
        if (friendDao.selectCount(friendid,userid)>0){
            flag = "1";
            //把对方的islike改为1
            friendDao.updateIsLike(userid,friendid,"1");
        }


        Friend friend = new Friend();
        friend.setIslike(flag);
        friend.setUserid(userid);
        friend.setFriendid(friendid);


        friendDao.save(friend);
        // 添加好友 用户的关注 +1
        userClient.incFollowcount(userid,1);
        // 朋友的粉丝数 +1
        userClient.incFanscount(friendid,1);

        return 1L;
    }

    public void addNoFriend(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    // 删除好友

    @Transactional
    public void deleteFriend(String userid, String friendid) {
        friendDao.deleteFriend(userid,friendid);
        // 删除好友 用户的关注 -1
        userClient.incFollowcount(userid,-1);
        // 朋友的粉丝数 -1
        userClient.incFanscount(friendid,-1);

        //更新对方的islike为0
        if(friendDao.selectCount(friendid,userid)>0){
            friendDao.updateIsLike(friendid,userid,"0");
        }
    }
}
