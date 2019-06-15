package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{


    //  查询最新问答
    @Query(value = "select * from `tb_problem` , `tb_pl` where id = problemid and labelid = ?1 order by replytime desc",nativeQuery = true)
    // 注意: newlist(String labelid, Pageable pageRequest);  Pageable位置  必须是接口 不能是  实现类
    Page<Problem> newlist(String labelid, Pageable pageRequest);

    // 查询最热门问答
    @Query(value = "select * from `tb_problem` , `tb_pl` where id = problemid and labelid = ?1 order by reply desc",nativeQuery = true)
    Page<Problem> hotlist(String labelid, Pageable pageRequest);

    // 查询等待问答
    @Query(value = "select * from `tb_problem` , `tb_pl` where id = problemid and labelid = ?1 and reply= 0 order by replytime desc ",nativeQuery = true)
    Page<Problem> waitlist(String labelid, Pageable pageRequest);

}
