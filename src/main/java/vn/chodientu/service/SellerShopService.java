/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.SellerShop;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.input.ItemSearch;
import vn.chodientu.entity.input.SellerShopSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.SellerShopRepository;
import vn.chodientu.repository.UserRepository;

/**
 *
 * @author CANH
 */
@Service
public class SellerShopService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerShopRepository sellerShopRepository;

    public void listSellerID() throws IOException, Exception {
        SellerShop checkOK = sellerShopRepository.find("OK");
        if (checkOK != null) {
            if (checkOK.isActive()) {
                return;
            }
        }
        long startTime = 1262278800000L;
        long endTime = startTime + 2629743000L;
        ItemSearch itemSearch = new ItemSearch();
        itemSearch.setPageIndex(0);
        itemSearch.setPageSize(100);
        itemSearch.setCreateTimeFrom(startTime);
        itemSearch.setCreateTimeTo(endTime);
        itemSearch.setSource(ItemSource.SELLER);
        DataPage<Item> itemPage = itemService.searchMongo(itemSearch);
        List<String> listSellerID = new ArrayList<>();
        //itemPage.getPageIndex() < itemPage.getPageCount()
        while (itemSearch.getCreateTimeFrom() < System.currentTimeMillis()) {
            List<Item> listItem = itemPage.getData();
            for (Item item : listItem) {
                if (item.getSellerId() != null && !item.getSellerId().equals("")) {
                    if (!listSellerID.contains(item.getSellerId())) {
                        listSellerID.add(item.getSellerId());
                        SellerShop sellerShop = new SellerShop();
                        if (item.getSellerId() != null) {
                            sellerShop.setUserId(item.getSellerId());
                        } else {
                            sellerShop.setUserId("null");
                        }
                        User user = userRepository.find(item.getSellerId());
                        if (user != null) {
                            sellerShop.setActive(user.isActive());
                            sellerShop.setEmail(user.getEmail());
                            sellerShop.setPhone(user.getPhone());
                            sellerShop.setUsername(user.getUsername());
                            sellerShop.setName(user.getName());
                        }
                        Shop shop = shopService.getShop(item.getSellerId());
                        if (shop != null) {
                            sellerShop.setShopId(shop.getUserId());
                            if (shop.getEmail() != null && !shop.getEmail().equals("")) {
                                sellerShop.setEmail(shop.getEmail());
                            }
                            if (shop.getPhone() != null && !shop.getPhone().equals("")) {
                                sellerShop.setPhone(shop.getPhone());
                            }
                            sellerShop.setShopAlias(shop.getAlias());
                        } else {
                            sellerShop.setShopId("");
                            sellerShop.setShopAlias("");
                        }
                        sellerShopRepository.save(sellerShop);
                    }
                }
            }
            itemSearch.setPageIndex(itemSearch.getPageIndex() + 1);
            if (listItem.size() < itemSearch.getPageSize()) {
                itemSearch.setPageIndex(0);
                itemSearch.setCreateTimeFrom(itemSearch.getCreateTimeTo());
                itemSearch.setCreateTimeTo(itemSearch.getCreateTimeFrom() + 2629743000L);
            }
            itemPage = itemService.searchMongo(itemSearch);
        }
        SellerShop sellerShop = new SellerShop();
        sellerShop.setUserId("OK");
        sellerShop.setActive(true);
        sellerShopRepository.save(sellerShop);
    }

    public DataPage<SellerShop> search(SellerShopSearch search) {
        Criteria cri = new Criteria();
        if (search.isActive()) {
            cri.and("active").is(true);
        }
        if (!search.isActive()) {
            cri.and("active").is(false);
        }
        if (search.getUserId() != null && !search.getUserId().equals("")) {
            cri.and("userId").is(search.getUserId());
        }
        if (search.getEmail() != null && !search.getEmail().equals("")) {
            cri.and("email").regex(search.getEmail());
        }
        if (search.getPhone() != null && !search.getPhone().equals("")) {
            cri.and("phone").is(search.getPhone());
        }
        if (search.getUsername() != null && !search.getUsername().equals("")) {
            cri.and("username").regex(search.getUsername());
        }
        if (search.getName() != null && !search.getName().equals("")) {
            cri.and("name").regex(search.getName());
        }
        if (search.getShopId() != null && !search.getShopId().equals("")) {
            cri.and("shopId").is(search.getShopId());
        }
        if (search.getShopAlias() != null && !search.getShopAlias().equals("")) {
            cri.and("shopAlias").regex(search.getShopAlias());
        }
        Query query = new Query(cri).skip(search.getPageIndex() * search.getPageSize()).limit(search.getPageSize());
        List<SellerShop> resultList = sellerShopRepository.find(query);
        long total = sellerShopRepository.count();
        DataPage<SellerShop> dataPage = new DataPage<>();
        dataPage.setData(resultList);
        dataPage.setDataCount(sellerShopRepository.count());
        dataPage.setPageCount(total % search.getPageSize() == 0 ? total / search.getPageSize() : total / search.getPageSize() + 1);
        dataPage.setPageIndex(search.getPageIndex());
        dataPage.setPageSize(search.getPageSize());
        return dataPage;
    }
}
