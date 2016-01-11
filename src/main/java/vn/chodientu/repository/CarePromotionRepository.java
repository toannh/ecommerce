package vn.chodientu.repository;

import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.CarePromotion;

/**
 *
 * @author Phu
 */
@Repository
public class CarePromotionRepository extends BaseRepository<CarePromotion> {

    public CarePromotionRepository() {
        super(CarePromotion.class);
    }
    
     

   
}
