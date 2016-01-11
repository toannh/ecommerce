package vn.chodientu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.report.ReportBuyer;
import vn.chodientu.entity.db.report.ReportCash;
import vn.chodientu.entity.db.report.ReportItem;
import vn.chodientu.entity.db.report.ReportLading;
import vn.chodientu.entity.db.report.ReportOrder;
import vn.chodientu.entity.db.report.ReportShop;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.ItemSource;
import vn.chodientu.entity.enu.ListingType;
import vn.chodientu.entity.enu.PaymentMethod;
import vn.chodientu.entity.enu.PaymentStatus;
import vn.chodientu.entity.enu.ShipmentStatus;
import vn.chodientu.entity.input.ItemSearch;
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
import vn.chodientu.repository.ReportShopRepository;
import vn.chodientu.repository.SellerRepository;
import vn.chodientu.repository.ShopRepository;
import vn.chodientu.repository.UserRepository;

@Service
public class ReportTestService {

    @Autowired
    private ReportOrderRepository reportOrderRepository;
    @Autowired
    private ReportLadingRepository reportLadingRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private LadingRepository ladingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;
    @Autowired
    private ReportCashRepository reportCashRepository;
    @Autowired
    private ReportItemRepository reportItemRepository;
    @Autowired
    private ReportBuyerRepository reportBuyerRepository;
    @Autowired
    private ReportShopRepository reportShopRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    private long getTime(long time, boolean endday) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            cal.setTime(new Date(time));
            long time1 = sdfTime.parse(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + (endday ? " 23:59" : " 00:00")).getTime();
            if (endday) {
                time1 += 59000;
            }
            return time1;
        } catch (Exception e) {
            return 0;
        }
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

    @Async
    public void runReportShopTest(int from, int to, int month) {
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);
            ReportShop reportShop = reportShopRepository.find(time);
            if (reportShop == null) {
                reportShop = new ReportShop();
                reportShop.setCreateTime(time);
                reportShop.setTime(time);
            }
            reportShop.setUpdateTime(System.currentTimeMillis());
            reportShop.setShop(shopRepository.count(new Query(new Criteria())));
            Criteria cri = new Criteria();
            long time1 = getTime(reportShop.getCreateTime(), true);
            cri.and("createTime").gte(getTime(reportShop.getCreateTime(), false)).lte(getTime(reportShop.getCreateTime(), true));
            reportShop.setNewshop(shopRepository.count(new Query(cri)));
            if (i == to) {
                reportShop.setLockedShop(this.countLockedShop());
            }
            reportShopRepository.save(reportShop);
        }
    }

    @Async
    public void runReportOderTest(int from, int to, int month) {
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);
            ReportOrder reportOrder = reportOrderRepository.find(time);
            if (reportOrder == null) {
                reportOrder = new ReportOrder();
                reportOrder.setCreateTime(time);
                reportOrder.setTime(time);
            }
            reportOrder.setUpdateTime(time);
            this.cOrderStatusTest(reportOrder);
            this.cOrderMethodTest(reportOrder);
            this.cOrderPaidGMVTest(reportOrder);
            this.cOrderTest(reportOrder);
            reportOrderRepository.save(reportOrder);
        }
    }

    @Async
    public void runReportCashTest(int from, int to, int month) {
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);
            ReportCash report = reportCashRepository.find(time);
            if (report == null) {
                report = new ReportCash();
                report.setCreateTime(time);
                report.setTime(time);
            }
            report.setUpdateTime(time);
            this.cCashTest(report);
            reportCashRepository.save(report);
        }
    }

    @Async
    public void runReportLadingTest(int from, int to, int month) {
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);
            ReportLading report = reportLadingRepository.find(time);
            if (report == null) {
                report = new ReportLading();
                report.setCreateTime(time);
                report.setTime(time);
            }
            report.setUpdateTime(time);
            this.cLadingTest(report);
            reportLadingRepository.save(report);
        }
    }

    @Async
    public void runReportItem(int from, int to, int month) {
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);
            ReportItem report = reportItemRepository.find(time);
            if (report == null) {
                report = new ReportItem();
                report.setCreateTime(time);
                report.setTime(time);
            }
            report.setUpdateTime(time);
            this.cItemSourceTest(report);
            reportItemRepository.save(report);
        }
    }

    private void cItemSourceTest(ReportItem report) {
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("source").is(ItemSource.SELLER.toString());
        report.setSeller(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("source").is(ItemSource.API.toString());
        report.setApi(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("source").is(ItemSource.CRAWL.toString());
        report.setCrawl(itemRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("active").is(true).and("completed").is(true).and("approved").is(false);
        report.setUnapproved(itemRepository.count(new Query(cri)));
        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
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
        //long count = itemRepository.count(new Query(cri));
        Map<String, Long> itemStatusHistogramReportByTime = itemService.getItemStatusHistogramReportByTime(getTime(report.getCreateTime(), false), getTime(report.getCreateTime(), true));

        report.setLiveListing(itemStatusHistogramReportByTime.get("total"));

        //Số lượng Người bán có tin bán:Number of listers
        report.setNumberSellerLister(itemRepository.getDistincUser(getTime(report.getCreateTime(), false), getTime(report.getCreateTime(), true)));

        long time = this.getTime(report.getCreateTime(), false);
        ReportBuyer reportBuyer = reportBuyerRepository.find(time);
        if (reportBuyer == null) {
            reportBuyer = new ReportBuyer();
            reportBuyer.setCreateTime(System.currentTimeMillis());
            reportBuyer.setTime(time);
        }
        //count newbie
        reportBuyer.setUpdateTime(System.currentTimeMillis());
        reportBuyer.setSoldBuyer(this.countTotalSoldBuyer(false, report.getCreateTime()));
        reportBuyer.setTotalSoldBuyer(this.countTotalSoldBuyer(true, report.getCreateTime()));
        reportBuyerRepository.save(reportBuyer);
    }

    private void cOrderStatusTest(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
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
        c1.and("paidTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        Criteria c2 = new Criteria("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        c2.and("shipmentUpdateTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.orOperator(c1, c2);
        reportOrder.setPaidStatus(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePaidStatus(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePaidStatus(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.and("paymentStatus").is(PaymentStatus.PENDING.toString());
        reportOrder.setPadingStatus(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPricePadingStatus(countPrice.get("finalPrice"));
            reportOrder.setTotalPricePadingStatus(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    private void cOrderMethodTest(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.and("paymentMethod").is(PaymentMethod.NONE.toString());
        reportOrder.setNonePayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceNonePayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceNonePayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.and("paymentMethod").is(PaymentMethod.COD.toString());
        reportOrder.setCodPayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceCodPayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceCodPayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

        cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
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
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.andOperator(new Criteria("paymentMethod").is(PaymentMethod.VISA.toString()), new Criteria("paymentMethod").is(PaymentMethod.MASTER.toString()));
        reportOrder.setVisaPayment(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceVisaPayment(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceVisaPayment(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    private void cOrderPaidGMVTest(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("paidTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
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
        cri.and("shipmentUpdateTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
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
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        cri.and("refundStatus").is(true);
        reportOrder.setOrderReturn(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPriceReturn(countPrice.get("finalPrice"));
            reportOrder.setTotalPriceReturn(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }

    }

    private void cOrderTest(ReportOrder reportOrder) {
        Map<String, Long> countPrice = null;
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(reportOrder.getCreateTime(), false)).lte(getTime(reportOrder.getCreateTime(), true));
        reportOrder.setQuantity(orderRepository.count(new Query(cri)));
        try {
            countPrice = orderRepository.sumPrice(cri);
            reportOrder.setFinalPrice(countPrice.get("finalPrice"));
            reportOrder.setTotalPrice(countPrice.get("totalPrice"));
        } catch (Exception e) {
        }
    }

    public Response cOrderTests(int from, int to, int month) {
        List<Order> orderxxx = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, i);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
            }
            long time = this.getTime(cal.getTime().getTime(), false);

            Criteria cri = new Criteria();

            cri.and("createTime").gte(getTime(time, false)).lte(getTime(time, true));
            List<Order> orders = orderRepository.find(new Query(cri));
            orderxxx.addAll(orders);
        }
        Criteria c1 = new Criteria();
        c1.and("createTime").gte(1406826000000l).lte(1408121940000l);

        List<Order> ordersc1 = orderRepository.find(new Query(c1));
        List<String> lists = new ArrayList<>();
        List<String> listsCheck = new ArrayList<>();

        for (Order list : orderxxx) {
            if (!lists.contains(list.getId())) {
                lists.add(list.getId());
            }
        }
        for (Order list : ordersc1) {
            if (!lists.contains(list.getId())) {
                listsCheck.add(list.getId());
            }
        }
        return new Response(true, "info", listsCheck);
    }

    private void cCashTest(ReportCash reportCash) {
        Criteria criteria = new Criteria();
        criteria.and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true));
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
        criteria.and("nlPayTime").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true));
        reportCash.setTopup(cashTransactionRepository.sumForReport(criteria));
        reportCash.setTopupQuantity(cashTransactionRepository.count(new Query(criteria)));

        criteria = new Criteria();
        criteria.and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true));
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
        criteria.and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true));
        criteria.and("type").is(CashTransactionType.PANALTY_BLANCE.toString());
        reportCash.setPanaltyBlance(cashTransactionRepository.sumForReport(criteria));
        reportCash.setPanaltyBlanceQuantity(cashTransactionRepository.count(new Query(criteria)));

        reportCash.setUpSchedule(cashTransactionRepository.sumForReport(new Criteria().and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true)).orOperator(new Criteria("type").is(CashTransactionType.SPENT_UPITEM.toString()))));
        reportCash.setVipItem(cashTransactionRepository.sumForReport(new Criteria().and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true)).orOperator(new Criteria("type").is(CashTransactionType.SPENT_VIPITEM.toString()))));
        reportCash.setActiveCustomer(cashTransactionRepository.count(new Query(new Criteria().and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true)).orOperator(new Criteria("type").is(CashTransactionType.ACTIVE_MARKETING.toString())))));
        reportCash.setActiveQuickBooking(cashTransactionRepository.count(new Query(new Criteria().and("time").gte(getTime(reportCash.getCreateTime(), false)).lte(getTime(reportCash.getCreateTime(), true)).orOperator(new Criteria("type").is(CashTransactionType.ACTIVE_QUICK_SUBMIT.toString())))));
    }

    private void cLadingTest(ReportLading report) {
        Criteria cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        report.setLading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.NEW.toString());
        report.setNewlading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("shipmentUpdateTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        report.setDelivered(orderRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DELIVERING.toString());
        report.setDelivering(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.DENIED.toString());
        report.setDenied(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.RETURN.toString());
        report.setReturnLading(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("createTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("shipmentStatus").is(ShipmentStatus.STOCKING.toString());
        report.setStocking(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("type").is(PaymentMethod.COD.toString());
        report.setLadingCod(ladingRepository.count(new Query(cri)));

        cri = new Criteria();
        cri.and("updateTime").gte(getTime(report.getCreateTime(), false)).lte(getTime(report.getCreateTime(), true));
        cri.and("type").is(PaymentMethod.NONE.toString());
        report.setLadingShipping(ladingRepository.count(new Query(cri)));
    }

    public long countTotalSoldBuyer(boolean all, long time) {
        Criteria cri = new Criteria();
        List<String> userIds = new ArrayList<>();
        Criteria cri1 = new Criteria();
        if (!all) {
            cri1.and("paidTime").gte(getTime(time, false)).lte(getTime(time, true));
        }
        cri1.and("paymentStatus").is(PaymentStatus.PAID.toString());
        Criteria cri2 = new Criteria();
        if (!all) {
            cri2.and("shipmentUpdateTime").gte(getTime(time, false)).lte(getTime(time, true));
        }
        cri2.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        cri.orOperator(cri1, cri2);
        List<Order> orders = orderRepository.find(new Query(cri));
        for (Order order : orders) {
            if (!userIds.contains(order.getBuyerEmail())) {
                userIds.add(order.getBuyerEmail());
            }
        }
        return userIds.size();
    }

}
