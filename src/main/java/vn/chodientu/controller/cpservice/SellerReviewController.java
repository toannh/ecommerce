/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.controller.cpservice;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.BaseRest;
import vn.chodientu.controller.cp.OrderController;
import vn.chodientu.entity.db.ItemReview;
import vn.chodientu.entity.db.ItemReviewLike;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.SellerReview;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.form.SellerReviewForm;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.EmailService;
import vn.chodientu.service.ImageService;
import vn.chodientu.service.ItemReviewService;
import vn.chodientu.service.MessageService;
import vn.chodientu.service.RealTimeService;
import vn.chodientu.service.SellerReviewService;
import vn.chodientu.service.UserService;

/**
 *
 * @author thunt
 */
@Controller("cpSellerReviewService")
@RequestMapping(value = "/cpservice/sellerreview")
public class SellerReviewController extends BaseRest {

    @Autowired
    private SellerReviewService sellerReviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private Gson gson;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private EmailService emailService;

    /**
     * Sửa trạng thái đánh giá
     *
     * @param itemId
     * @param reviewId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/changestatus", method = RequestMethod.GET)
    @ResponseBody
    public Response changestatus(@RequestParam String id) throws Exception {
        SellerReview active = sellerReviewService.changeActive(id);
        return new Response(true, "Thay đổi trạng thái đánh giá thành công", active);
    }

    @RequestMapping(value = "/getorderreview", method = RequestMethod.GET)
    @ResponseBody
    public Response getOrderReview(String id) throws Exception {
        Order order = sellerReviewService.getOrderReview(id);
        return new Response(true, "", order);
    }

    @RequestMapping(value = "/reviewAdmin", method = RequestMethod.POST)
    @ResponseBody
    public Response review(@RequestBody SellerReviewForm review, HttpServletRequest request) {
        try {
            SellerReview rv = sellerReviewService.reviewAdmin(review.getOrderId(), review.getContent(), review.getProductQuality(),
                    review.getReviewType(), request, viewer.getAdministrator().getEmail());
            User user = userService.get(rv.getSellerId());
            Map<String, Object> data = new HashMap<>();
            data.put("code", user.getId());
            data.put("username", (user.getUsername() == null || user.getUsername().equals("")) ? user.getEmail() : user.getUsername());
            if (user != null && rv != null) {
                emailService.send(EmailOutboxType.VERIFY, user.getEmail(), "Admin Đánh giá uy tín tại Chợ Điện Tử", "verify", data);
                realTimeService.add("Đơn hàng " + rv.getOrderId() + "Vừa được Admin " + viewer.getAdministrator().getEmail() + "đánh giá uy tín", rv.getSellerId(), "/"
                        + rv.getOrderId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
            }
            return new Response(true, "Đánh giá thành công", rv);
        } catch (Exception e) {
            return new Response(false, e.getMessage());
        }
    }
}
