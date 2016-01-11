package vn.chodientu.controller.user;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.CashTransaction;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.input.CashTransactionSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.service.CashService;
import vn.chodientu.service.EmailService;
import vn.chodientu.util.TextUtils;

/**
 * @since May 20, 2014
 * @author Phu
 */
@Controller
@RequestMapping("/user")
public class CashController extends BaseUser {

    @Autowired
    private CashService cashService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/tai-khoan-xeng", method = RequestMethod.GET)
    public String list(ModelMap model, @RequestParam(value = "page", defaultValue = "1") int page, HttpSession session) {

        if (viewer == null || viewer.getUser() == null) {
            return "redirect:/user/signin.html?ref=" + baseUrl + "/user/tai-khoan-xeng.html";
        }
        Cash cash = null;
        try {
            cash = cashService.getCash(viewer.getUser().getId());
        } catch (Exception ex) {
        }
        CashTransactionSearch transactionSearch = new CashTransactionSearch();

        if (session.getAttribute("transactionSearch") != null && page != 0) {
            transactionSearch = (CashTransactionSearch) session.getAttribute("transactionSearch");
        } else {
            session.setAttribute("transactionSearch", transactionSearch);
        }

        transactionSearch.setPageIndex(page - 1);
        transactionSearch.setPageSize(15);
        transactionSearch.setUserId(viewer.getUser().getId());

        DataPage<CashTransaction> search = cashService.search(transactionSearch);
        Map<String, Double> calcReport = cashService.totalMoneyTransaction(transactionSearch);

        model.put("cash", cash);
        model.put("calcReport", calcReport);
        model.put("transactions", search);
        model.put("clientScript", "cash.init();");
        model.put("transactionSearch", transactionSearch);
        return "user.cash";
    }

    @RequestMapping(value = {"/tai-khoan-xeng"}, method = RequestMethod.POST)
    public String search(
            @ModelAttribute("transactionSearch") CashTransactionSearch transactionSearch,
            HttpSession session,
            ModelMap model, @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {
        if (viewer == null || viewer.getUser() == null) {
            return "redirect:/user/signin.html?ref=" + baseUrl + "/user/tai-khoan-xeng.html";
        }
        if (page > 0) {
            transactionSearch.setPageIndex(page - 1);
        } else {
            transactionSearch.setPageIndex(0);
        }
        transactionSearch.setPageSize(15);
        transactionSearch.setUserId(viewer.getUser().getId());

        DataPage<CashTransaction> search = cashService.search(transactionSearch);

        session.setAttribute("transactionSearch", transactionSearch);

        Map<String, Double> calcReport = cashService.totalMoneyTransaction(transactionSearch);
        Cash cash = null;
        try {
            cash = cashService.getCash(viewer.getUser().getId());
        } catch (Exception ex) {
        }

        model.put("cash", cash);
        model.put("calcReport", calcReport);
        model.put("transactions", search);
        model.put("clientScript", "cash.init();");
        model.put("transactionSearch", transactionSearch);
        return "user.cash";
    }

    /**
     * Ngân lượng trả về thanh toán và update vào hệ thống
     *
     * @param model
     * @param trans
     * @return
     */
    @RequestMapping(value = {"/thanh-toan-thanh-cong"}, method = RequestMethod.GET)
    public String paymentDone(ModelMap model, @RequestParam(value = "trans", defaultValue = "") String trans) {
        CashTransaction cashTransaction;
        String type = "";
        String message = "";
        try {
            cashTransaction = cashService.checkNLDataReturn(trans, null);
            String totalMoney = TextUtils.numberFormat((double) cashTransaction.getAmount());
            message = "Nạp thành công " + totalMoney + ".";
            type = "success";
       
            Map<String, Object> data = new HashMap<String, Object>();
                        data.put("username", viewer.getUser().getUsername() == null ? viewer.getUser().getEmail() : viewer.getUser().getUsername());
                emailService.send(EmailOutboxType.AUTO_9, System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000L), viewer.getUser().getEmail(), "Làm sao để sản phẩm của bạn lên đầu danh mục của Chodientu", "auto_9", data);
                emailService.send(EmailOutboxType.AUTO_10, System.currentTimeMillis() + (21 * 24 * 60 * 60 * 1000L), viewer.getUser().getEmail(), "Hãy cho sản phẩm của bạn cơ hội nổi bật nhất", "auto_10", data);
                emailService.send(EmailOutboxType.AUTO_11, System.currentTimeMillis() + (28 * 24 * 60 * 60 * 1000L), viewer.getUser().getEmail(), "Gia tăng tập khách hàng cho Shop của bạn ngay bây giờ", "auto_11", data);
                emailService.send(EmailOutboxType.AUTO_12, System.currentTimeMillis() + (35 * 24 * 60 * 60 * 1000L), viewer.getUser().getEmail(), "Gửi SMS chi phí rẻ nhất thị trường  với giá chưa đến 100 đồng", "auto_12", data);
                emailService.send(EmailOutboxType.AUTO_13, System.currentTimeMillis() + (42 * 24 * 60 * 60 * 1000L), viewer.getUser().getEmail(), "Gửi Email marketing chi phí rẻ nhất thị trường chỉ với 20 đồng", "auto_13", data);
        } catch (Exception ex) {
            message = ex.getMessage();
            type = "error";
        }
        message += "</br> Bạn sẽ được chuyển về trang quản trị xèng sau <span id='countdown'>5</span> giây.";
        model.put("message", message);
        model.put("type", type);

        String startScript = "setInterval(function(){document.getElementById('countdown').innerHTML = parseInt(document.getElementById('countdown').innerHTML) - 1 },1000);";
        startScript += "setTimeout(function() {document.location = '" + baseUrl + "/user/tai-khoan-xeng.html" + "';}, 5000);";
        model.put("title", "Thanh toán gói xèng tại chodientu.vn");
        model.put("clientScript", startScript);
        return "user.msg";
    }
}
