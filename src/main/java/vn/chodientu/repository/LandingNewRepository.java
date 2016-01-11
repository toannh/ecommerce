package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.Landing;
import vn.chodientu.entity.db.LandingNew;

/**
 *
 * @author phuongdt
 */
@Repository
public class LandingNewRepository extends BaseRepository<LandingNew> {

    public LandingNewRepository() {
        super(LandingNew.class);
    }

    public List<LandingNew> getAll(Pageable pageable) {
        return getMongo().find(new Query().with(pageable), getEntityClass());
    }
}
