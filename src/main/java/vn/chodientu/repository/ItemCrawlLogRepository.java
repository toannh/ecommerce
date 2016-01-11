/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author CANH
 */
@Repository
public class ItemCrawlLogRepository extends BaseRepository<ItemCrawlLog> {

    public ItemCrawlLogRepository() {
        super(ItemCrawlLog.class);
    }

    public ItemCrawlLog findByItemId(String itemId) {
        Criteria cri = new Criteria();
        cri.and("itemId").is(itemId);
        return getMongo().findOne(new Query(cri).limit(1), getEntityClass());
    }

    public ItemCrawlLog findByItemIdAndTime(String itemId, long time) {
        Criteria cri = new Criteria();
        cri.and("itemId").is(itemId).and("time").is(time);
        return getMongo().findOne(new Query(cri).limit(1), getEntityClass());
    }

    public List<String> listSellerUniq(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("itemCrawlLog");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("time", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        List listSellerUnique = myColl.distinct("sellerId", gtQuery);
        return listSellerUnique;
    }

    public ItemCrawlLog findBySourceLink(String sourceLink) {
        Criteria cri = new Criteria();
        cri.and("sourceLink").is(sourceLink);
        return getMongo().findOne(new Query(cri).limit(1).with(new Sort(Sort.Direction.DESC, "time")), getEntityClass());
    }

    public List<ItemCrawlLog> listByItem(String itemId) {
        Criteria cri = new Criteria();
        cri.and("itemId").is(itemId);
        return getMongo().find(new Query(cri).with(new Sort(Sort.Direction.DESC, "time")), getEntityClass());
    }

}
