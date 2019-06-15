package com.tensquare.friend.dao;


import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface NOFriendDao extends JpaRepository<NoFriend,String> , JpaSpecificationExecutor<NoFriend> {

}
