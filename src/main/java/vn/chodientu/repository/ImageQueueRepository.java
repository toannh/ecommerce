/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ImageQueue;

/**
 *
 * @author CANH
 */
@Repository
public class ImageQueueRepository extends BaseRepository<ImageQueue> {

    public ImageQueueRepository() {
        super(ImageQueue.class);
    }

    public ImageQueue getForDownload() {
        Criteria cri = new Criteria();
        cri.and("run").is(0).and("done").is(0).and("lock").is(0);
        return getMongo().findOne(new Query(cri), getEntityClass());
    }

    public ImageQueue findByItemAndUrl(String itemId, String url) {
        Criteria cri = new Criteria();
        cri.and("targetId").is(itemId).and("url").is(url).and("run").is(0);
        return getMongo().findOne(new Query(cri), getEntityClass());
    }

}
