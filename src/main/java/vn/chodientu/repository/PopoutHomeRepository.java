package vn.chodientu.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.PopoutHome;

/**
 * @since May 15, 2014
 * @author Phuongdt
 */
@Repository
public class PopoutHomeRepository extends BaseRepository<PopoutHome> {

    public PopoutHomeRepository() {
        super(PopoutHome.class);
    }

    public List<PopoutHome> getAll(Pageable pageable) {
        return getMongo().find(new Query().with(pageable).with(new Sort(Sort.Direction.DESC, "time")), getEntityClass());
    }

}
