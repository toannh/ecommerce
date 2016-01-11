package vn.chodientu.controller.user;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.component.ScClient;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.ItemSync;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.enu.SyncType;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.SellerRepository;
import vn.chodientu.service.CashService;
import vn.chodientu.service.CityService;
import vn.chodientu.service.DistrictService;
import vn.chodientu.service.EmailService;
import vn.chodientu.service.ItemSyncService;
import vn.chodientu.service.SellerReviewService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.SellerStockService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;

/**
 * @since May 20, 2014
 * @author Phu
 */
@Controller
@RequestMapping("/user")
public class PaymentIntegrateController extends BaseUser {
    
    @Autowired
    private UserService userService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ScClient scClient;
    @Autowired
    private Gson json;
    @Autowired
    private SellerStockService sellerStockService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CashService cashService;
    @Autowired
    private ItemSyncService itemSyncService;
    @Autowired
    private EmailService emailService;
    @Autowired
    protected SellerReviewService sellerReviewService;
    
    @Async
    @RequestMapping(value = {"/cau-hinh-tich-hop"}, method = RequestMethod.GET)
    public String paymentIntegrate(
            @RequestParam(value = "merchant_id", defaultValue = "", required = false) String merchant_id,
            @RequestParam(value = "email", defaultValue = "", required = false) String email,
            @RequestParam(value = "cdt_id", defaultValue = "", required = false) String cdt_id,
            @RequestParam(value = "type", defaultValue = "", required = false) String type,
            @RequestParam(value = "itype", defaultValue = "nl", required = false) String itype,
            @RequestParam(value = "checksum", defaultValue = "-1", required = false) String checksum,
            @RequestParam(value = "code", defaultValue = "", required = false) String code,
            @RequestParam(value = "config", defaultValue = "false") boolean config,
            ModelMap map) throws Exception {
        User user = viewer.getUser();
        if (user == null) {
            return "redirect:/user/signin.html?ref=" + baseUrl + "/user/cau-hinh-tich-hop.html";
        }
        String chcksumConfirm = DigestUtils.md5Hex(merchant_id + "df19aa6d60" + email + cdt_id + type);
        Seller seller = null;
        try {
            seller = sellerService.getById(user.getId());
        } catch (Exception e) {
            seller = sellerService.createSeller(user);
        }
        
        if (!code.equals("")) {
            try {
                scClient.signin(code, seller);
                sellerRepository.save(seller);
                ItemSync itemSync = new ItemSync();
                itemSync.setSellerId(user.getId());
                itemSync.setSyncType(SyncType.SELLER_SYNC_COD_ITEM);
                itemSync.setScIntegrated(true);
                itemSync.setSync(true);
                itemSyncService.add(itemSync);
                //sellerService.processUpdateCodPaymentItem(user.getId(), true);
                try {
                    cashService.reward(CashTransactionType.INTEGRATED_COD, seller.getUserId(), seller.getUserId(), "/user/" + user.getId() + "/ho-so-nguoi-ban.html", null, null);
                } catch (Exception e) {
                }
                return "redirect:/user/cau-hinh-tich-hop.html?config=true";
            } catch (Exception e) {
            }
        }
        
        if (checksum.equals(chcksumConfirm)) {
            if (itype.equals("nl")) {
                seller.setNlEmail(email);
                seller.setNlIntegrated(true);
                seller.setNlId(cdt_id);
                seller.setCountNLIntergrated(seller.getCountNLIntergrated() + 1);
                seller.setUserId(viewer.getUser().getId());
                sellerService.update(seller);
                ItemSync itemSync = new ItemSync();
                itemSync.setSellerId(user.getId());
                itemSync.setSyncType(SyncType.SELLER_SYNC_ONLINE_ITEM);
                itemSync.setNlIntegrated(true);
                itemSync.setSync(true);
                itemSyncService.add(itemSync);
                //sellerService.processUpdateOnlinePaymentItem(user.getId(), true);
                if (seller.isScIntegrated()) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("username", user.getUsername() == null ? user.getEmail() : user.getUsername());
                    emailService.send(EmailOutboxType.AUTO_7, System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L), user.getEmail(), "Hãy tăng cơ hội tiếp cận 2 triệu người mua của Sàn Chodientu.vn", "auto_7", data);
                }
                try {
                    // Tích hợp ngân lượng
                    if (seller.getCountNLIntergrated() == 1) {
                        sellerReviewService.intergrateReview(user.getId(), "Tích hợp Ngân Lượng", 2, user.getId(), System.currentTimeMillis());
                    }
                    cashService.reward(CashTransactionType.INTEGRATED_NL, seller.getUserId(), seller.getUserId(), "/user/" + user.getId() + "/ho-so-nguoi-ban.html", null, null);
                } catch (Exception e) {
                }
            } else {
                seller.setScEmail(email);
                seller.setScIntegrated(true);
                seller.setScId(cdt_id);
                seller.setCountCodIntergrated(seller.getCountCodIntergrated() + 1);
                seller.setUserId(viewer.getUser().getId());
                sellerService.update(seller);
                ItemSync itemSync = new ItemSync();
                itemSync.setSellerId(user.getId());
                itemSync.setSyncType(SyncType.SELLER_SYNC_COD_ITEM);
                itemSync.setScIntegrated(true);
                itemSync.setSync(true);
                itemSyncService.add(itemSync);
                //sellerService.processUpdateCodPaymentItem(user.getId(), true);
                if (seller.isNlIntegrated()) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("username", user.getUsername() == null ? user.getEmail() : user.getUsername());
                    emailService.send(EmailOutboxType.AUTO_7, System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000L), user.getEmail(), "Hãy tăng cơ hội tiếp cận 2 triệu người mua của Sàn Chodientu.vn", "auto_7", data);
                }
                try {
                    // Tích hợp ship chung
                    if (seller.getCountCodIntergrated()== 1) {
                        sellerReviewService.intergrateReview(user.getId(), "Tích hợp Ship Chung", 2, user.getId(), System.currentTimeMillis());
                    }
                    cashService.reward(CashTransactionType.INTEGRATED_COD, seller.getUserId(), seller.getUserId(), "/user/" + user.getId() + "/ho-so-nguoi-ban.html", null, null);
                } catch (Exception e) {
                }
            }
        }
        if (seller != null) {
            map.put("nlEmail", seller.getNlEmail());
            map.put("nlStatus", seller.isNlIntegrated());
            map.put("scStatus", seller.isScIntegrated());
            map.put("scEmail", seller.getScEmail());
            map.put("seller", seller);
        }
        
        Response users = userService.getById(user.getId());
        viewer.setUser(user);
        List<City> listCt = cityService.list();
        List<District> listDt = districtService.list();
        map.put("user", users.getData());
        map.put("module", "paymentIntegrate");
        String cof = "";
        
        if (config && seller.isNlIntegrated() && seller.isScIntegrated() && shopService.getShop(seller.getUserId()) == null) {
            cof = "payment.openShop();";
        }
        map.put("clientScript", "var citys= " + json.toJson(listCt) + ";var districts = " + json.toJson(listDt) + ";payment.init();" + cof);
        return "user.paymentintegrate";
    }
    
    @RequestMapping(value = {"/chinh-sach-ban-hang"}, method = RequestMethod.GET)
    public String sellerPolicy(ModelMap map) {
        User user = viewer.getUser();
        if (user == null || viewer == null) {
            return "redirect:/user/signin.html?ref=" + baseUrl + "/user/chinh-sach-ban-hang.html";
        }
        Seller seller = null;
        try {
            seller = sellerService.getById(user.getId());
        } catch (Exception e) {
            sellerService.createSeller(user);
        }
        map.put("clientScript", "var sellerPolicy=" + json.toJson(seller.getSalesPolicy()) + ";payment.initSellerPolicy();");
        return "user.sellerpolicy";
    }
    
}
