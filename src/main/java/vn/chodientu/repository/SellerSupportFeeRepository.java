package vn.chodientu.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.SellerSupportFee;
import vn.chodientu.entity.enu.FeeType;

@Repository
public class SellerSupportFeeRepository extends BaseRepository<SellerSupportFee> {

    public SellerSupportFeeRepository() {
        super(SellerSupportFee.class);
    }
    public SellerSupportFee getTopByOrderPrice(String sellerId,double price, FeeType feeType) {
        return getMongo().findOne(new Query(new Criteria("sellerId").is(sellerId).and("minOrderPrice").lte(price).and("active").is(true).and("type").is(feeType.toString())).with(new Sort(Sort.Direction.DESC, "minOrderPrice")), getEntityClass());
    }
}
