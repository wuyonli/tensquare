package com.tensquare.spit.service;

import com.mongodb.client.result.UpdateResult;
import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.dao.SpitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    // findAll
    public List<Spit> findAll(){
        return spitDao.findAll();
    }
    // findOne
    public Spit findOne(String id){
        return spitDao.findById(id).get();
    }

    // add
    public void add(Spit spit){  //  不需要有id
        // 判断parentId是否存在
        if (spit.getParentid() != null && !spit.getParentid().equals("")){
            // 存在就给parentid对应的吐槽  添加 comment数
            // 创建添加
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            // 创建更新
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "tb_spit");
        }
        // 设置id
        spit.setId(idWorker.nextId()+"");
        spitDao.save(spit);
    }
    //update
    public void update(Spit spit){  // spit必须带有数据库存在的id
        spitDao.save(spit);
    }
    // delete
    public void delete(String id){
       spitDao.deleteById(id);
    }
// 根据parentid分页查询
    public Page<Spit> comment(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return spitDao.findByParentid(parentid,pageable);
    }

    // 吐槽点赞
    public void thumbup(String id) {
       // 新建query
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        // 新增要更新的值
        Update update = new Update();
        update.inc("thumbup",1);  // $inc  increasement
        // 执行
        mongoTemplate.updateFirst(query,update,"tb_spit");
    }
}
