package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ItemMarketing;
import vn.chodientu.entity.output.DataPage;

/**
 *
 * @author Anhpp
 */
@Repository
public class ItemMarketingRepository extends BaseRepository<ItemMarketing> {

    public ItemMarketingRepository() {
        super(ItemMarketing.class);
    }

    public List<ItemMarketing> getAll(Pageable pageable) {
        return getMongo().find(new Query().with(pageable), getEntityClass());
    }
    public List<ItemMarketing> getAll() {
        return getMongo().find(new Query(), getEntityClass());
    }
    public List<ItemMarketing> search(int pageIndex,int pageSize) {
        Query query=new Query();
        List<ItemMarketing> list = find(query.skip(pageIndex * pageSize).limit(pageSize).with(new Sort(Sort.Direction.DESC,"id")));
        return list;
    }
}
