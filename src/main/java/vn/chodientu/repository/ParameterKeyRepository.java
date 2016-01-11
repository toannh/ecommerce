package vn.chodientu.repository;
                                                                                                              
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ParameterKey;

/**
 * @since May 12, 2015
 * @author TaiND
 */
@Repository
public class ParameterKeyRepository extends BaseRepository<ParameterKey> {

    public ParameterKeyRepository() {
        super(ParameterKey.class);
    }

    public List<ParameterKey> getAll() {
        return getMongo().find(new Query(new Criteria()).with(new Sort(new Sort.Order(Sort.Direction.DESC, "key"))), getEntityClass());
    }
    
    public ParameterKey getParameterKeyById(String keyConvention) {
        return getMongo().findOne(new Query(new Criteria("keyConvention").is(keyConvention)), getEntityClass());
    }
}
