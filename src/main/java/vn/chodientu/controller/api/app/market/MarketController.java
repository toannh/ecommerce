package vn.chodientu.controller.api.app.market;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.chodientu.controller.api.BaseApi;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.News;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.output.ItemHistogram;
import vn.chodientu.entity.output.Response;
import vn.chodientu.service.CashService;
import vn.chodientu.service.ItemService;
import vn.chodientu.service.MessageService;
import vn.chodientu.service.NewsService;
import vn.chodientu.service.SellerReviewService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;

/**
 * @since May 15, 2014
 * @author Phu
 */
@Controller("marketAppController")
@RequestMapping(value = "/app/market")
public class MarketController extends BaseApi {
    @Autowired
    private NewsService newsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SellerReviewService sellerReviewService;
    @Autowired
    private CashService cashService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerService sellerService;
    
    @RequestMapping(value = "/getlistshownotify", method = RequestMethod.GET)
    @ResponseBody
    public Response getListShowNotify(HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        List<News> listShowN = new ArrayList<>();
        List<News> listShowNotify = newsService.getListShowNotify();
        for (News news : listShowNotify) {
            News n = new News();
            n.setId(news.getId());
            n.setTitle(news.getTitle());
            listShowN.add(n);
        }
        return new Response(true, "Load thông báo mới ở trang chủ", listShowN);

    }
    @RequestMapping(value = "/inboxcount", method = RequestMethod.GET)
    @ResponseBody
    public Response inboxCount(@RequestParam("id") String userId,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        long inboxCount=messageService.reportInbox(viewer.getUser().getId());
        return new Response(true, "Load thông báo mới ở trang chủ", inboxCount);

    }
    @RequestMapping(value = "/loadinforeviewseller", method = RequestMethod.GET)
    @ResponseBody
    public Response loadInfoReviewSeller(@RequestParam("id") String sellerId,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        HashMap<String, Long> info = sellerReviewService.report(sellerId);
        return new Response(true, "Thông tin seller", info);
    }
    @RequestMapping(value = "/loadcashseller", method = RequestMethod.GET)
    @ResponseBody
    public Response loadCashSeller(@RequestParam("id") String sellerId,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            Cash cash = cashService.getCash(sellerId);
        return new Response(true, "Thống kê xèng", cash.getBalance());
        } catch (Exception e) {
            return new Response(false, "Thống kê xèng lỗi", e.getMessage());
        }
        
    }
    @RequestMapping(value = "/loadsellseller", method = RequestMethod.GET)
    @ResponseBody
    public Response loadSellSeller(@RequestParam("id") String sellerId,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            long statusCount = 0;
            List<ItemHistogram> itemHistograms = itemService.getItemStatusHistogram(sellerId);
            HashMap<String, Long> map=new HashMap<>();
            for (ItemHistogram itemHistogram : itemHistograms) {
                if (itemHistogram.getType().equals("outDate")) {
                    map.put("outDate", itemHistogram.getCount());
                    statusCount += itemHistogram.getCount();
                } else if (itemHistogram.getType().equals("outOfStock")) {
                    map.put("outOfStock", itemHistogram.getCount());
                    statusCount += itemHistogram.getCount();
                } else if (itemHistogram.getType().equals("unapproved")) {
                    map.put("unapproved", itemHistogram.getCount());
                    statusCount += itemHistogram.getCount();
                }
            }
            map.put("statusCount", statusCount);
            return new Response(true, "Thống kê bán hàng", map);
        } catch (Exception e) {
            return new Response(false, "Thống kê bán hàng lỗi", e.getMessage());
        }
        
    }
    //- Thông tin shop, thông tin tích hợp
    @RequestMapping(value = "/infoshop", method = RequestMethod.GET)
    @ResponseBody
    public Response infoShop(@RequestParam("id") String sellerId,HttpServletResponse response) throws Exception {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        if(!validateApi()){
                return new Response(false, "Bạn không có quyền truy cập!", null);
            }
        try {
            
        
        Response<User> user = userService.getById(sellerId);
        if (!user.isSuccess()) {
            user = userService.getById(sellerId);
            if (!user.isSuccess()) {
                return new Response(false, "User không tồn tại");
            }
        }
        User data = user.getData();
        Shop shop = shopService.getShop(data.getId());
        Seller seller = sellerService.getById(data.getId());
        HashMap<String, Object> map=new HashMap<>();

        map.put("user", data);
        map.put("shop", shop);
        map.put("seller", seller);
        return new Response(true, "Thông tin tài khoản", map);
        } catch (Exception e) {
            return new Response(false, "Có lỗi xảy ra", null);
        }
    }
}
