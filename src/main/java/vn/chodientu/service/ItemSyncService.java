package vn.chodientu.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemSync;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ItemSyncRepository;

/**
 *
 * @author Anhpp
 */
@Service
public class ItemSyncService {

    @Autowired
    private ItemSyncRepository itemSyncRepository;
    @Autowired
    private ItemService itemService;
    
    //@Scheduled(fixedDelay = 30000)
    public void SyncItem() {
        while (true) {
            ItemSync itemSync = itemSyncRepository.getForSync();
            if (itemSync == null) {
                break;
            }
            sellerItemSync(itemSync);
        }
    }
    /**
     * Đồng bộ sản phẩm cho seller
     * 
     * @param itemSync
     */
    @Async
    public void sellerItemSync(ItemSync itemSync){
        String userId=itemSync.getSellerId();
        boolean cod=itemSync.isScIntegrated();
        boolean onlinePayment=itemSync.isNlIntegrated();
        long total = itemService.countBySeller(userId);
        int totalPage = (int) total / 100;
        if (total % 100 != 0) {
            totalPage++;
        }
        for (int i = 0; i <= totalPage; i++) {
            List<Item> items = itemService.getBySeller(new PageRequest(i, 100), userId);
            if (items != null && items.size() > 0) {
                for (Item item : items) {
                    switch(itemSync.getSyncType()){
                        case SELLER_SYNC_ITEM:
                            item.setCod(cod);
                            item.setOnlinePayment(onlinePayment);
                            break;
                        case SELLER_SYNC_COD_ITEM:
                            item.setCod(cod);
                            break;
                        case SELLER_SYNC_ONLINE_ITEM:
                            item.setOnlinePayment(onlinePayment);
                            break;
                        case SELLER_SYNC_FEE_ITEM:
                            item.setShipmentType(itemSync.getShipmentType());
                            item.setShipmentPrice(itemSync.getShipmentPrice());
                    }
                    
                    try {
                        itemService.edit(item, null);
                    } catch (Exception ex) {
                        Logger.getLogger(ItemSyncService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    public Response add(ItemSync itemSync) {
        if (itemSync.getId() == null || itemSync.getId().equals("")) {
            itemSync.setId(itemSyncRepository.genId());
            itemSync.setTime(System.currentTimeMillis());
        }
        itemSync.setSync(true);
        itemSyncRepository.save(itemSync);
        return new Response(true, "Thêm mới thành công", itemSync);
    }
}
