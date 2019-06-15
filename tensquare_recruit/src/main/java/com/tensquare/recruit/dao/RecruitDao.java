package com.tensquare.recruit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.recruit.pojo.Recruit;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{

    //查询前4条推荐职位  state = 2
    List<Recruit> findTop4ByStateOrderByCreatetimeDesc(String s);
    // 查询前12条新职位 state != 0
    List<Recruit> findTop12ByStateNotOrderByCreatetimeDesc(String s);
}
