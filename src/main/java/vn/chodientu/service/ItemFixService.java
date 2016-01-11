/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.enu.ItemHistoryType;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.ItemSearchRepository;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author CANH
 */
@Service
public class ItemFixService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SearchIndexService searchIndexService;
    @Autowired
    private ItemSearchRepository itemSearchRepository;
    @Autowired
    private ItemHistoryService itemHistoryService;

    public Item fixItem(Item item) {

        if (item != null) {
            if (item.getSellPrice() == 0) {
                item.setFixedPrice(false);
                syncItem(item);
            } else {
                String strPercent = TextUtils.percentFormat(item.getStartPrice(), item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                int percent = (int) Double.parseDouble(strPercent);
                if (percent > 50) {
                    if (!item.isDiscount() && item.getStartPrice() > item.getSellPrice()) {
                        long startprice = 2 * item.getSellPrice();
                        item.setStartPrice(startprice);
                    } else if (item.isDiscount()) {
                        if (item.getStartPrice() <= item.getSellPrice()) {
                            if (item.getDiscountPrice() > 0) {
                                long newSellPrice = 2 * (item.getSellPrice() - item.getDiscountPrice());
                                item.setSellPrice(newSellPrice);
                                item.setStartPrice(newSellPrice);
                                item.setDiscountPrice(newSellPrice / 2);
                            } else if (item.getDiscountPercent() > 0) {
                                long newSellPrice = item.getSellPrice() * (100 - item.getDiscountPercent()) / 50;
                                item.setSellPrice(newSellPrice);
                                item.setStartPrice(newSellPrice);
                                item.setDiscountPercent(50);
                            }
                        } else {
                            long startprice = calculateStartPrice(item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                            item.setStartPrice(startprice);
                            String newStrPercent = TextUtils.percentFormat(item.getStartPrice(), item.getSellPrice(), item.isDiscount(), item.getDiscountPrice(), item.getDiscountPercent());
                            int newPercent = (int) Double.parseDouble(newStrPercent);
                            if (newPercent > 50) {
                                // neu sau khi update startprice mà nó nhảy sang trường hợp giá startprice<sellprice thì chạy vào case update khác.
                                //qua case đó là ok.
                                fixItem(item);
                            }
                        }
                    }
                    item.setFixedPrice(true);
                } else {
                    item.setFixedPrice(false);
                }
            }
        }
        return item;
    }

    //@Scheduled(fixedDelay = 30000)
    public void deleteIndexItem() {
        while (true) {
            Item item = itemRepository.findForDelete();
            if (item == null) {
                break;
            }
            try {
                delItem(item);
            } catch (Exception e) {
            }
        }
    }

    @Async
    private void delItem(Item item) {
        try {
            itemSearchRepository.delete(item.getId());
        } catch (Exception e) {
        }
    }

    //@Scheduled(fixedDelay = 500)
    public void fixItem() {
        List<Item> listItems = itemRepository.getForFixPrice(500);
        if (listItems == null || listItems.isEmpty()) {
        }
        for (Item item : listItems) {
            item = fixItem(item);
            syncItem(item);
        }
    }

    @Async
    private void syncItem(Item item) {
        try {
            searchIndexService.processIndexItem(item);
            itemRepository.save(item);
        } catch (Exception ex) {
            itemRepository.save(item);
        }
    }

    public long calculateStartPrice(long sellprice, boolean discount, long discountPrice, int discountPercent) {
        if (discount == true) {
            if (discountPercent > 0) {
                return 2 * sellprice * (100 - discountPercent) / 100;
            } else if (discountPrice > 0) {
                return 2 * (sellprice - discountPrice);
            }
        } else {
            return 2 * sellprice;
        }
        return 0;
    }

    public void fixItem(String id) {
        List<Item> listItems = itemRepository.getForFixPriceByID(id);
        if (listItems == null || listItems.isEmpty()) {
            return;
        }
        for (Item item : listItems) {
            item = fixItem(item);
            syncItem(item);
        }
    }
}
