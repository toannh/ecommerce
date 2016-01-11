/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.PromotionCare;

/**
 *
 * @author Phuongdt
 */
@Repository
public class PromotionCareRepository extends BaseRepository<PromotionCare> {

    public PromotionCareRepository() {
        super(PromotionCare.class);
    }
    

    
}
