/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.db.SellerCrawlLog;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.CrawlType;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.input.ItemCrawlLogSearch;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.SellerCrawlLogSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.ItemCrawlLogRepository;
import vn.chodientu.repository.SellerCrawlLogRepository;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author CANH
 */
@Service
public class SellerCrawlLogService {

    @Autowired
    private SellerCrawlLogRepository sellerCrawlLogRepository;
    @Autowired
    private ItemCrawlLogRepository itemCrawlLogRepository;
    @Autowired
    private UserService sellerService;
    @Autowired
    private ItemCrawlLogService itemCrawlLogService;
    @Autowired
    private ItemService itemService;

//    
//    @Scheduled(fixedDelay = 5 * 60 * 1000L)
    @Scheduled(cron = "0 0 20 * * ?")
    public void run() {
        long currTime = System.currentTimeMillis();
        List<String> listSellerIds = itemCrawlLogRepository.listSellerUniq(TextUtils.getTime(currTime, false), TextUtils.getTime(currTime, true));
        ItemCrawlLogSearch itemcrllgSearch = new ItemCrawlLogSearch();
        for (String sellerId : listSellerIds) {
            List<Item> listItems = new ArrayList<>();
            //
            SellerCrawlLog sellerLog = sellerCrawlLogRepository.findBySeller(sellerId, currTime);
            if (sellerLog == null) {
                sellerLog = new SellerCrawlLog();
            }
            sellerLog.setSellerId(sellerId);
            sellerLog.setTime(currTime);
            //
            long totalRequest = 0;
            long successRequest = 0;
            long errorRequest = 0;
            long addRequest = 0;
            long editRequest = 0;
            long noCondition = 0;
            long enoughCondition = 0;

            //
            itemcrllgSearch.setPageSize(500);
            itemcrllgSearch.setPageIndex(0);
            itemcrllgSearch.setSellerId(sellerId);
            itemcrllgSearch.setTimeFrom(TextUtils.getTime(currTime, false));
            itemcrllgSearch.setTimeTo(TextUtils.getTime(currTime, true));
            itemcrllgSearch.setStatus(-1);
            DataPage<ItemCrawlLog> itemcrllogPage = itemCrawlLogService.search(itemcrllgSearch);
            totalRequest = itemcrllogPage.getData().size();
            while (itemcrllogPage.getPageIndex() < itemcrllogPage.getPageCount() + 1) {
                List<ItemCrawlLog> listlog = itemcrllogPage.getData();
                totalRequest = 1000;
                for (ItemCrawlLog log : listlog) {
                    if (log.isStatus()) {
                        successRequest += 1;
                    } else {
                        errorRequest += 1;
                    }
                    if (log.getType() != null) {
                        if (log.getType().equals(CrawlType.ADD)) {
                            addRequest += 1;
                        } else {
                            editRequest += 1;
                        }
                    }
                    if (log.getItem() != null) {
                        if (!itemService.validateSubmit(log.getItem())) {
                            noCondition += 1;
                        } else {
                            enoughCondition += 1;
                        }
                    }
                }
                itemcrllgSearch.setPageIndex(itemcrllogPage.getPageIndex() + 1);
                itemcrllogPage = itemCrawlLogService.search(itemcrllgSearch);
            }
            sellerLog.setTotalRequest(itemcrllogPage.getDataCount());
            sellerLog.setSuccessRequest(successRequest);
            sellerLog.setErrorRequest(errorRequest);
            sellerLog.setAddRequest(addRequest);
            sellerLog.setEditRequest(editRequest);
            sellerLog.setNoCondition(noCondition);
            sellerLog.setEnoughCondition(enoughCondition);
            Map<String, Long> countResult = countItemEnoughCondition(sellerId);
            if (countResult == null) {
                sellerLog.setItemOK(-1);
                sellerLog.setItemMiss(-1);
            } else {
                sellerLog.setItemOK(countResult.get("countOK"));
                sellerLog.setItemMiss(countResult.get("countMiss"));
            }
            sellerCrawlLogRepository.save(sellerLog);
        }
    }

    public Map<String, Long> countItemEnoughCondition(String sellerId) {
        long countOK = 0;
        long countMiss = 0;
        if (sellerId == null || sellerId.equals("")) {
            return null;
        }
        ItemSearch search = new ItemSearch();
        search.setSellerId(sellerId);
        search.setPageIndex(0);
        search.setPageSize(500);
        search.setSource(ItemSource.CRAWL);
        DataPage<Item> itemPage = itemService.searchMongo(search);
        while (itemPage.getPageIndex() < itemPage.getPageCount() + 1) {
            List<Item> listtItem = itemPage.getData();
            for (Item item : listtItem) {
                if (itemService.validateSubmit(item)) {
                    countOK += 1;
                } else {
                    countMiss += 1;
                }
            }
            search.setPageIndex(itemPage.getPageIndex() + 1);
            itemPage = itemService.searchMongo(search);
        }
        Map<String, Long> result = new HashMap<String, Long>();
        result.put("countOK", countOK);
        result.put("countMiss", countMiss);
        return result;
    }

    public DataPage<SellerCrawlLog> search(SellerCrawlLogSearch logSearch) {
        String sellerId = logSearch.getSellerId();
        long timeFrom = logSearch.getTimeFrom();
        long timeTo = logSearch.getTimeTo();
        int pageIndex = logSearch.getPageIndex();
        int pageSize = logSearch.getPageSize();

        Criteria cri = new Criteria();
        if (sellerId != null && !sellerId.equals("")) {
            cri.and("sellerId").is(sellerId);
        }
        if (timeFrom > 0 && timeTo > 0) {
            cri.and("time").gte(timeFrom).lt(timeTo);
        } else if (timeFrom > 0) {
            cri.and("time").gte(timeFrom);
        } else if (timeTo > 0) {
            cri.and("time").lt(timeTo);
        }
        if (pageIndex <= 0) {
            pageIndex = 0;
        }
        if (pageSize <= 0) {
            pageSize = 50;
        }
        Query query = new Query(cri);
        query.skip(pageIndex * pageSize).limit(pageSize);
        query.with(new Sort(Sort.Direction.DESC, "time"));
        //
        DataPage<SellerCrawlLog> dataPage = new DataPage<>();
        dataPage.setData(sellerCrawlLogRepository.find(query));
        dataPage.setPageIndex(pageIndex);
        dataPage.setPageSize(pageSize);
        dataPage.setDataCount(sellerCrawlLogRepository.count(query));
        dataPage.setPageCount(dataPage.getDataCount() / pageSize);
        try {
            List<String> listSellerIds = new ArrayList<>();
            for (SellerCrawlLog crawlLog : dataPage.getData()) {
                if (crawlLog.getSellerId() != null && !crawlLog.getSellerId().equals("")) {
                    listSellerIds.add(crawlLog.getSellerId());
                }
            }
            List<User> listSeller = sellerService.getUserByIds(listSellerIds);
            for (SellerCrawlLog crawlLog : dataPage.getData()) {
                for (User seller : listSeller) {
                    if (seller.getId().equals(crawlLog.getSellerId())) {
                        crawlLog.setSeller(seller);
                    }
                }
            }
        } catch (Exception ex) {
            return dataPage;
        }
        return dataPage;

    }

}
