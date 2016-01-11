/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.data.KeyVal64;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.ItemCrawlLog;
import vn.chodientu.entity.enu.CrawlImageStatus;
import vn.chodientu.entity.enu.CrawlType;
import vn.chodientu.entity.input.ItemCrawlLogSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.ItemCrawlLogRepository;
import vn.chodientu.util.ErrorUtils;

/**
 *
 * @author CANH
 */
@Service
public class ItemCrawlLogService {

    @Autowired
    private ItemCrawlLogRepository crawlLogRepository;
    @Autowired
    private ItemService itemService;

    public DataPage<ItemCrawlLog> search(ItemCrawlLogSearch logSearch) {
        String itemId = logSearch.getItemId();
        String sellerId = logSearch.getSellerId();
        long timeFrom = logSearch.getTimeFrom();
        long timeTo = logSearch.getTimeTo();
        int status = logSearch.getStatus();
        String imgStatus = logSearch.getImageStatus();
        String type = logSearch.getType();
        int pageIndex = logSearch.getPageIndex();
        int pageSize = logSearch.getPageSize();

        Criteria cri = new Criteria();
        if (itemId != null && !itemId.equals("")) {
            cri.and("itemId").is(itemId);
        }
        if (sellerId != null && !sellerId.equals("")) {
            cri.and("sellerId").is(sellerId);
        }
        if (imgStatus != null && !imgStatus.equals("")) {
            switch (imgStatus) {
                case "NO_IMAGE":
                    cri.and("imageStatus").is(CrawlImageStatus.NO_IMAGE);
                    break;
                case "WAIT_DOWNLOAD":
                    cri.and("imageStatus").is(CrawlImageStatus.WAIT_DOWNLOAD);
                    break;
                case "DOWNLOAD_SUCCESS":
                    cri.and("imageStatus").is(CrawlImageStatus.DOWNLOAD_SUCCESS);
                    break;
                case "DOWNLOAD_FAIL":
                    cri.and("imageStatus").is(CrawlImageStatus.DOWNLOAD_FAIL);
                    break;
                case "SMALL_IMAGE":
                    cri.and("imageStatus").is(CrawlImageStatus.SMALL_IMAGE);
                    break;
                default:
                    break;
            }
        }
        if (type != null && !type.equals("")) {
            switch (type) {
                case "ADD":
                    cri.and("type").is(CrawlType.ADD);
                    break;
                case "EDIT":
                    cri.and("type").is(CrawlType.ADD);
                    break;
                default:
                    break;
            }
        }
        switch (status) {
            case 0: //everrun
                cri.and("status").is(false);
                break;
            case 1: //running
                cri.and("status").is(true);
                break;
            default:
                break;
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
        DataPage<ItemCrawlLog> dataPage = new DataPage<>();
        dataPage.setData(crawlLogRepository.find(query));
        dataPage.setPageIndex(pageIndex);
        dataPage.setPageSize(pageSize);
        dataPage.setDataCount(crawlLogRepository.count(query));
        dataPage.setPageCount(dataPage.getDataCount() / pageSize);
        try {
            List<String> listItemIds = new ArrayList<>();
            for (ItemCrawlLog crawlLog : dataPage.getData()) {
                if (crawlLog.getItemId() != null && !crawlLog.getItemId().equals("")) {
                    listItemIds.add(crawlLog.getItemId());
                }
                List<String> errMsg = new ArrayList<>();
                List<String> alertMsg = new ArrayList<>();
                for (String errcode : crawlLog.getErrorCode()) {
                    errMsg.add(ErrorUtils.getErrorMessage(errcode));
                }
                for (String alertCode : crawlLog.getAlertCode()) {
                    alertMsg.add(ErrorUtils.getAlertMessage(alertCode));
                }
                crawlLog.setErrorMessage(errMsg);
                crawlLog.setAlertMessage(alertMsg);
            }
            List<Item> listItems = itemService.list(listItemIds);
            for (ItemCrawlLog crawlLog : dataPage.getData()) {
                for (Item item : listItems) {
                    if (item.getId().equals(crawlLog.getItemId())) {
                        crawlLog.setItem(item);
                    }
                }
                for (KeyVal64 keyval : crawlLog.getRequest()) {
                    String value = StringUtils.newStringUtf8(Base64.decodeBase64(keyval.getValue()));
                    if (value != null && !value.equals("")) {
                        value = value.trim();
                    }
                    keyval.setValue(value);
                }
            }
        } catch (Exception ex) {
            return dataPage;
        }
        return dataPage;

    }

}
