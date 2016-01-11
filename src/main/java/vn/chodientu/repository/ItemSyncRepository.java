package vn.chodientu.repository;


import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ItemSync;

@Repository
public class ItemSyncRepository extends BaseRepository<ItemSync> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ItemSyncRepository() {
        super(ItemSync.class);
    }

    @Async
    public void bulkSave(List<ItemSync> items) {
        for (ItemSync item : items) {
            save(item);
        }
    }
    public ItemSync getForSync() {
        Criteria cri = new Criteria("sync").is(true);
        return getMongo().findAndModify(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")), new Update().set("sync", false), getEntityClass());
    }
}
