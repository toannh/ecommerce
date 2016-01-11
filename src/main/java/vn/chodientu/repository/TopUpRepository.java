package vn.chodientu.repository;

import com.mongodb.DBObject;
import static java.lang.Long.parseLong;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.TopUp;

@Repository
public class TopUpRepository extends BaseRepository<TopUp> {

    public TopUpRepository() {
        super(TopUp.class);
    }

    public long totalAmount(Criteria criteria) {
        Aggregation aggregations = Aggregation.newAggregation(
                match(criteria),
                group("null").sum("amount").as("amount"),
                project("amount")
        );
        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregations, "topUp", DBObject.class);
        List<DBObject> fieldList = results.getMappedResults();
        long amount = 0;
        if (fieldList != null && !fieldList.isEmpty()) {
            for (DBObject db : fieldList) {
                amount = parseLong(db.get("amount").toString());
            }
        }
        return amount;
    }

    /**
     * *
     * Lấy 1 giao dịch xèng mới nhất
     *
     * @param userId
     * @return
     */
    public TopUp getOneNew(String userId) {
        Criteria criteria = new Criteria();
        criteria.and("userId").is(userId);
        TopUp findOne = getMongo().findOne(new Query(criteria).with(new Sort(Sort.Direction.DESC, "createTime")), getEntityClass());
        return findOne;

    }

}
