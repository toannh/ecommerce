package vn.chodientu.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.SellerReview;
import vn.chodientu.entity.enu.ReviewType;

@Repository
public class SellerReviewRepository extends BaseRepository<SellerReview> {

    public SellerReviewRepository() {
        super(SellerReview.class);
    }

    /**
     * Đếm point
     *
     * @param sellerId
     * @param point
     * @return
     */
    public long totalPoint(String sellerId, int point) {
        point = point > 2 ? 2 : point;
        point = point < 0 ? 0 : point;
        return getMongo().count(new Query(new Criteria("sellerId").is(sellerId).and("point").is(point)), getEntityClass());
    }

    public long totalReview(String sellerId) {
        return getMongo().count(new Query(new Criteria("sellerId").is(sellerId)), getEntityClass());
    }

    public SellerReview find(String orderId, String userId) {
        Criteria cri = new Criteria();
        cri.and("orderId").is(orderId);
        cri.and("userId").is(userId);
        return getMongo().findOne(new Query(cri), getEntityClass());
    }

    public SellerReview getByOrderId(String orderId) {
        Criteria criteria = new Criteria();
        criteria.and("orderId").is(orderId);
        SellerReview seller = getMongo().findOne(new Query(criteria), getEntityClass());
        if (seller == null) {
            criteria = new Criteria();
            criteria.and("object").is(orderId);
            return getMongo().findOne(new Query(criteria), getEntityClass());
        } else {
            return seller;
        }
    }

    public long getCountSeller(String sellerId) {
        DBCollection myColl = getMongo().getCollection("sellerReview");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("sellerId", sellerId);
        gtQuery.put("emailAdmin", null);
        gtQuery.put("active", true);
        List<String> listSeller = myColl.distinct("userId", gtQuery);
        return listSeller.size();
    }

    public long getCountSellerBuy(String sellerId) {
        long count = getMongo().count(new Query(new Criteria("sellerId").is(sellerId).and("emailAdmin")
                .is(null).and("active").is(true).and("reviewType").is("BUY")), getEntityClass());
        return count;
    }

    public long getCountSellerNoReview(String sellerId) {
        long count = getMongo().count(new Query(new Criteria("sellerId").is(sellerId).and("emailAdmin")
                .is(null).and("active").is(true).and("reviewType").is("NOREVIEW")), getEntityClass());
        return count;
    }

    public long getCountSellerDoNotBuy(String sellerId) {
        long count = getMongo().count(new Query(new Criteria("sellerId").is(sellerId).and("emailAdmin")
                .is(null).and("active").is(true).and("reviewType").is("DONOTBUY")), getEntityClass());
        return count;
    }

    public List<SellerReview> getPointReputable(String sellerId) {
        List<SellerReview> listseller = getMongo().find(new Query(new Criteria("sellerId").
                is(sellerId).and("active").is(false)), getEntityClass());
        return listseller;
    }

    public List<SellerReview> getPointProductQuality(String sellerId) {
        List<SellerReview> point = getMongo().find(new Query(new Criteria("sellerId").
                is(sellerId).and("emailAdmin").is(null).and("active").is(true)), getEntityClass());
        return point;
    }

    public List<SellerReview> getSellerReviewOrder(String orderId) {
        Criteria cri = new Criteria("orderId").is(orderId);
        Query query = new Query(cri).with(new Sort(Sort.Direction.DESC, "createTime"));
        return getMongo().find(query, getEntityClass());
    }

    public double getBySellerReviewId(String sellerId, ReviewType reviewType) {
        List<SellerReview> sellerReviews = getMongo().find(new Query(new Criteria("sellerId").
                is(sellerId).and("reviewType").is(reviewType.toString()).and("emailAdmin").is(null).and("active").is(true)), getEntityClass());
        double point = 0;
        for (SellerReview sellerReview : sellerReviews) {
            point += sellerReview.getProductQuality() + sellerReview.getInteractive() + sellerReview.getShippingCosts();
        }
        return point;
    }

    public double getPointSellerId(String sellerId) {
        List<SellerReview> sellerReviews = getMongo().find(new Query(new Criteria("sellerId").
                is(sellerId).and("emailAdmin").is(null).and("active").is(true)), getEntityClass());
        double point = 0;
        for (SellerReview sellerReview : sellerReviews) {
            point += (sellerReview.getProductQuality() + sellerReview.getInteractive() + 0 + sellerReview.getShippingCosts());
        }
        return Math.round(point / 3);
    }
}
