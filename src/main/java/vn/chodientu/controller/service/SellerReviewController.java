package vn.chodientu.controller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.user.BaseUser;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.SellerReview;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.form.SellerReviewForm;
import vn.chodientu.entity.input.SellerReviewSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.EmailService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.OrderService;
import vn.chodientu.service.RealTimeService;
import vn.chodientu.service.SellerReviewService;
import vn.chodientu.service.UserService;

/**
 * @author Phuc
 */
@Controller("serviceSellerReview")
@RequestMapping("/sellerreview")
public class SellerReviewController extends BaseUser {

    @Autowired
    private SellerReviewService sellerReviewService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    @ResponseBody
    public Response review(@RequestBody SellerReviewForm review, HttpServletRequest request) {
        try {
            SellerReview rv = sellerReviewService.review(review.getOrderId(), review.getContent(), review.getProductQuality(), review.getInteractive(),
                    review.getShippingCosts(), review.getReviewType(), request);
            User user = userService.get(rv.getSellerId());
            Map<String, Object> data = new HashMap<>();
            data.put("code", user.getId());
            data.put("username", (user.getUsername() == null || user.getUsername().equals("")) ? user.getEmail() : user.getUsername());
            if (user != null && rv != null) {
                emailService.send(EmailOutboxType.VERIFY, user.getEmail(), "Đánh giá uy tín tại Chợ Điện Tử", "verify", data);
                realTimeService.add("Đơn hàng " + rv.getOrderId() + " vừa được đánh giá uy tín", rv.getSellerId(), "/" + 
                    rv.getOrderId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
            }
            return new Response(true, "Đánh giá thành công", rv);
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }

    @RequestMapping(value = "/loadinforeviewseller", method = RequestMethod.GET)
    @ResponseBody
    public Response loadInfoReviewSeller(@RequestParam String sellerId) throws Exception {
        HashMap<String, Long> info = sellerReviewService.report(sellerId);
        double totalPoint = sellerReviewService.calReputationPoints(sellerId);
        info.put("totalPoint", Math.round(totalPoint));
        return new Response(true, "Info review item", info);
    }

    @RequestMapping(value = "/getbyorderids", method = RequestMethod.POST)
    @ResponseBody
    public Response getbyorderids(@RequestBody List<String> ids, @RequestParam String userId, @RequestParam boolean seller) throws Exception {
        List<SellerReview> byOrderIds = sellerReviewService.getByOrderIds(ids, userId, seller);
        return new Response(true, null, byOrderIds);
    }

    @RequestMapping(value = "/getorderreview", method = RequestMethod.GET)
    @ResponseBody
    public Response getOrderReview(String id) throws Exception {
        Order order = sellerReviewService.getOrderReview(id);
        return new Response(true, "", order);
    }

    @RequestMapping(value = "/sellerreviewsearch", method = RequestMethod.GET)
    @ResponseBody
    public Response sellerreviewsearch(@ModelAttribute SellerReviewSearch reviewSearch) throws Exception {
        reviewSearch.setPageIndex(reviewSearch.getPageIndex() > 0 ? reviewSearch.getPageIndex() - 1 : 0);
        DataPage<SellerReview> search = sellerReviewService.searchSellerReview(reviewSearch);
        List<String> orderIds = new ArrayList<>();
        List<String> itemIds = new ArrayList<>();
        List<String> userIds = new ArrayList<>();
        for (SellerReview review : search.getData()) {
            if (!orderIds.contains(review.getOrderId())) {
                orderIds.add(review.getOrderId());
            }
        }
        Map<String, Object> mapPoint = new HashMap<>();
        if (orderIds != null && !orderIds.isEmpty()) {
            List<OrderItem> orderItemsByOrderIds = orderService.getOrderItemsByOrderIds(orderIds);
            if (!orderItemsByOrderIds.isEmpty()) {
                for (OrderItem orderItem : orderItemsByOrderIds) {
                    if (!itemIds.contains(orderItem.getItemId())) {
                        itemIds.add(orderItem.getItemId());
                    }
                    for (SellerReview sellerReview : search.getData()) {
                        if (sellerReview.getOrderId() == null) {
                            sellerReview.setOrderId("");
                        }
                        if (sellerReview.getOrderId().equals(orderItem.getOrderId())) {
                            if (sellerReview.getItemIds() == null) {
                                sellerReview.setItemIds(new ArrayList<String>());
                            }
                            sellerReview.getItemIds().add(orderItem.getItemId());
                            double reputationPoints = sellerReviewService.calReputationPoints(sellerReview.getUserId());
                            mapPoint.put(sellerReview.getUserId(), reputationPoints);
                        }
                        if (!userIds.contains(sellerReview.getUserId())) {
                            userIds.add(sellerReview.getUserId());
                        }
                        if (sellerReview.getUserIds() == null) {
                            sellerReview.setUserIds(new ArrayList<String>());
                        }
                        sellerReview.getUserIds().add(sellerReview.getUserId());
                    }
                }
            }
        }
        List<Item> items = itemService.list(itemIds);
        List<User> users = userService.getAllUserByIds(userIds);
        mapPoint.put("reviewPageOrder", search);
        mapPoint.put("itemOrder", items);
        mapPoint.put("userOrder", users);

        return new Response(true, null, mapPoint);
    }
}
