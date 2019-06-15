package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> , JpaSpecificationExecutor<Friend> {
// 查看 曾经添加好友
    @Query(value = "select count(*) from tb_friend where userid= ?1 and friendid= ?2",nativeQuery = true)
    public int selectCount(String userid, String friendid);

    // 删除好友
    @Modifying
    @Query("delete from Friend f where f.userid=?1 and f.friendid = ?2")
    public void deleteFriend(String userid, String friendid);

    //把对方的islike改为1
    @Modifying
    @Query("update Friend f set f.islike = ?3 where f.friendid = ?1 and f.userid=?2")
    public void updateIsLike(String friendid, String userid, String s);
}
