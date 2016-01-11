package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.LandingNewItem;

/**
 *
 * @author phuongdt
 */
@Repository
public class LandingNewItemRepository extends BaseRepository<LandingNewItem> {

    public LandingNewItemRepository() {
        super(LandingNewItem.class);
    }
 public List<LandingNewItem> getAll(Pageable pageable) {
        return getMongo().find(new Query().with(pageable), getEntityClass());
    }
    public List<LandingNewItem> getByLandingNewId(String landingNewId, Pageable page) {
        Criteria cri = new Criteria("landingNewId").in(landingNewId);
        Query query = new Query(cri);
        if (page != null && (page.getPageNumber() > 0 || page.getPageSize() > 0)) {
            query.with(page);
        }
        return getMongo().find(query.with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public void removeByCategory(String cateId) {
        Criteria cri = new Criteria("landingCategoryId").is(cateId);
        getMongo().remove(new Query(cri), getEntityClass());
    }

    public LandingNewItem getByItem(String itemId) {
        return getMongo().findOne(new Query(new Criteria("itemId").is(itemId)), getEntityClass());
    }

    public LandingNewItem getByItem(String itemId, String landingItem) {
        Query query = new Query(new Criteria("itemId").is(itemId).and("landingNewId").is(landingItem));
        return getMongo().findOne(query, getEntityClass());
    }

}
