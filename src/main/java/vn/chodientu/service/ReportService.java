package vn.chodientu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.db.report.ReportBuyer;
import vn.chodientu.entity.db.report.ReportCash;
import vn.chodientu.entity.db.report.ReportItem;
import vn.chodientu.entity.db.report.ReportLading;
import vn.chodientu.entity.db.report.ReportOrder;
import vn.chodientu.entity.db.report.ReportSeller;
import vn.chodientu.entity.db.report.ReportShop;
import vn.chodientu.entity.db.report.ReportUser;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.enu.ListingType;
import vn.chodientu.entity.enu.PaymentMethod;
import vn.chodientu.entity.enu.PaymentStatus;
import vn.chodientu.entity.enu.ShipmentStatus;
import vn.chodientu.entity.input.ReportSearch;
import vn.chodientu.entity.output.ItemHistogram;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.CashTransactionRepository;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.LadingRepository;
import vn.chodientu.repository.OrderItemRepository;
import vn.chodientu.repository.OrderRepository;
import vn.chodientu.repository.ReportBuyerRepository;
import vn.chodientu.repository.ReportCashRepository;
import vn.chodientu.repository.ReportItemRepository;
import vn.chodientu.repository.ReportLadingRepository;
import vn.chodientu.repository.ReportOrderRepository;
import vn.chodientu.repository.ReportSellerRepository;
import vn.chodientu.repository.ReportShopRepository;
import vn.chodientu.repository.ReportUserRepository;
import vn.chodientu.repository.SellerRepository;
import vn.chodientu.repository.ShopRepository;
import vn.chodientu.repository.UserLockRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.util.TextUtils;

@Service
public class ReportService {

    @Autowired
    private ReportSellerRepository reportSellerRepository;
    @Autowired
    private ReportUserRepository reportUserRepository;
    @Autowired
    private ReportBuyerRepository reportBuyerRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ReportShopRepository reportShopRepository;
    @Autowired
    private ReportOrderRepository reportOrderRepository;
    @Autowired
    private ReportLadingRepository reportLadingRepository;
    @Autowired
    private ReportItemRepository reportItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LadingRepository ladingRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;
    @Autowired
    private ReportCashRepository reportCashRepository;
    @Autowired
    private UserLockRepository userLockRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "00 56 23 * * *")
    public void run() {
        this.runReportSeller();
        this.runReportBuyer();
        this.runReportUser();
        this.runReportShop();
        this.runReportOder();
        this.runReportItem();
        this.runReportLading();
        this.runReportCash();
    }

    @Async
    public void runReportItem() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportItem report = reportItemRepository.find(time);
        if (report == null) {
            report = new ReportItem();
            report.setCreateTime(System.currentTimeMillis());
            report.setTime(time);
        }
        report.setUpdateTime(System.currentTimeMillis());
        this.cItem(report);
        this.cItemSource(report);
        reportItemRepository.save(report);
    }

    @Async
    public void runReportCash() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportCash report = reportCashRepository.find(time);
        if (report == null) {
            report = new ReportCash();
            report.setCreateTime(System.currentTimeMillis());
            report.setTime(time);
        }
        report.setUpdateTime(System.currentTimeMillis());
        this.cCash(report);
        reportCashRepository.save(report);
    }

    @Async
    public void runReportLading() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportLading report = reportLadingRepository.find(time);
        if (report == null) {
            report = new ReportLading();
            report.setCreateTime(System.currentTimeMillis());
            report.setTime(time);
        }
        report.setUpdateTime(System.currentTimeMillis());
        this.cLading(report);
        reportLadingRepository.save(report);
    }

    @Async
    public void runReportOder() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportOrder reportOrder = reportOrderRepository.find(time);
        if (reportOrder == null) {
            reportOrder = new ReportOrder();
            reportOrder.setCreateTime(System.currentTimeMillis());
            reportOrder.setTime(time);
        }
        reportOrder.setUpdateTime(System.currentTimeMillis());
        this.cOrderStatus(reportOrder);
        this.cOrderMethod(reportOrder);
        this.cOrderPaidGMV(reportOrder);
        this.cOrder(reportOrder);
        reportOrderRepository.save(reportOrder);
    }

    @Async
    public void runReportUser() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportUser reportUser = reportUserRepository.find(time);
        if (reportUser == null) {
            reportUser = new ReportUser();
            reportUser.setCreateTime(System.currentTimeMillis());
            reportUser.setTime(time);
        }
        //count newbie
        reportUser.setNewbie(this.countNewbie());
        reportUser.setTotalEmailVerified(this.countEmailVerified(true));
        reportUser.setEmailVerified(this.countEmailVerified(false));
        reportUser.setTotalPhoneVerified(this.countPhoneVerified(true));
        reportUser.setPhoneVerified(this.countPhoneVerified(false));
        reportUser.setTotalUser(userRepository.count(new Query(new Criteria())));
        reportUser.setUpdateTime(System.currentTimeMillis());
        reportUser.setUserlocked(this.countLockUser(false));
        reportUser.setTotalUserLocked(this.countLockUser(true));
