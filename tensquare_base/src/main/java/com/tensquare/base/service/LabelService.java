package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class LabelService {
    //导入dao
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;


    // 查询所有
    public List<Label> findAll(){
        return labelDao.findAll();
    }
    //查询一个
    public Label findById(String id){
        return labelDao.findById(id).get();
    }
    // 添加一个
    public void add(Label label){
        //设置雪花id
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    // 更新一个
    public void updata(Label label){
        labelDao.save(label);
    }
    /**
     * 删除一个
     */
    public void delete(String id){
        labelDao.deleteById(id);
    }

    // 多条件动态查询
    public List<Label> search(Map searchMap) {
        Specification<Label> specification = createSpecification(searchMap);
        List<Label> labelList = labelDao.findAll(specification);
        return labelList;
    }

    // 产生动态条件拼接对象
    private Specification<Label> createSpecification(Map map){
       return new Specification<Label>() {
           @Override
           public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
              List<Predicate> predicateList = new ArrayList<>();
              // id
               if (map.get("id") != null && !"".equals(map.get("id"))){
                  Expression<String> id = root.get("id").as(String.class);
                  Predicate id1 = cb.equal(id, map.get("id"));
                   predicateList.add(id1);
              }
            // labelname
               if (map.get("labelname") != null && !"".equals(map.get("labelname"))){
                   Expression<String> labelname = root.get("labelname").as(String.class);
                   Predicate id2 = cb.like(labelname, "%"+ map.get("labelname") + "%");
                   predicateList.add(id2);
               }

               // state
               if (map.get("state") != null && !"".equals(map.get("state"))){
                   Expression<String> state = root.get("state").as(String.class);
                   Predicate id3 = cb.equal(state, map.get("state"));
                   predicateList.add(id3);
               }

               // count
               if (map.get("count") != null && !"".equals(map.get("count"))){
                   Expression<Integer> count = root.get("count").as(int.class);
                   Predicate id4 = cb.equal(count, map.get("count"));
                   predicateList.add(id4);
               }
               // recommend
               if (map.get("recommend") != null && !"".equals(map.get("recommend"))){
                   Expression<String> recommend = root.get("recommend").as(String.class);
                   Predicate id5 = cb.equal(recommend, map.get("recommend"));
                   predicateList.add(id5);
               }

               Predicate[] predicates = new Predicate[predicateList.size()];
               // predicateList.toArray(predicates)  将 list中元素全部赋给 predicates  并返回该数组
                return cb.and(predicateList.toArray(predicates));
           }
       };
    }

    //  多条件分页查询
    public Page<Label> findSearch(int page, int size, Map searchMap) {
        //获取条件拼接对象
        Specification<Label> specification = createSpecification(searchMap);
        // 创建分页对象
        Pageable pageabele = PageRequest.of(page-1,size);
        return labelDao.findAll(specification,pageabele);
    }
}
