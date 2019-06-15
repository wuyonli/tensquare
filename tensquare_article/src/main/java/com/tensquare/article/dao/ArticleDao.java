package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
    // 修改表的时候必须要写   @Modifying 注解
    @Modifying
    @Query(value = "update tb_article set state = 1 where id = ?1",nativeQuery = true)
    public void examine(String articleid);

    // 文章点赞    COALESCE(thumbup,0)  如果thumbup 为null  返回 0 否则返回 thumbup
    @Modifying
    @Query(value = "update tb_article set thumbup = COALESCE(thumbup,0) +1 where id = ?1",nativeQuery = true)
    void thumbup(String articleid);
}
