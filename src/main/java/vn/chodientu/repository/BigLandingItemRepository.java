package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.BigLandingItem;

/**
 *
 * @author Anhpp
 */
@Repository
public class BigLandingItemRepository extends BaseRepository<BigLandingItem> {

    public BigLandingItemRepository() {
        super(BigLandingItem.class);
    }

    public List<BigLandingItem> getByLandingCategory(List<String> cateId, Pageable page, int active) {
        Criteria cri = new Criteria("bigLandingCategoryId").in(cateId);
        if (active > 0) {
            cri.and("active").is(active == 1);
        }
        Query query = new Query(cri);
        if (page != null && (page.getPageNumber() > 0 || page.getPageSize() > 0)) {
            query.with(page);
        }
        query.with(new Sort(Sort.Direction.ASC, "position"));
        return getMongo().find(query, getEntityClass());
    }

    public List<BigLandingItem> getByCategories(List<String> cateIds, Pageable page, int special) {
        Criteria cri = new Criteria("bigLandingCategoryId").in(cateIds);
        Query query = new Query(cri);
        if (page != null && (page.getPageNumber() > 0 || page.getPageSize() > 0)) {
            query.with(page);
        }
        return getMongo().find(query, getEntityClass());
    }

    public List<BigLandingItem> getByCategories(List<String> cateIds) {
        Criteria cri = new Criteria("bigLandingCategoryId").in(cateIds);
        cri.and("active").is(true);
        Query query = new Query(cri);
        return getMongo().find(query, getEntityClass());
    }

    public List<BigLandingItem> getFeaturedByCategories(String cateIds) {
        Criteria cri = new Criteria("bigLandingCategoryId").is(cateIds).and("featured").is(true).and("active").is(true).and("position").gt(0);

        Query query = new Query(cri).with(new Sort(Sort.Direction.ASC, "position"));
        return getMongo().find(query, getEntityClass());
    }

    public List<BigLandingItem> getTopByCategories(String cId) {
        Criteria criteria = new Criteria();
        criteria.and("active").is(true).and("bigLandingCategoryId").is(cId);
        return getMongo().find(new Query(criteria).limit(4).with(new Sort(Sort.Direction.ASC, "position")), getEntityClass());
    }

    public long count(String cateId) {
        Criteria cri = new Criteria("bigLandingCategoryId").is(cateId).and("active").is(true);
        Query query = new Query(cri);
        return getMongo().count(query, getEntityClass());
    }

    public long count(List<String> cateId) {
        Criteria cri = new Criteria("bigLandingCategoryId").in(cateId).and("active").is(true);
        Query query = new Query(cri);
        return getMongo().count(query, getEntityClass());
    }

    public long countByCategory(List<String> cateId) {
        Criteria cri = new Criteria("bigLandingCategoryId").in(cateId);
        Query query = new Query(cri);
        return getMongo().count(query, getEntityClass());
    }

    public void removeByCategory(String cateId) {
        Criteria cri = new Criteria("bigLandingCategoryId").is(cateId);
        getMongo().remove(new Query(cri), getEntityClass());
    }

    public BigLandingItem getByItem(String itemId, String bigLandingCategoryId) {
        return getMongo().findOne(new Query(new Criteria("itemId").is(itemId).and("bigLandingCategoryId").is(bigLandingCategoryId)), getEntityClass());
    }

    public BigLandingItem getByItemBycustom(String bigLandingCateId, int position) {
        Criteria criteria = new Criteria("bigLandingCategoryId").is(bigLandingCateId);
        criteria.and("position").is(position);
        return getMongo().findOne(new Query(criteria), getEntityClass());
    }
}
