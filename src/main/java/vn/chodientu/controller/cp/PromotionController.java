/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Promotion;
import vn.chodientu.entity.db.PromotionItem;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.input.PromotionSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.repository.OrderItemRepository;
import vn.chodientu.service.OrderService;
import vn.chodientu.service.PromotionService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;

/**
 *
 * @author PhuongDT
 */
@Controller("cpPromotion")
@RequestMapping("/cp/promotion")
public class PromotionController extends BaseCp {

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private Gson gson;

    @RequestMapping(method = RequestMethod.GET)
    public String homebanner(ModelMap model, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {
        PromotionSearch promotionSearch = new PromotionSearch();
        if (session.getAttribute("promotionSearch") != null && page != 0) {
            promotionSearch = (PromotionSearch) session.getAttribute("promotionSearch");
        } else {
            session.setAttribute("promotionSearch", promotionSearch);
        }
        promotionSearch.setPageIndex(page - 1);
        promotionSearch.setPageSize(50);
        DataPage<Promotion> dataPage = promotionService.search(promotionSearch);
        List<String> sellerIds = new ArrayList<>();
        for (Promotion promotion : dataPage.getData()) {
            int countOrder = 0;
            sellerIds.add(promotion.getSellerId());
            List<PromotionItem> promotionItemList = promotionService.getListPromotionItem(promotion);
            List<Order> orderList = orderService.searchPromotionOders(promotion.getStartTime(), promotion.getEndTime(), promotion.getSellerId());
            for (Order order : orderList) {
                List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
                for (PromotionItem proItem : promotionItemList) {
                    for (OrderItem item : orderItems) {
                        if (item.getItemId().equals(proItem.getItemId())) {
                            countOrder++;
                            break;
                        }
                    }
                }
            }
            promotion.setTotalTransacton(countOrder);
        }
        List<String> promotionIds = new ArrayList<>();
        if (sellerIds != null && !sellerIds.isEmpty()) {
            List<Shop> userByIds = shopService.getShops(sellerIds);
            for (Shop shop : userByIds) {
                User user = userService.get(shop.getUserId());
                for (Promotion promotion : dataPage.getData()) {
                    if (promotion.getSellerId().equals(shop.getUserId())) {
                        promotion.setSellerName((shop.getAlias() == null || shop.getAlias().equals("")) ? user.getEmail() : shop.getAlias());
                    }
                    promotionIds.add(promotion.getId());
                }
            }
        }

        model.put("dataPage", dataPage);
        model.put("promotionSearch", promotionSearch);
        model.put("clientScript", "var promotionIds = " + gson.toJson(promotionIds)
                + "; promotion.init();");
        return "cp.promotion";
    }

    @RequestMapping(value = {""}, method = RequestMethod.POST)
    public String search(ModelMap model,
            HttpSession session,
            @ModelAttribute PromotionSearch promotionSearch) throws Exception {
        session.setAttribute("promotionSearch", promotionSearch);
        promotionSearch.setPageIndex(0);
        promotionSearch.setPageSize(50);
        DataPage<Promotion> dataPage = promotionService.search(promotionSearch);
        List<String> sellerIds = new ArrayList<>();
        for (Promotion promotion : dataPage.getData()) {
            sellerIds.add(promotion.getSellerId());
            int countOrder = 0;
            sellerIds.add(promotion.getSellerId());
            List<PromotionItem> promotionItemList = promotionService.getListPromotionItem(promotion);
            List<Order> orderList = orderService.searchPromotionOders(promotion.getStartTime(), promotion.getEndTime(), promotion.getSellerId());
            for (Order order : orderList) {
                List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
                for (PromotionItem proItem : promotionItemList) {
                    for (OrderItem item : orderItems) {
                        if (item.getItemId().equals(proItem.getItemId())) {
                            countOrder++;
                            break;
                        }
                    }
                }
            }
            promotion.setTotalTransacton(countOrder);
        }
        List<String> promotionIds = new ArrayList<>();
        if (sellerIds != null && !sellerIds.isEmpty()) {
            List<Shop> userByIds = shopService.getShops(sellerIds);
            for (Shop shop : userByIds) {
                User user = userService.get(shop.getUserId());
                for (Promotion promotion : dataPage.getData()) {
                    if (promotion.getSellerId().equals(shop.getUserId())) {
                        promotion.setSellerName((shop.getAlias() == null || shop.getAlias().equals("")) ? user.getEmail() : shop.getAlias());
                    }
                    promotionIds.add(promotion.getId());
                }
            }
        }

        model.put("dataPage", dataPage);
        model.put("promotionSearch", promotionSearch);
        model.put("clientScript", "var promotionIds = " + gson.toJson(promotionIds)
                + "; promotion.init();");
        return "cp.promotion";
    }
}
