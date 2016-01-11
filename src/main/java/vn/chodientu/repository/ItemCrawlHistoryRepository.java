/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.repository;

import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.ItemCrawlHistory;

/**
 *
 * @author CANH
 */
@Repository
public class ItemCrawlHistoryRepository extends BaseRepository<ItemCrawlHistory>{

    public ItemCrawlHistoryRepository() {
        super(ItemCrawlHistory.class);
    }
    
}
