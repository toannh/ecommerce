package vn.chodientu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.component.TopUpClient;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.CashTransaction;
import vn.chodientu.entity.db.TopUp;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.input.TopUpSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.CashRepository;
import vn.chodientu.repository.CashTransactionRepository;
import vn.chodientu.repository.TopUpRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.util.ExpiringMap;
import vn.chodientu.util.TextUtils;

@Service
public class TopUpService {

    @Autowired
    private TopUpRepository topUpRepository;
    @Autowired
    private TopUpClient topUpClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;
    @Autowired
    private SmsService smsService;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private Viewer viewer;

    /**
     * Nạp trực tiếp vào điện thoại
     *
     * @param phone
     * @param userId
     * @param amount
     * @return
     * @throws Exception
     */
    public TopUp topupTelco(String phone, String userId, TopUpClient.Amount amount) throws Exception {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new Exception("Không tìm thấy tài khoản trên hệ thống");
        }
//        if (user.getPhone() == null || user.getPhone().isEmpty()) {
//            throw new Exception("Tài khoản của bạn chưa có số điện thoại");
//        }
//        if (!user.isPhoneVerified()) {
//            throw new Exception("Số điện thoại " + user.getPhone() + " chưa được xác thực");
//        }
        if (!TextUtils.validatePhoneNumber(phone)) {
            throw new Exception("Số điện thoại không hợp lệ");
        }
        long nAmount = this.getAmount(amount);
        Criteria criteria = new Criteria();
        criteria.and("createTime").gt(TextUtils.firstDayOfMonth(System.currentTimeMillis())).lte(TextUtils.lastDayOfMonth(System.currentTimeMillis()));
        criteria.and("userId").is(userId);
        criteria.and("success").is(true);
        if (viewer.getTopup() != null && !viewer.getTopup().equals("")) {
            throw new Exception("Hệ thống đang xử lý bạn vui lòng đợi trong giây lát..");
        }
        long tAmount = topUpRepository.totalAmount(criteria);
        if (tAmount + nAmount > 200000) {
            throw new Exception("Bạn chỉ được đổi tối đa 200.000 vnđ trong một tháng");
        }
        Cash cash = cashRepository.getCash(userId, nAmount);
        TopUp topupTelco=null;
        if (cash == null) {
            throw new Exception("Số xèng trong tài khoản không đủ để thực hiện giao dịch này");
        }
        if (cash.getBalance() < 0) {
            //cong lai
            this.revertBlance(userId, amount);
            throw new Exception("Số xèng trong tài khoản không đủ để thực hiện giao dịch này");
        }
        this.createblance(userId, amount, cash.getBalance());
        //this.blance(userId, amount);
        viewer.setTopup("run");
         topupTelco = topUpClient.topupTelco(userId, phone, amount);
        if (topupTelco.isSuccess()) {
            //this.blance(userId, amount);
            this.sendAction(topupTelco);
            viewer.setTopup(null);
        } else {
            this.revertBlance(userId, amount);
            viewer.setTopup(null);
        }
        return topupTelco;

    }

    /**
     * Lấy mã thẻ điện thoại
     *
     * @param userId
     * @param amount
     * @param service
     * @return
     * @throws Exception
     */
    public TopUp buyCardTelco(String userId, TopUpClient.Amount amount, TopUpClient.Service service) throws Exception {
        User user = userRepository.find(userId);
        if (user == null) {
            throw new Exception("Không tìm thấy tài khoản trên hệ thống");
        }
        long nAmount = this.getAmount(amount);
        Criteria criteria = new Criteria();
        criteria.and("createTime").gte(TextUtils.firstDayOfMonth(System.currentTimeMillis())).lte(TextUtils.lastDayOfMonth(System.currentTimeMillis()));
        criteria.and("success").is(true);
        criteria.and("userId").is(userId);
        long tAmount = topUpRepository.totalAmount(criteria);
        if (tAmount + nAmount > 200000) {
            throw new Exception("Bạn chỉ được đổi tối đa 200.000 vnđ trong một tháng");
        }
        Cash cash = cashRepository.getCash(userId, nAmount);
        TopUp buyCardTelco = null;
        if (cash == null) {
            throw new Exception("Số xèng trong tài khoản không đủ để thực hiện giao dịch này");
        }
        if (cash.getBalance() < 0) {
            //cong lai
            this.revertBlance(userId, amount);
            throw new Exception("Số xèng trong tài khoản không đủ để thực hiện giao dịch này");
        }
        this.createblance(userId, amount, cash.getBalance());
        //viewer.setTopup("run");
        buyCardTelco = topUpClient.buyCardTelco(userId, amount, service);
        if (buyCardTelco.isSuccess()) {
            this.sendAction(buyCardTelco);
            //this.blance(userId, amount);
            //viewer.setTopup(null);
        } else {
            this.revertBlance(userId, amount);
            //viewer.setTopup(null);
        }

        return buyCardTelco;
    }

    private long getAmount(TopUpClient.Amount card) {
        long amount = 10000;
        try {
            String[] amounts = card.toString().split("_");
            amount = Long.parseLong(amounts[1]);
        } catch (Exception e) {
        }
        return amount;
    }

    /**
     * Tính toán cộng xèng
     *
     * @param userId
     * @param card
     * @throws Exception
     */
    private long blance(String userId, TopUpClient.Amount card) throws Exception {
        if (card == null) {
            throw new Exception("Bạn cần chọn mệnh giá thẻ");
        }
        long amount = this.getAmount(card);
        Cash cash = cashRepository.getCash(userId);
        if (cash.getBalance() < amount * 2) {
            throw new Exception("Số xèng trong tài khoản không đủ để thực hiện giao dịch này");
        }

        CashTransaction cashTransaction = new CashTransaction();
        cashTransaction.setId(cashTransactionRepository.genId());
        cashTransaction.setTime(System.currentTimeMillis());
        cashTransaction.setType(CashTransactionType.TOP_UP);
        cashTransaction.setSpentQuantity(1);
        cashTransaction.setUserId(userId);
        cashTransaction.setAmount(amount * 2);
        long monney = cashTransaction.getAmount() * cashTransaction.getSpentQuantity() * -1;
        Cash topupPaymentDone = cashRepository.topupPaymentDone(cashTransaction.getUserId(), monney);
        cashTransaction.setNewBalance(topupPaymentDone.getBalance());
        cashTransactionRepository.save(cashTransaction);
        return amount;
    }

    private long createblance(String userId, TopUpClient.Amount card, long balance) throws Exception {
        if (card == null) {
            throw new Exception("Bạn cần chọn mệnh giá thẻ");
        }
        long amount = this.getAmount(card);
        Cash cash = cashRepository.getCash(userId);

        CashTransaction cashTransaction = new CashTransaction();
        cashTransaction.setId(cashTransactionRepository.genId());
        cashTransaction.setTime(System.currentTimeMillis());
        cashTransaction.setType(CashTransactionType.TOP_UP);
        cashTransaction.setSpentQuantity(1);
        cashTransaction.setUserId(userId);
        cashTransaction.setAmount(amount * 2);
        cashTransaction.setNewBalance(balance);
        cashTransactionRepository.save(cashTransaction);
        return amount;
    }

    private long revertBlance(String userId, TopUpClient.Amount card) throws Exception {
        long amount = this.getAmount(card);
        CashTransaction cashTransaction = new CashTransaction();
        cashTransaction.setId(cashTransactionRepository.genId());
        cashTransaction.setTime(System.currentTimeMillis());
        cashTransaction.setType(CashTransactionType.TOP_UP_REVERT);
        cashTransaction.setSpentQuantity(1);
        cashTransaction.setUserId(userId);
        cashTransaction.setAmount(amount * 2);
        long monney = cashTransaction.getAmount() * cashTransaction.getSpentQuantity();
        Cash topupPaymentDone = cashRepository.topupPaymentDone(cashTransaction.getUserId(), monney);
        cashTransaction.setNewBalance(topupPaymentDone.getBalance());
        cashTransactionRepository.save(cashTransaction);
        return amount;
    }

    public void sendAction(TopUp topUp) {
        if (topUp.getType().equals("buyCardTelco")) {
            if (!topUp.isSendEmail()) {
                Map<String, Object> data = new HashMap<>();
                data.put("cardCode", topUp.getCardCode());
                data.put("cardSerial", topUp.getCardSerial());
                data.put("cardType", topUp.getService());
                data.put("cardValue", topUp.getCardValue());
                data.put("expiryDate", topUp.getExpiryDate());
                try {
                    User user = userService.get(topUp.getUserId());
                    data.put("username", user.getUsername() == null || user.getUsername().equals("") ? user.getEmail() : user.getUsername());
                    data.put("email", user.getEmail());
                    emailService.send(EmailOutboxType.TOPUP_CARD_TEL, user.getEmail(), "Chúc mừng bạn đã đổi thành công mã thẻ điện thoại", "buycardtel", data);
                } catch (Exception e) {
                }
            }
            if (!topUp.isSendInbox()) {
                try {
                    messageService.send(topUp.getUserId(),
                            "Chúc mừng bạn đã đổi thành công mã thẻ điện thoại",
                            "<p> Chúc mừng bạn đã đổi thành công mã thẻ điện thoại</p>"
                            + "<ul><li>- Nhà mạng: " + topUp.getService() + "</li>"
                            + "<li>- Mã thẻ: " + topUp.getCardCode() + "</li>"
                            + "<li>- Số serials: " + topUp.getCardSerial() + "</li>"
                            + "<li>- Mệnh giá: " + TextUtils.numberFormat(Double.parseDouble(topUp.getAmount() + "")) + "vnđ</li></ul>", null, null);
                    topUp.setSendInbox(true);
                } catch (Exception e) {
                }
            }
            if (!topUp.isSendReadTime()) {
                realTimeService.add("Bạn vừa đổi thành công mã thẻ điện thoại (xem chi tiết email)", topUp.getUserId(), null, "Đổi thẻ điện thoại", null);
                topUp.setSendReadTime(true);
            }
        } else {
            if (!topUp.isSendEmail()) {
                Map<String, Object> data = new HashMap<>();
                try {
                    User user = userService.get(topUp.getUserId());
                    data.put("username", user.getUsername() == null || user.getUsername().equals("") ? user.getEmail() : user.getUsername());
                    data.put("email", user.getEmail());
                    data.put("phone", topUp.getPhone());
                    data.put("amount", topUp.getAmount() + "");
                    emailService.send(EmailOutboxType.TOPUP_TEL, user.getEmail(), "Chúc mừng bạn vừa nạp thẻ thành công", "buytel", data);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (!topUp.isSendInbox()) {
                try {
                    messageService.send(topUp.getUserId(),
                            "Chúc mừng bạn vừa nạp thành công",
                            "<p>Chúc mừng bạn vừa nạp thành công " + TextUtils.numberFormat(Double.parseDouble(topUp.getAmount() + "")) + "vnđ vào số điện thoại " + topUp.getPhone() + "</p>", null, null);
                    topUp.setSendInbox(true);
                } catch (Exception e) {
                }
            }
            if (!topUp.isSendReadTime()) {
                realTimeService.add("Bạn vừa nạp tiền thành công " + TextUtils.numberFormat(Double.parseDouble(topUp.getAmount() + "")) + "vnđ vào số điện thoại " + topUp.getPhone(), topUp.getUserId(), null, "Nạp điện thoại", null);
                topUp.setSendReadTime(true);
            }
        }
    }

    public Criteria buildCriteria(TopUpSearch search) {
        Criteria cri = new Criteria();
        List<String> userIds = new ArrayList<>();
        if (search.getType() != null && !search.getType().equals("")) {
            cri.and("type").is(search.getType());
        }
        if (search.getPhone() != null && !search.getPhone().equals("")) {
            cri.and("phone").is(search.getPhone());
        }
        if (search.getRequestId() != null && !search.getRequestId().equals("")) {
            cri.and("requestId").is(search.getRequestId());
        }
        if (search.getSuccess() > 0) {
            if (search.getSuccess() == 1) {
                cri.and("success").is(true);
            } else {
                cri.and("success").is(false);
            }
        }
        if (search.getEmail() != null && !search.getEmail().equals("")) {
            User byEmail = userRepository.getByEmail(search.getEmail());
            if (byEmail != null) {
                userIds.add(byEmail.getId());
            }
        }
        if (search.getUserId() != null && !search.getUserId().equals("")) {
            userIds.add(search.getUserId());
        }
        if (userIds != null && !userIds.isEmpty()) {
            cri.and("userId").in(userIds);
        }
        if (search.getCreateTimeFrom() > 0 && search.getCreateTimeTo() > 0) {
            cri.and("createTime").gte(search.getCreateTimeFrom()).lt(search.getCreateTimeTo());
        } else if (search.getCreateTimeFrom() > 0) {
            cri.and("createTime").gte(search.getCreateTimeFrom());
        } else if (search.getCreateTimeTo() > 0) {
            cri.and("createTime").lt(search.getCreateTimeTo());
        }
        return cri;
    }

    /**
     * Tìm kiếm lịch sử quy đổi xèng
     *
     * @param search
     * @return
     */
    public DataPage<TopUp> search(TopUpSearch search) {
        Criteria buildCriteria = this.buildCriteria(search);
        Query query = new Query(buildCriteria);
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip(search.getPageIndex() * search.getPageSize()).limit(search.getPageSize());

        DataPage<TopUp> page = new DataPage<>();
        page.setPageSize(search.getPageSize());
        page.setPageIndex(search.getPageIndex());
        if (page.getPageSize() <= 0) {
            page.setPageSize(5);
        }
        page.setDataCount(topUpRepository.count(query));
        page.setPageCount(page.getDataCount() / page.getPageSize());
        if (page.getDataCount() % page.getPageSize() != 0) {
            page.setPageCount(page.getPageCount() + 1);
        }

        page.setData(topUpRepository.find(query));
        return page;
    }

    /**
     * Lấy giao dịch theo id
     *
     * @param id
     * @return
     * @throws Exception
     */
    public TopUp getById(String id) throws Exception {
        TopUp topup = topUpRepository.find(id);
        if (topup == null) {
            throw new Exception("Không tìm thấy giao dịch này!");
        }
        return topup;
    }

    /**
     * Tính tổng giá trị theo điều khiện lọc
     *
     * @param search
     * @return
     */
    public long sumPrice(TopUpSearch search) {
        Criteria buildCriteria = this.buildCriteria(search);
        return topUpRepository.totalAmount(buildCriteria);
    }
}
