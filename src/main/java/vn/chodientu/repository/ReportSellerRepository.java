/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.report.ReportSeller;

/**
 *
 * @author CANH
 */
@Repository
public class ReportSellerRepository extends BaseRepository<ReportSeller> {

    public ReportSellerRepository() {
        super(ReportSeller.class);
    }

    public ReportSeller find(long time) {
        return getMongo().findOne(new Query(new Criteria("time").is(time)), getEntityClass());
    }
}
