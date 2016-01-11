/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.SellerCrawlLog;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author CANH
 */
@Repository
public class SellerCrawlLogRepository extends BaseRepository<SellerCrawlLog> {

    public SellerCrawlLogRepository() {
        super(SellerCrawlLog.class);
    }

    public List<SellerCrawlLog> listByIds(List<String> ids) {
        Criteria cri = new Criteria("id").in(ids);
        return getMongo().find(new Query(cri), getEntityClass());
    }
    
    public SellerCrawlLog findBySeller(String sellerId, long time){
        Criteria cri = new Criteria("sellerId").is(sellerId);
        cri.and("time").lte(TextUtils.getTime(time, true)).gte(TextUtils.getTime(time, false));
        return getMongo().findOne(new Query(cri), getEntityClass());
    }

}
