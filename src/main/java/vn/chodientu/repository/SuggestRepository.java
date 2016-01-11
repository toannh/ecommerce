package vn.chodientu.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.Suggest;

@Repository
public class SuggestRepository extends BaseRepository<Suggest> {

    public SuggestRepository() {
        super(Suggest.class);
    }

    public Suggest find(String alias, String categoryId) {
        return getMongo().findOne(new Query(new Criteria("_id").is(alias + "-" + categoryId).and("categoryId").is(categoryId)), getEntityClass());
    }

}