//        reportUser.setUserNoActive(this.countUserNoActive(false));
//        reportUser.setTotalUserNoActive(this.countUserNoActive(true));
        reportUser.setTotalUserSCIntegrated(this.countUserSCIntegrated());
        reportUser.setTotalUserNLIntegrated(this.countUserNLIntegrated());
        reportUserRepository.save(reportUser);
    }

    @Async
    public void runReportShop() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportShop reportShop = reportShopRepository.find(time);
        if (reportShop == null) {
            reportShop = new ReportShop();
            reportShop.setCreateTime(System.currentTimeMillis());
            reportShop.setTime(time);
        }
        reportShop.setUpdateTime(System.currentTimeMillis());
        reportShop.setShop(shopRepository.count(new Query(new Criteria())));
        reportShop.setNewshop(this.countNewShop());
        reportShop.setLockedShop(this.countLockedShop());
        reportShopRepository.save(reportShop);
    }

    public long countLockedShop() {
        List<Shop> listShop = shopRepository.find(new Query(new Criteria()));
        List<Shop> result = new ArrayList<Shop>();
        for (Shop tempShop : listShop) {
            Criteria cri1 = new Criteria("_id").is(tempShop.getUserId()).and("active").is(false);
            Query query1 = new Query(cri1);
            if (userRepository.find(query1).size() > 0) {
                result.add(tempShop);
                continue;
            }
            Criteria cri2 = new Criteria("_id").is(tempShop.getUserId()).and("nlIntegrated").is(true).and("scIntegrated").is(true);
            Query query2 = new Query(cri2);
            if (sellerRepository.find(query2).size() <= 0) {
                result.add(tempShop);
                continue;
            }
        }
        return result.size();
    }

    private void cLading(ReportLading report) {
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        report.setLading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.NEW.toString());
        report.setNewlading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        report.setDelivered(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DELIVERING.toString());
        report.setDelivering(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DENIED.toString());
        report.setDenied(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.RETURN.toString());
        report.setReturnLading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.STOCKING.toString());
        report.setStocking(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("type").is(PaymentMethod.COD.toString());
        report.setLadingCod(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("type").is(PaymentMethod.NONE.toString());
        report.setLadingShipping(ladingRepository.count(new Query(cri)));
    }

    private void cOrder(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        reportOrder.setQuantity(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPrice(countPrice.get("finalPrice"));
            reportOrder.setTotalPrice(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    private void cCash(ReportCash reportCash) {
        Criteria criteria = new Criteria();
        criteria.and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        criteria.orOperator(new Criteria("type").is(CashTransactionType.SPENT_UPITEM.toString()),
                new Criteria("type").is(CashTransactionType.SPENT_VIPITEM.toString()),
                new Criteria("type").is(CashTransactionType.SPENT_EMAIL.toString()),
                new Criteria("type").is(CashTransactionType.ACTIVE_MARKETING.toString()),
                new Criteria("type").is(CashTransactionType.ACTIVE_QUICK_SUBMIT.toString()),
                new Criteria("type").is(CashTransactionType.CLOSE_ADV.toString()),
                new Criteria("type").is(CashTransactionType.TOP_UP.toString()),
                new Criteria("type").is(CashTransactionType.SPENT_SMS.toString()));
        reportCash.setUseBalance(cashTransactionRepository.sumForReport(criteria));
        reportCash.setUseBalanceQuantity(cashTransactionRepository.count(new Query(criteria)));

        criteria = new Criteria();
        Criteria c1 = new Criteria("type").is(CashTransactionType.TOPUP_NL.toString()).and("nlStatus").ne(2);
        Criteria c2 = new Criteria("type").is(CashTransactionType.SMS_NAP.toString());
        criteria.orOperator(c1, c2);
        criteria.and("nlPayTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        reportCash.setTopup(cashTransactionRepository.sumForReport(criteria));
        reportCash.setTopupQuantity(cashTransactionRepository.count(new Query(criteria)));

        criteria = new Criteria();
        criteria.and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        Criteria cri1 = new Criteria("type").is(CashTransactionType.COMMENT_MODEL_REWARD.toString());
        Criteria cri2 = new Criteria("type").is(CashTransactionType.COMMENT_ITEM_REWARD.toString());
        Criteria cri3 = new Criteria("type").is(CashTransactionType.SELLER_POST_NEWS.toString());
        Criteria cri4 = new Criteria("type").is(CashTransactionType.VIEW_PAGE.toString());
        Criteria cri5 = new Criteria("type").is(CashTransactionType.SIGNIN.toString());
        Criteria cri6 = new Criteria("type").is(CashTransactionType.REGISTER.toString());
        Criteria cri7 = new Criteria("type").is(CashTransactionType.PAYMENT_SUSSESS_NL.toString());
        Criteria cri9 = new Criteria("type").is(CashTransactionType.INTEGRATED_NL.toString());
        Criteria cri10 = new Criteria("type").is(CashTransactionType.INTEGRATED_COD.toString());
        Criteria cri11 = new Criteria("type").is(CashTransactionType.SELLER_POST_ITEM.toString());
        Criteria cri12 = new Criteria("type").is(CashTransactionType.OPEN_SHOP.toString());
        Criteria cri13 = new Criteria("type").is(CashTransactionType.SELLER_CREATE_PROMOTION.toString());
        Criteria cri14 = new Criteria("type").is(CashTransactionType.BROWSE_LADING.toString());
        Criteria cri15 = new Criteria("type").is(CashTransactionType.EMAIL_VERIFIED.toString());
        Criteria cri16 = new Criteria("type").is(CashTransactionType.PHONE_VERIFIED.toString());
        criteria.orOperator(cri1, cri2, cri3, cri4, cri5, cri6, cri7, cri9, cri10, cri11, cri12, cri13, cri14, cri15, cri16);
        reportCash.setReward(cashTransactionRepository.sumForReport(criteria));
        reportCash.setRewardQuantity(cashTransactionRepository.count(new Query(criteria)));

        criteria = new Criteria();
        criteria.and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        criteria.and("type").is(CashTransactionType.PANALTY_BLANCE.toString());
        reportCash.setPanaltyBlance(cashTransactionRepository.sumForReport(criteria));
        reportCash.setPanaltyBlanceQuantity(cashTransactionRepository.count(new Query(criteria)));

        reportCash.setUpSchedule(cashTransactionRepository.sumForReport(new Criteria().and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true)).orOperator(new Criteria("type").is(CashTransactionType.SPENT_UPITEM.toString()))));
        reportCash.setVipItem(cashTransactionRepository.sumForReport(new Criteria().and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true)).orOperator(new Criteria("type").is(CashTransactionType.SPENT_VIPITEM.toString()))));
        reportCash.setActiveCustomer(cashTransactionRepository.count(new Query(new Criteria().and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true)).orOperator(new Criteria("type").is(CashTransactionType.ACTIVE_MARKETING.toString())))));
        reportCash.setActiveQuickBooking(cashTransactionRepository.count(new Query(new Criteria().and("time").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true)).orOperator(new Criteria("type").is(CashTransactionType.ACTIVE_QUICK_SUBMIT.toString())))));
    }

    private void cOrderStatus(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("paymentStatus").is(PaymentStatus.NEW.toString());
        reportOrder.setNewStatus(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceNewStatus(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceNewStatus(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        Criteria c1 = new Criteria("paymentStatus").is(PaymentStatus.PAID.toString());
        c1.and("paidTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        Criteria c2 = new Criteria("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        c2.and("shipmentUpdateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.orOperator(c1, c2);
        reportOrder.setPaidStatus(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePaidStatus(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePaidStatus(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("paymentStatus").is(PaymentStatus.PENDING.toString());
        reportOrder.setPadingStatus(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePadingStatus(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePadingStatus(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    private void cOrderMethod(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("paymentMethod").is(PaymentMethod.NONE.toString());
        reportOrder.setNonePayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceNonePayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceNonePayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("paymentMethod").is(PaymentMethod.COD.toString());
        reportOrder.setCodPayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceCodPayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceCodPayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        Criteria c1 = new Criteria("paymentMethod").ne(PaymentMethod.NONE.toString());

        Criteria c2 = new Criteria("paymentMethod").ne(PaymentMethod.COD.toString());
        cri.andOperator(c1, c2);

        reportOrder.setNlPayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceNlPayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceNlPayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.andOperator(new Criteria("paymentMethod").is(PaymentMethod.VISA.toString()), new Criteria("paymentMethod").is(PaymentMethod.MASTER.toString()));
        reportOrder.setVisaPayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceVisaPayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceVisaPayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    private void cOrderPaidGMV(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("paidTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        Criteria c1 = new Criteria("paymentMethod").ne(PaymentMethod.COD.toString());
        Criteria c2 = new Criteria("paymentMethod").ne(PaymentMethod.NONE.toString());
        cri.andOperator(c1, c2);
        cri.and("paymentStatus").is(PaymentStatus.PAID.toString());
        reportOrder.setNlPaymentPaid(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePaidStatusNL(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePaidStatusNL(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("shipmentUpdateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("paymentMethod").is(PaymentMethod.COD.toString());
        cri.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        reportOrder.setCodPaymentPaid(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePaidStatusCOD(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePaidStatusCOD(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("refundStatus").is(true);
        reportOrder.setOrderReturn(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceReturn(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceReturn(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

    }

    private long countNewShop() {
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        return shopRepository.count(new Query(cri));
    }

    private long countNewbie() {
        Criteria cri = new Criteria();
        cri.and("joinTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        return userRepository.count(new Query(cri));
    }

    private long countEmailVerified(boolean all) {
        Criteria cri = new Criteria();
        if (!all) {
            cri.and("emailVerifiedTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        }
        cri.and("emailVerified").is(true);
        return userRepository.count(new Query(cri));
    }

    private long countPhoneVerified(boolean all) {
        Criteria cri = new Criteria();
        if (!all) {
            cri.and("phoneVerifiedTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        }
        cri.and("phoneVerified").is(true);
        return userRepository.count(new Query(cri));
    }

    private long getTime(long time, boolean endday) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            cal.setTime(new Date(time));
            return sdfTime.parse(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + (endday ? " 23:59:59" : " 00:00:00")).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    private void cItem(ReportItem report) {
        List<ItemHistogram> histograms = itemService.getItemStatusHistogramMongo(null);
        for (ItemHistogram itemHistogram : histograms) {
            switch (itemHistogram.getType()) {
                case "unapproved":
                    //report.setUnapproved(itemHistogram.getCount());
                    break;
                case "outDate":
                    report.setOutDate(itemHistogram.getCount());
                    break;
                case "outOfStock":
                    report.setOutOfStock(itemHistogram.getCount());
                    break;
                case "uncompleted":
                    report.setUncompleted(itemHistogram.getCount());
                    break;
                case "recycle":
                    //report.setRecycle(itemHistogram.getCount());
                    break;
                case "all":
                    report.setTotal(itemHistogram.getCount());
                    break;
                case "selling":
                    //report.setSelling(itemHistogram.getCount());
                    break;
            }
        }
    }

    private void cItemSource(ReportItem report) {
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("source").is(ItemSource.SELLER.toString());
        report.setSeller(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("source").is(ItemSource.API.toString());
        report.setApi(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("source").is(ItemSource.CRAWL.toString());
        report.setCrawl(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("active").is(true).and("completed").is(true).and("approved").is(false);
        report.setUnapproved(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        cri.and("active").is(false);
        report.setRecycle(itemRepository.count(new Query(cri)));
        //Số lượng sản phẩm bán thành công: Sold items
        cri = new Criteria();
        Criteria cri1 = new Criteria();
        cri1.and("paidTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri1.and("paymentStatus").is(PaymentStatus.PAID.toString());
        Criteria cri2 = new Criteria();
        cri2.and("shipmentUpdateTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri2.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());

        cri.orOperator(cri1, cri2);
        List<String> orderIds = new ArrayList<>();
        List<Order> orders = orderRepository.find(new Query(cri));
        for (Order order : orders) {
            if (!orderIds.contains(order.getId())) {
                orderIds.add(order.getId());
            }
        }
        int quantity = 0;
        if (orderIds != null && !orderIds.isEmpty()) {
            List<OrderItem> orderItems = orderItemRepository.find(new Query(new Criteria("orderId").in(orderIds)));
            for (OrderItem orderItem : orderItems) {
                quantity += orderItem.getQuantity();
            }
        }
        report.setSoldItem(quantity);
        //Số lượng tin bán đang bán trên sàn: live listing
        cri = new Criteria();
        Criteria c1 = new Criteria("startTime").lte(System.currentTimeMillis());
        Criteria c2 = new Criteria("endTime").gte(System.currentTimeMillis());
        Criteria c3 = new Criteria("active").is(true);
        Criteria c4 = new Criteria("completed").is(true);
        Criteria c5 = new Criteria("approved").is(true);
        Criteria c6 = new Criteria("quantity").gt(0);
        Criteria c7 = new Criteria("listingType").ne(ListingType.AUCTION.toString());
        Criteria c8 = new Criteria("price").gt(0);
        Criteria c9 = new Criteria();
        c9.orOperator(c7, c8);
        cri.andOperator(c1, c2, c3, c4, c5, c6, c9);
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        long count = itemRepository.count(new Query(cri));
        report.setLiveListing(count);

        //Số lượng Người bán có tin bán:Number of listers
        report.setNumberSellerLister(itemRepository.getDistincUser(getTime(report.getCreateTime(), false), getTime(report.getCreateTime(), true)));
    }

    /**
     * build query
     *
     * @param search
     * @return
     */
    private Criteria buildSearch(ReportSearch search) {
        Criteria cri = new Criteria();
        long startTime = (search.getStartTime() > 0) ? search.getStartTime() : new Date().getTime();
        long endTime = (search.getEndTime() > 0) ? search.getEndTime() : new Date().getTime();
        cri.and("time").gte(startTime).lt(endTime);
        return cri;
    }

    private String converTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1);
    }

    //Service get data
    public Response findDataShop(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportShop> report = reportShopRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Tổng shop");
        row.add("Shop mới");
//        row.add("Shop bị khóa");
        data.add(row);
        long totalNewShop = 0;
        for (ReportShop reportShop : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportShop.getTime()));
            row.add(reportShop.getShop());
            row.add(reportShop.getNewshop());
            totalNewShop += reportShop.getNewshop();
//            row.add(reportShop.getLockedShop());
            data.add(row);
        }

        resp.put("chart", data);
        row = new ArrayList<>();
        row.add("Tổng số Shop mới: " + TextUtils.formatNumber(totalNewShop));
        resp.put("dataRow", row);
        //
        row = new ArrayList<>();
        row.add("Tổng số Shop: " + TextUtils.formatNumber(report.get(report.size() - 1).getShop()));
        row.add("Tổng số Shop bị khóa (toàn thời gian): " + TextUtils.formatNumber(report.get(report.size() - 1).getLockedShop()));
        resp.put("dataRowTotal", row);
        return new Response(true, "Thống kê dữ liệu shop", resp);
    }

    //Service get data
    public Response findDataItem(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportItem> report = reportItemRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Sản phẩm Crawl");
        row.add("Sản phẩm từ người bán đăng");
        row.add("Sản phẩm lấy từ API");
        row.add("Sản phẩm không được duyệt");
        row.add("Sản phẩm bị xóa");
        row.add("Sản phẩm bán thành công");
        row.add("Sản phẩm đang bán trên sàn");
        data.add(row);
        long crawl = 0;
        long seller = 0;
        long api = 0;
        long unapproved = 0;
        long recycle = 0;
        long soldItem = 0;
        long liveListing = 0;
        long sellerListing = 0;
        for (ReportItem reportItem : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportItem.getTime()));
            row.add(reportItem.getCrawl());
            row.add(reportItem.getSeller());
            row.add(reportItem.getApi());
            row.add(reportItem.getUnapproved());
            row.add(reportItem.getRecycle());
            row.add(reportItem.getSoldItem());
            row.add(reportItem.getLiveListing());
            data.add(row);
            crawl += reportItem.getCrawl();
            seller += reportItem.getSeller();
            api += reportItem.getApi();
            unapproved += reportItem.getUnapproved();
            recycle += reportItem.getRecycle();
            soldItem += reportItem.getSoldItem();
            liveListing += reportItem.getLiveListing();
            sellerListing += reportItem.getNumberSellerLister();
        }
        resp.put("chart", data);

        row = new ArrayList<>();
        row.add("Sản phẩm Crawl: " + (this.conventLongToD(crawl)));
        row.add("Sản phẩm từ người bán đăng: " + (this.conventLongToD(seller)));
        row.add("Sản phẩm lấy từ API: " + (this.conventLongToD(api)));
        row.add("Sản phẩm không được duyệt: " + (this.conventLongToD(unapproved)));
        row.add("Sản phẩm bị xóa: " + (this.conventLongToD(recycle)));
        row.add("Sản phẩm bán thành công: " + (this.conventLongToD(soldItem)));
        row.add("Sản phẩm đang bán trên sàn: " + (this.conventLongToD(liveListing)));
        row.add("Số người bán có tin bán trên sàn: " + (this.conventLongToD(sellerListing)));
        resp.put("dataSearch", row);
        long crawlT = 0;
        long totalT = 0;
        long sellerT = 0;
        long apiT = 0;
        long unapprovedT = 0;
        long recycleT = 0;
        long soldItemT = 0;
        long liveListingT = 0;
        long sellerListingT = 0;
        List<ReportItem> report1 = reportItemRepository.find(new Query(new Criteria()).with(new Sort(Sort.Direction.DESC, "time")));
        row = new ArrayList<>();
        for (ReportItem report11 : report1) {
            totalT += report11.getTotal();
            crawlT += report11.getCrawl();
            sellerT += report11.getSeller();
            apiT += report11.getApi();
            unapprovedT += report11.getUnapproved();
            recycleT += report11.getRecycle();
            soldItemT += report11.getSoldItem();
            liveListingT += report11.getLiveListing();
            sellerListingT += report11.getNumberSellerLister();
        }
        Map<String, Long> itemStatusHistogramReport = itemService.getItemStatusHistogramReport();
        row.add("Sản phẩm Crawl: " + (this.conventLongToD(itemStatusHistogramReport.get("totalCrawl"))));
        row.add("Sản phẩm từ người bán đăng: " + (this.conventLongToD(itemStatusHistogramReport.get("totalSeller"))));
        row.add("Sản phẩm lấy từ API: " + (this.conventLongToD(itemStatusHistogramReport.get("totalAPI"))));    
        row.add("Sản phẩm không được duyệt: " + (this.conventLongToD(unapprovedT)));
        row.add("Sản phẩm bị xóa: " + (this.conventLongToD(recycleT)));
        row.add("Sản phẩm bán thành công: " + (this.conventLongToD(soldItemT)));
        row.add("Sản phẩm đang bán trên sàn: " + (this.conventLongToD(itemStatusHistogramReport.get("total"))));
        //long distincUser = itemRepository.getDistincUser(949120773000l, System.currentTimeMillis());
        row.add("Số người bán có tin bán trên sàn: " + (this.conventLongToD(sellerListingT)));
        resp.put("dataNow", row);
        resp.put("timeNow", report1.get(0).getTime());

        return new Response(true, "Thống kê dữ liệu item", resp);
    }

    //Service get data
    public Response findDataUser(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportUser> report = reportUserRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Người dùng mới");
        row.add("Người dùng kích hoạt email");
        row.add("Người dùng kích hoạt phone");
        row.add("Người dùng bị khóa tài khoản");
        data.add(row);
        long totalUser = 0, totalEmailVerified = 0, totalPhoneVerified = 0, totalUserLocked = 0;

        for (ReportUser reportUser : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportUser.getTime()));
            row.add(reportUser.getNewbie());
            row.add(reportUser.getEmailVerified());
            row.add(reportUser.getPhoneVerified());
            row.add(reportUser.getUserlocked());
            totalUser += reportUser.getNewbie();
            totalEmailVerified += reportUser.getEmailVerified();
            totalPhoneVerified += reportUser.getPhoneVerified();
            totalUserLocked += reportUser.getUserlocked();
//            row.add(reportUser.getUserNoActive());
            data.add(row);
        }
        resp.put("chart", data);
        row = new ArrayList<>();
        row.add("Tổng số người dùng: " + TextUtils.formatNumber(totalUser));
        row.add("Tổng số người dùng kích hoạt Email: " + TextUtils.formatNumber(totalEmailVerified));
        row.add("Tổng số người dùng kích hoạt phone: " + TextUtils.formatNumber(totalPhoneVerified));
        row.add("Tổng số người dùng bị khóa: " + TextUtils.formatNumber(totalUserLocked));
        resp.put("dataRow", row);
        //
        row = new ArrayList<>();
        row.add("Tổng số người dùng: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalUser()));
        row.add("Tổng số người dùng kích hoạt Email: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalEmailVerified()));
        row.add("Tổng số người dùng kích hoạt phone: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalPhoneVerified()));
        row.add("Tổng số người dùng bị khóa: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalUserLocked()));
        row.add("Tổng số người dùng tích hợp ShipChung (toàn thời gian): " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalUserSCIntegrated()));
        row.add("Tổng số người dùng tích hợp NganLuong (toàn thời gian): " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalUserNLIntegrated()));
        resp.put("dataRowTotal", row);
        return new Response(true, "Thống kê dữ liệu người dùng", resp);
    }

    public Response findDataOrder(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        List<ReportOrder> reportOrders = reportOrderRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Chưa thanh toán");
        row.add("Đã thanh toán");
        row.add("Chờ thanh toán");
        row.add("Đã thanh toán thành công qua NL");
        row.add("Đã thanh toán thành công qua Cod");
        row.add("Hóa đơn refund");
        data.add(row);
        for (ReportOrder reportOrder : reportOrders) {
            row = new ArrayList<>();
            row.add(this.converTime(reportOrder.getTime()));
            row.add(reportOrder.getNewStatus());
            row.add(reportOrder.getPaidStatus());
            row.add(reportOrder.getPadingStatus());
            row.add(reportOrder.getNlPaymentPaid());
            row.add(reportOrder.getCodPaymentPaid());
            row.add(reportOrder.getOrderReturn());
            data.add(row);
        }
        resp.put("chart", data);
        Map<String, Long> reportSumOrderFinal = reportOrderRepository.reportSumOrderFinal(cri);
        Map<String, Long> reportSumOrder = reportOrderRepository.reportSumOrder(cri);
        resp.put("dataFinal", reportSumOrderFinal);
        resp.put("dataTotal", reportSumOrder);
        return new Response(true, "Thống kê dữ liệu đơn hàng", resp);
    }

    public Response findDataLading(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportLading> report = reportLadingRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Hàng đã tới tay người mua");
        row.add("Đã hủy");
        data.add(row);
        long ladingCod = 0;
        long ladingShiping = 0;
        long ladingDelivered = 0;
        long ladingDenied = 0;
        for (ReportLading reportLading : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportLading.getTime()));
            row.add(reportLading.getDelivered());
            row.add(reportLading.getDenied());
            data.add(row);
            ladingShiping += reportLading.getLadingShipping();
            ladingCod += reportLading.getLadingCod();
            ladingDelivered += reportLading.getDelivered();
            ladingDenied += reportLading.getDenied();
        }
        resp.put("chart", data);
        row = new ArrayList<>();
        row.add("Vận đơn vận chuyển: " + ladingShiping);
        row.add("Vận đơn COD: " + ladingCod);
        //row.add("Chưa giao hàng: " + ladingStocking);
        //row.add("Hàng đang đi trên đường: " + ladingDelivering);
        row.add("Hàng đã tới tay người mua thành công: " + ladingDelivered);
        row.add("Đã hủy: " + ladingDenied);
        resp.put("dataSearch", row);

        List<ReportLading> reportAll = reportLadingRepository.find(new Query(new Criteria()).with(new Sort(Sort.Direction.ASC, "time")));
        ladingCod = 0;
        ladingShiping = 0;
        ladingDelivered = 0;

        ladingDenied = 0;
        for (ReportLading reportLading : reportAll) {
            ladingShiping += reportLading.getLadingShipping();
            ladingCod += reportLading.getLadingCod();
            ladingDelivered += reportLading.getDelivered();
            ladingDenied += reportLading.getDenied();
        }
        row = new ArrayList<>();
        row.add("Vận đơn vận chuyển: " + ladingShiping);
        row.add("Vận đơn COD: " + ladingCod);
        //row.add("Chưa giao hàng: " + ladingStocking);
        //row.add("Hàng đang đi trên đường: " + ladingDelivering);
        row.add("Hàng đã tới tay người mua thành công: " + ladingDelivered);
        row.add("Đã hủy: " + ladingDenied);
        resp.put("dataNow", row);
        return new Response(true, "Thống kê dữ liệu vận đơn", resp);
    }

    public Response findDataCash(ReportSearch search) {

        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportCash> report = reportCashRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Nạp xèng");
        row.add("Tiêu xèng");
        row.add("Kiếm xèng");
        row.add("Phạt xèng");
        row.add("Tiêu mua Uptin");
        row.add("Tiêu mua tin VIP");
        row.add("Kích hoạt chức năng đăng nhanh");
        row.add("Kích hoạt danh sách khách hàng");
        data.add(row);
        long topUp = 0;
        long useBalance = 0;
        long reward = 0;
        long upSchedule = 0;
        long vipItem = 0;
        long activeQuickBooking = 0;
        long activeCustomer = 0;
        long panaltyBlance = 0;

        long topUpQ = 0;
        long useBalanceQ = 0;
        long rewardQ = 0;
        long panaltyBlanceQ = 0;

        for (ReportCash reportCash : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportCash.getTime()));
            row.add(reportCash.getTopup());
            row.add(reportCash.getUseBalance());
            row.add(reportCash.getReward());
            row.add(reportCash.getPanaltyBlance());
            row.add(reportCash.getUpSchedule());
            row.add(reportCash.getVipItem());
            row.add(reportCash.getActiveQuickBooking());
            row.add(reportCash.getActiveCustomer());
            data.add(row);
            topUp += reportCash.getTopup();
            useBalance += reportCash.getUseBalance();
            reward += reportCash.getReward();
            upSchedule += reportCash.getUpSchedule();
            vipItem += reportCash.getVipItem();
            activeQuickBooking += reportCash.getActiveQuickBooking();
            activeCustomer += reportCash.getActiveCustomer();
            panaltyBlance += reportCash.getPanaltyBlance();
            panaltyBlanceQ += reportCash.getPanaltyBlanceQuantity();
            topUpQ += reportCash.getTopupQuantity();
            useBalanceQ += reportCash.getUseBalanceQuantity();
            rewardQ += reportCash.getRewardQuantity();
        }
        resp.put("chart", data);
        row = new ArrayList<>();

        row.add("Nạp xèng: " + this.conventLongToD(topUp) + "(" + topUpQ + " giao dịch)");
        row.add("Tiêu xèng: " + this.conventLongToD(useBalance) + "(" + useBalanceQ + " giao dịch)");
        row.add("Kiếm xèng: " + this.conventLongToD(reward) + "(" + rewardQ + " giao dịch)");
        row.add("Phạt xèng: " + this.conventLongToD(panaltyBlance) + "(" + panaltyBlanceQ + " giao dịch)");
        row.add("Tiêu mua Uptin: " + this.conventLongToD(upSchedule));
        row.add("Tiêu mua tin VIP: " + this.conventLongToD(vipItem));
        row.add("Kích hoạt chức năng đăng nhanh: " + this.conventLongToD(activeQuickBooking));
        row.add("Kích hoạt danh sách khách hàng: " + this.conventLongToD(activeCustomer));

        resp.put("dataSearch", row);
        List<ReportCash> report1 = reportCashRepository.find(new Query(new Criteria()));
        long topUpS = 0;
        long useBalanceS = 0;
        long rewardS = 0;
        long upScheduleS = 0;
        long vipItemS = 0;
        long activeQuickBookingS = 0;
        long activeCustomerS = 0;
        long panaltyBlanceS = 0;
        long topUpQS = 0;
        long useBalanceQS = 0;
        long rewardQS = 0;
        long panaltyBlanceQS = 0;

        for (ReportCash reportCash : report1) {
            topUpS += reportCash.getTopup();
            useBalanceS += reportCash.getUseBalance();
            rewardS += reportCash.getReward();
            upScheduleS += reportCash.getUpSchedule();
            vipItemS += reportCash.getVipItem();
            activeQuickBookingS += reportCash.getActiveQuickBooking();
            activeCustomerS += reportCash.getActiveCustomer();
            panaltyBlanceS += reportCash.getPanaltyBlance();
            panaltyBlanceQS += reportCash.getPanaltyBlanceQuantity();
            topUpQS += reportCash.getTopupQuantity();
            useBalanceQS += reportCash.getUseBalanceQuantity();
            rewardQS += reportCash.getRewardQuantity();
        }
        row = new ArrayList<>();
        row.add("Nạp xèng: " + this.conventLongToD(topUpS) + "(" + topUpQS + " giao dịch)");
        row.add("Tiêu xèng: " + this.conventLongToD(useBalanceS) + "(" + useBalanceQS + " giao dịch)");
        row.add("Kiếm xèng: " + this.conventLongToD(rewardS) + "(" + rewardQS + " giao dịch)");
        row.add("Phạt xèng: " + this.conventLongToD(panaltyBlanceS) + "(" + panaltyBlanceQS + " giao dịch)");
        row.add("Tiêu mua Uptin: " + this.conventLongToD(upScheduleS));
        row.add("Tiêu mua tin VIP: " + this.conventLongToD(vipItemS));
        row.add("Kích hoạt chức năng đăng nhanh: " + this.conventLongToD(activeQuickBookingS));
        row.add("Kích hoạt danh sách khách hàng: " + this.conventLongToD(activeCustomerS));

        resp.put("dataNow", row);
        resp.put("timeNow", report1.get(0).getTime());
        return new Response(true, "Thống kê dữ liệu xèng", resp);
    }

    public Response findDataGMV(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        List<ReportOrder> reportOrders = reportOrderRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("GMV đặt hàng");
        row.add("GMV đặt hàng NL");
        row.add("GMV đặt hàng Cod");
        row.add("GMV thanh toán thành công");
        row.add("GMV thanh toán thành công qua NL");
        row.add("GMV thanh toán thành công qua CoD");
        row.add("GMV hàng bị trả lại");
        data.add(row);
        for (ReportOrder reportOrder : reportOrders) {
            row = new ArrayList<>();
            row.add(this.converTime(reportOrder.getTime()));
            row.add(reportOrder.getQuantity());
            row.add(reportOrder.getNlPayment());
            row.add(reportOrder.getCodPayment());
            row.add(reportOrder.getPaidStatus());
            row.add(reportOrder.getFinalPricePaidStatusNL());
            row.add(reportOrder.getFinalPricePaidStatusCOD());
            row.add(reportOrder.getFinalPriceReturn());
            data.add(row);
        }
        resp.put("chart", data);
        Map<String, Long> reportSumGMV = reportOrderRepository.reportSumGMV(cri);
        Map<String, Long> reportSumGMVTime = reportOrderRepository.reportSumGMVTime(cri);
        resp.put("dataGMV", reportSumGMV);
        resp.put("dataGMVTime", reportSumGMVTime);
        return new Response(true, "Thống kê dữ liệu GMV", resp);
    }

    public String conventLongToD(long number) {
        String s = Long.toString(number);
        return TextUtils.numberFormat(Double.parseDouble(s));
    }

    public long countUserSCIntegrated() {
        Criteria cri = new Criteria();
        cri.and("scIntegrated").is(true);
        return sellerRepository.count(new Query(cri));
    }

    public long countUserNLIntegrated() {
        Criteria cri = new Criteria();
        cri.and("nlIntegrated").is(true);
        return sellerRepository.count(new Query(cri));
    }

    public long countUserNoActive(boolean all) {
        Criteria cri = new Criteria();
        cri.and("active").is(false);
        if (!all) {
            cri.and("joinTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        }
        return userRepository.count(new Query(cri));
    }

    public long countLockUser(boolean all) {
        Criteria cri = new Criteria("active").is(false);
        if (!all) {
            cri.and("updateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
//            cri.orOperator(new Criteria("endTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true)));
        }
        return userRepository.count(new Query(cri));
    }

    public long countTotalSoldBuyer(boolean all) {
        Criteria cri = new Criteria();
        List<String> userIds = new ArrayList<>();
        Criteria cri1 = new Criteria();
        if (!all) {
            cri1.and("paidTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        }
        cri1.and("paymentStatus").is(PaymentStatus.PAID.toString());
        Criteria cri2 = new Criteria();
        if (!all) {
            cri2.and("shipmentUpdateTime").gte(getTime(System.currentTimeMillis(), false)).lte(getTime(System.currentTimeMillis(), true));
        }
        cri2.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        cri.orOperator(cri1, cri2);
        List<Order> orders = orderRepository.find(new Query(cri));
        for (Order order : orders) {
            if (!userIds.contains(order.getBuyerPhone())) {
                userIds.add(order.getBuyerPhone());
            }
        }
        return userIds.size();
    }

    public long countBuyerUnique(boolean all) {
        return orderService.countBuyerUnique(all);
    }

    public long countBuyerOnce(boolean all) {
        return orderService.countBuyerOnce(all);
    }

    public long countBuyerRefund(boolean all) {
        return orderService.countBuyerReturn(all);
    }

    public long countBuyerSCIntegrated(boolean all) {
        return orderService.countBuyerSCIntegrated(all);
    }

    public Response findDataBuyer(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportBuyer> report = reportBuyerRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        List<ReportBuyer> reportTotal = reportBuyerRepository.find(new Query().with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Người mua ");//buyer
        row.add("Người mua lần đầu"); //buyerOnce
        row.add("Người mua trả hàng"); // buyerReturn
        row.add("Người mua tích hợp ShipChung"); //buyerShipChungIntegrated 
        row.add("Tổng số người mua mua thành công"); //buyerShipChungIntegrated 
        data.add(row);
        long totalBuyer = 0, totalBuyerReturn = 0, totalBuyerSC = 0, totalSoldBuyers = 0, totalSoldBuyersT = 0;
        for (ReportBuyer reportBuyer : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportBuyer.getTime()));
            row.add(reportBuyer.getBuyer());
            row.add(reportBuyer.getBuyerOnce());
            row.add(reportBuyer.getBuyerReturn());
            row.add(reportBuyer.getBuyerSCIntegrated());
            row.add(reportBuyer.getSoldBuyer());
            data.add(row);
        }
        for (ReportBuyer reportBuyer : reportTotal) {
            totalSoldBuyersT += reportBuyer.getSoldBuyer();
        }
        resp.put("chart", data);
        row = new ArrayList<>();
        row.add("Tổng người mua: " + TextUtils.formatNumber(getTotalBuyer(search.getStartTime(), search.getEndTime())));
        row.add("Tổng người mua mới: " + TextUtils.formatNumber(getTotalFirstPhone(search.getStartTime(), search.getEndTime())));
        row.add("Tổng người mua trả hàng: " + TextUtils.formatNumber(getTotalBuyerReturn(search.getStartTime(), search.getEndTime())));
        row.add("Tổng số người mua tích hợp ShipChung: " + TextUtils.formatNumber(getTotalBuyerSC(search.getStartTime(), search.getEndTime())));
        row.add("Tổng số người mua mua hàng thành công: " + TextUtils.formatNumber(getTotalSoldBuyers(search.getStartTime(), search.getEndTime())));
        resp.put("dataRow", row);
        //
        row = new ArrayList<>();
        row.add("Tổng người mua: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalBuyerUnique()));
        row.add("Tổng người mua trả hàng: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalBuyerReturn()));
        row.add("Tổng số người mua tích hợp ShipChung: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalBuyerSCIntegrated()));
        row.add("Tổng số người mua mua hàng thành công: " + TextUtils.formatNumber(totalSoldBuyersT));
        resp.put("dataRowTotal", row);
        return new Response(true, "Thống kê dữ liệu người dùng", resp);

    }

    @Async
    public void runReportBuyer() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportBuyer reportBuyer = reportBuyerRepository.find(time);
        if (reportBuyer == null) {
            reportBuyer = new ReportBuyer();
            reportBuyer.setCreateTime(System.currentTimeMillis());
            reportBuyer.setTime(time);
        }
        //count newbie
        reportBuyer.setUpdateTime(System.currentTimeMillis());
        reportBuyer.setBuyer(this.countBuyerUnique(false));
        reportBuyer.setBuyerOnce(this.countBuyerOnce(false));
        reportBuyer.setBuyerReturn(this.countBuyerRefund(false));
        reportBuyer.setBuyerSCIntegrated(this.countBuyerSCIntegrated(false));
        reportBuyer.setTotalBuyerUnique(this.countBuyerUnique(true));
        reportBuyer.setTotalBuyerOnce(this.countBuyerOnce(true));
        reportBuyer.setTotalBuyerReturn(this.countBuyerRefund(true));
        reportBuyer.setTotalBuyerSCIntegrated(this.countBuyerSCIntegrated(true));
        reportBuyer.setSoldBuyer(this.countTotalSoldBuyer(false));
        reportBuyer.setTotalSoldBuyer(this.countTotalSoldBuyer(true));
        reportBuyerRepository.save(reportBuyer);
    }

    @Async
    public void runReportBuyer(long startTime) {
        long time = this.getTime(startTime, false);
        ReportBuyer reportBuyer = reportBuyerRepository.find(time);
        if (reportBuyer == null) {
            reportBuyer = new ReportBuyer();
            reportBuyer.setCreateTime(startTime);
            reportBuyer.setTime(time);
        }
        //count newbie
        reportBuyer.setUpdateTime(startTime);
        reportBuyer.setBuyer(this.countBuyerUnique(false));
        reportBuyer.setBuyerOnce(this.countBuyerOnce(false));
        reportBuyer.setBuyerReturn(this.countBuyerRefund(false));
        reportBuyer.setBuyerSCIntegrated(this.countBuyerSCIntegrated(false));
        reportBuyer.setTotalBuyerUnique(this.countBuyerUnique(true));
        reportBuyer.setTotalBuyerOnce(this.countBuyerOnce(true));
        reportBuyer.setTotalBuyerReturn(this.countBuyerRefund(true));
        reportBuyer.setTotalBuyerSCIntegrated(this.countBuyerSCIntegrated(true));
        reportBuyerRepository.save(reportBuyer);
    }

    public Response findDataSeller(ReportSearch search) {
        HashMap<String, Object> resp = new HashMap<String, Object>();
        //build query
        Criteria cri = this.buildSearch(search);
        //find data
        List<ReportSeller> report = reportSellerRepository.find(new Query(cri).with(new Sort(Sort.Direction.ASC, "time")));
        //create data report
        List<Object> data = new ArrayList<>();
        List<Object> row = new ArrayList<>();
        row.add("Ngày/Tháng");
        row.add("Người bán mới ");//newSeller
        row.add("Người bán thành công "); //sucesSeller
        row.add("Người bán lần đầu thành công "); // firstSucesSeller
        row.add("Người bán bị trả hàng "); // returnSeller
//        row.add("Người bán tích hợp ShipChung"); // scSeller
        data.add(row);
        long totalNewSeller = 0, totalSuccessSeller = 0, totalReturnSeller = 0, totalOrderedSeller = 0;
        for (ReportSeller reportSeller : report) {
            row = new ArrayList<>();
            row.add(this.converTime(reportSeller.getTime()));
            row.add(reportSeller.getNewSeller());
            totalNewSeller += reportSeller.getNewSeller();
            row.add(reportSeller.getSucesSeller());
            row.add(reportSeller.getFirstSucesSeller());
            row.add(reportSeller.getReturnSeller());
            data.add(row);
        }
        totalSuccessSeller = getTotalSuccessSeller(search.getStartTime(), search.getEndTime());
        totalReturnSeller = getTotalReturnedSeller(search.getStartTime(), search.getEndTime());
        totalOrderedSeller = getTotalOrderedSeller(search.getStartTime(), search.getEndTime());
        resp.put("chart", data);
        row = new ArrayList<>();
        row.add("Tổng người bán mới: " + TextUtils.formatNumber(totalNewSeller));
        row.add("Tổng người bán thành công: " + TextUtils.formatNumber(totalSuccessSeller));
        row.add("Tổng người bán bị trả hàng: " + TextUtils.formatNumber(totalReturnSeller));
        row.add("Tổng người bán phát sinh đơn hàng: " + TextUtils.formatNumber(totalOrderedSeller));
        resp.put("dataRow", row);
        //
        row = new ArrayList<>();
        row.add("Tổng người bán mới: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalNewSeller()));
        row.add("Tổng người bán thành công: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalSucesSeller()));
        row.add("Tổng người bán bị trả hàng: " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalReturnSeller()));
        row.add("Tổng số người bán tích hợp ShipChung (toàn thời gian): " + TextUtils.formatNumber(report.get(report.size() - 1).getTotalSCSeller()));
        resp.put("dataRowTotal", row);
        return new Response(true, "Thống kê dữ liệu người dùng", resp);
    }

    @Async
    public void runReportSeller() {
        long time = this.getTime(System.currentTimeMillis(), false);
        ReportSeller reportSeller = reportSellerRepository.find(time);
        if (reportSeller == null) {
            reportSeller = new ReportSeller();
            reportSeller.setCreateTime(System.currentTimeMillis());
            reportSeller.setTime(time);
        }
        reportSeller.setUpdateTime(System.currentTimeMillis());
        reportSeller.setNewSeller(this.countNewSeller(false));
        reportSeller.setSucesSeller(this.countSuccessSeller(false));
        reportSeller.setFirstSucesSeller(this.countFirstSuccessSeller(false));
        reportSeller.setReturnSeller(this.countReturnedSeller(false));
        reportSeller.setScSeller(this.countSCSeller(false));
        reportSeller.setTotalNewSeller(this.countNewSeller(true));
        reportSeller.setTotalSucesSeller(this.countSuccessSeller(true));
        reportSeller.setTotalFirstSucesSeller(this.countFirstSuccessSeller(true));
        reportSeller.setTotalReturnSeller(this.countReturnedSeller(true));
        reportSeller.setTotalSCSeller(this.countSCSeller(true));
        reportSellerRepository.save(reportSeller);
    }

    /**
     * Tính số người bán mới (những người chưa thực hiện có order nào trước đó)
     *
     * @param all
     * @return
     */
    private long countNewSeller(boolean all) {
        return itemService.countNewSeller(all);
    }

    private long countNewSeller(boolean all, long time) {
        return itemService.countNewSeller(all, time);
    }

    private long countSuccessSeller(boolean all) {
        return orderService.countSuccessSeller(all);
    }

    private long countSuccessSeller(boolean all, long time) {
        return orderService.countSuccessSeller(all, time);
    }

    private long countFirstSuccessSeller(boolean all) {
        return orderService.countFirstSuccessSeller(all);
    }

    private long countFirstSuccessSeller(boolean all, long time) {
        return orderService.countFirstSuccessSeller(all, time);
    }

    private long countSCSeller(boolean all) {
        Criteria cri = new Criteria();
        if (!all) {
        }
        cri.and("scIntegrated").is(true);
        return sellerRepository.count(new Query(cri));
    }

    private long countReturnedSeller(boolean all) {
        return orderService.countReturnedSeller(all);
    }

    private long countReturnedSeller(boolean all, long time) {
        return orderService.countReturnedSeller(all, time);
    }

    @Async
    public void runReportSeller(long startTime) {
        long time = this.getTime(startTime, false);
        ReportSeller reportSeller = reportSellerRepository.find(time);
        if (reportSeller == null) {
            reportSeller = new ReportSeller();
            reportSeller.setCreateTime(startTime);
            reportSeller.setTime(time);
        }
        reportSeller.setUpdateTime(startTime);
        reportSeller.setNewSeller(this.countNewSeller(false, startTime));
        reportSeller.setSucesSeller(this.countSuccessSeller(false, startTime));
        reportSeller.setFirstSucesSeller(this.countFirstSuccessSeller(false, startTime));
        reportSeller.setReturnSeller(this.countReturnedSeller(false, startTime));
        reportSeller.setScSeller(this.countSCSeller(false));
        reportSeller.setTotalNewSeller(this.countNewSeller(true, startTime));
        reportSeller.setTotalSucesSeller(this.countSuccessSeller(true, startTime));
        reportSeller.setTotalFirstSucesSeller(this.countFirstSuccessSeller(true, startTime));
        reportSeller.setTotalReturnSeller(this.countReturnedSeller(true, startTime));
        reportSeller.setTotalSCSeller(this.countSCSeller(true));
        reportSellerRepository.save(reportSeller);
    }

    @Async
    public void runReportMonth(String reportType, int numDay) {
        long stepTime = 86400000;
        long startTime = System.currentTimeMillis() - (numDay * stepTime);
        for (int i = 0; i < numDay; i++) {
            startTime += stepTime * i;
            if (reportType.equals("seller")) {
                runReportSeller(startTime);
            } else if (reportType.equals("buyer")) {
                runReportSeller(startTime);
            } else if (reportType.equals("shop")) {
                runReportSeller(startTime);
            }
        }
    }

    public long getTotalSuccessSeller(long startTime, long endTime) {
        return orderService.countTotalSuccessSeller(startTime, endTime);
    }

    public long getTotalReturnedSeller(long startTime, long endTime) {
        return orderService.countTotalReturnedSeller(startTime, endTime);
    }

    public long getTotalOrderedSeller(long startTime, long endTime) {
        return orderService.countTotalOrderedSeller(startTime, endTime);
    }

    public long getTotalBuyer(long startTime, long endTime) {
        return orderService.countBuyerUnique(startTime, endTime);
    }
    
    public long getTotalFirstPhone(long startTime, long endTime) {
        return orderRepository.countBuyerFirstPhone(startTime, endTime);
    }

    public long getTotalBuyerReturn(long startTime, long endTime) {
        return orderService.getTotalBuyerReturn(startTime, endTime);
    }

    public long getTotalBuyerSC(long startTime, long endTime) {
        return orderService.getTotalBuyerSC(startTime, endTime);
    }

    public long getTotalSoldBuyers(long startTime, long endTime) {
        return orderService.getTotalSoldBuyers(startTime, endTime);
    }

    public List<Seller> getNewSeller(long startTime, long endTime) {
        List<String> listNewSeller = itemRepository.listNewSeller(startTime, endTime);
        if (!listNewSeller.isEmpty()) {
            List<Seller> result = sellerRepository.get(listNewSeller);
            return result;
        }
        return null;
    }
    public Workbook downloadNewSeller(ReportSearch search){
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("New-Seller-CDT");
        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("UserId CĐT");
        row.createCell(1).setCellValue("Email Chợ");
        row.createCell(2).setCellValue("Phone");
        row.createCell(3).setCellValue("Username");
        int i = 1;
        List<Seller> sellers = getNewSeller(search.getStartTime(), search.getEndTime());
        List<String> userIds = new ArrayList<>();
        for (Seller seller : sellers) {
                userIds.add(seller.getUserId());
        }
        List<User> users = userRepository.find(new Query(new Criteria("_id").in(userIds).and("active").is(true)));
        for (Seller seller : sellers) {
            for (User user : users) {
                if (user.getId().equals(seller.getUserId())) {
                    i++;
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue(seller.getUserId());
                    row.createCell(1).setCellValue(user.getEmail());
                    row.createCell(2).setCellValue(user.getPhone());
                    row.createCell(3).setCellValue(user.getName());
                }
            }
        }
        return wb;
    }
    
    

}
