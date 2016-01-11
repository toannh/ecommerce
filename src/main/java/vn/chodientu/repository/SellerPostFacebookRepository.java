/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.SellerPostFacebook;
import vn.chodientu.entity.enu.SellerPostStatus;

/**
 *
 * @author Anh
 */
@Repository
public class SellerPostFacebookRepository extends BaseRepository<SellerPostFacebook> {

    public SellerPostFacebookRepository() {
        super(SellerPostFacebook.class);
    }
    public List<SellerPostFacebook> list(String sellerId) {
        Criteria criteria = new Criteria();
        criteria.and("sellerId").is(sellerId);
        return getMongo().find(new Query(criteria).with(new Sort(Sort.Direction.DESC, "createTime")).limit(100), getEntityClass());
    }
    public SellerPostFacebook getForCheck() {
        return getMongo().findOne(new Query(new Criteria("status").is(SellerPostStatus.NEW).and("createTime").lte(System.currentTimeMillis() - (5 * 60 * 1000))).with(new Sort(Sort.Direction.DESC, "createTime")), getEntityClass());
    }
}
