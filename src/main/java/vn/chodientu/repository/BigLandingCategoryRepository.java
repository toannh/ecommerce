package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.BigLandingCategory;

/**
 *
 * @author Anhpp
 */
@Repository
public class BigLandingCategoryRepository extends BaseRepository<BigLandingCategory> {

    public BigLandingCategoryRepository() {
        super(BigLandingCategory.class);
    }

    public List<BigLandingCategory> getByBigLanding(String bigLandingId) {
        Criteria cri = new Criteria("bigLandingId").is(bigLandingId).and("parentId").exists(false);
        return getMongo().find(new Query(cri).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public List<BigLandingCategory> getActiveByBigLanding(String bigLandingId) {
        Criteria cri = new Criteria("bigLandingId").is(bigLandingId).and("parentId").exists(false).and("active").is(true);
        return getMongo().find(new Query(cri).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public List<BigLandingCategory> getByBigParentCate(String cateId) {
        Criteria cri = new Criteria("parentId").is(cateId).and("active").is(true);
        return getMongo().find(new Query(cri).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public void deleteByBigLanding(String bigLandingId) {
        Criteria cri = new Criteria("bigLandingId").is(bigLandingId);
        getMongo().remove(new Query(cri), getEntityClass());
    }

    public BigLandingCategory get(String cateId) {
        Criteria cri = new Criteria("id").is(cateId);
        return getMongo().findOne(new Query(cri), getEntityClass());
    }
}
