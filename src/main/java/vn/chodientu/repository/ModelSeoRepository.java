package vn.chodientu.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ModelSeo;

/**
 * @since May 14, 2014
 * @author Phu
 */
@Repository
public class ModelSeoRepository extends BaseRepository<ModelSeo> {

    public ModelSeoRepository() {
        super(ModelSeo.class);
    }

    public void remove(String modelId) {
        getMongo().remove(new Query(new Criteria("modelId").is(modelId)), getEntityClass());
    }
    public ModelSeo get(String modelId) {
        return getMongo().findOne(new Query(new Criteria("modelId").is(modelId)), getEntityClass());
    }

}
