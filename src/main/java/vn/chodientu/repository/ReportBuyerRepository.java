/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.report.ReportBuyer;

/**
 *
 * @author CANH
 */
@Repository
public class ReportBuyerRepository extends BaseRepository<ReportBuyer>{

    public ReportBuyerRepository() {
        super(ReportBuyer.class);
    }
    public ReportBuyer find(long time) {
        return getMongo().findOne(new Query(new Criteria("time").is(time)), getEntityClass());
    }
    
}
