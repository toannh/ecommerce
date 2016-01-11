package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ActiveKey;

/**
 * @since May 15, 2014
 * @author PAnh
 */
@Repository
public class ActiveKeyRepository extends BaseRepository<ActiveKey> {

    public ActiveKeyRepository() {
        super(ActiveKey.class);
    }

    public List<ActiveKey> getAll() {
        return getMongo().find(new Query(new Criteria()).with(new Sort(new Sort.Order(Sort.Direction.DESC, "time"))), getEntityClass());
    }

    public ActiveKey findByObject(String id,String type) {
        //ActiveKey find = find(wardId);
        return getMongo().findOne(new Query(new Criteria("id").is(id).and("type").is(type)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "time"))), getEntityClass());
    }
    public ActiveKey findByCode(String code,String type) {
        //ActiveKey find = find(wardId);
        return getMongo().findOne(new Query(new Criteria("code").is(code).and("type").is(type)).with(new Sort(new Sort.Order(Sort.Direction.DESC, "time"))), getEntityClass());
    }
    public boolean getExistActiveKey(String id,String type) {
        Criteria cri = new Criteria();
        cri.and("id").is(id).and("type").is(type);
        Query query = new Query(cri);
        ActiveKey find = getMongo().findOne(query, getEntityClass());
        boolean exists = false;
        if (find != null) {
            exists = true;
        }
        return exists;
    }
    public boolean checkActiveKey(String code,String type) {
        Criteria cri = new Criteria();
        cri.and("code").is(code).and("type").is(type);
        Query query = new Query(cri);
        ActiveKey find = getMongo().findOne(query, getEntityClass());
        boolean exists = false;
        if (find != null) {
            exists = true;
        }
        return exists;
    }
}
