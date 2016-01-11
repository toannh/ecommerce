package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.BigLandingSeller;

/**
 *
 * @author PhuongDt
 */
@Repository
public class BigLandingSellerRepository extends BaseRepository<BigLandingSeller> {
    
    public BigLandingSellerRepository() {
        super(BigLandingSeller.class);
    }
    
    public List<BigLandingSeller> list(int status, String biglandingId) {
        Criteria criteria = new Criteria();
        if (status > 0) {
            criteria.and("active").is(true);
        }
        criteria.and("biglandingId").is(biglandingId);
        return getMongo().find(new Query(criteria).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public BigLandingSeller getBigLandingByPromotionId(String promotionId) {
        return getMongo().findOne(new Query(new Criteria("promotionId").is(promotionId)), getEntityClass());
    }
    
}
