package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.Ward;

/**
 * @since May 15, 2014
 * @author Phu
 */
@Repository
public class WardRepository extends BaseRepository<Ward> {

    public WardRepository() {
        super(Ward.class);
    }

    public List<Ward> getAll() {
        return getMongo().find(new Query(new Criteria()).with(new Sort(new Sort.Order(Sort.Direction.ASC, "position"))), getEntityClass());
    }

    public void deleteByDistrictId(String districtId) {
        getMongo().remove(new Query(new Criteria("cityId").is(districtId)), getEntityClass());
    }

    public List<Ward> getAllWardByDistrict(String districtId) {
        Criteria cri = new Criteria();
        cri.and("districtId").in(districtId);
        Query query = new Query(cri);
        return getMongo().find(query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "position"))), getEntityClass());
    }

    public String getWardNameById(String wardId) {
        Ward find = find(wardId);
        return (find == null) ? null : find.getName();
    }

    public Ward getBySc(String scId) {
        return getMongo().findOne(new Query(new Criteria("scId").is(scId)), getEntityClass());
    }

    public boolean getExistWard(String districtId) {
        Criteria cri = new Criteria();
        cri.and("districtId").in(districtId);
        Query query = new Query(cri);
        Ward find = getMongo().findOne(query, getEntityClass());
        boolean exists = false;
        if (find != null) {
            exists = true;
        }
        return exists;
    }
}
