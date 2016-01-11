package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.BigLanding;

/**
 *
 * @author Anhpp
 */
@Repository
public class BigLandingRepository extends BaseRepository<BigLanding> {

    public BigLandingRepository() {
        super(BigLanding.class);
    }

    public List<BigLanding> getAll(Pageable pageable) {
        return getMongo().find(new Query().with(pageable), getEntityClass());
    }

    public BigLanding getCurrent(String id) {
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);
        criteria.and("active").is(true);
        criteria.and("endTime").gte(System.currentTimeMillis());
        return getMongo().findOne(new Query(criteria), getEntityClass());
    }

    public BigLanding getCheckExistCurrent() {
        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("startTime").lte(System.currentTimeMillis()).and("endTime").gte(System.currentTimeMillis());
        return getMongo().findOne(new Query(criteria), getEntityClass());
    }

    public BigLanding getCheckExistCurrentSeller() {
        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("startTimeSeller").lte(System.currentTimeMillis()).and("endTimeSeller").gte(System.currentTimeMillis());
        return getMongo().findOne(new Query(criteria), getEntityClass());
    }

    public BigLanding getCheckShowMessage() {
        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("endTime").gte(System.currentTimeMillis());
        return getMongo().findOne(new Query(criteria), getEntityClass());
    }
}
