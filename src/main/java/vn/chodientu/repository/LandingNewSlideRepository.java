package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.LandingNewSlide;

/**
 *
 * @author phuongdt
 */
@Repository
public class LandingNewSlideRepository extends BaseRepository<LandingNewSlide> {

    public LandingNewSlideRepository() {
        super(LandingNewSlide.class);
    }

    public List<LandingNewSlide> getAll(Pageable pageable, String landingNewId) {
        Criteria criteria = new Criteria("landingNewId").is(landingNewId);
        return getMongo().find(new Query(criteria).with(pageable).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }
}
