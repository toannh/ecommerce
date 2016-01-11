/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.CategoryMapping;
import vn.chodientu.entity.enu.CategoryMappingType;

/**
 *
 * @author CANH
 */
@Repository
public class CategoryMappingRespository extends BaseRepository<CategoryMapping> {

    public CategoryMappingRespository() {
        super(CategoryMapping.class);
    }

    public CategoryMapping findOne() {
        Criteria cri = new Criteria("active").is(true).and("running").is(CategoryMappingType.EVERRUN);
        return getMongo().findOne(new Query(cri).limit(1).with(new Sort(Sort.Direction.ASC, "createTime")), getEntityClass());
    }

    public CategoryMapping findByOrigCate(String orCateId) {
        Criteria cri = new Criteria("originCategoryId").is(orCateId).and("running").is(CategoryMappingType.EVERRUN).and("active").is(true);
        return getMongo().findOne(new Query(cri), getEntityClass());
    }
}
