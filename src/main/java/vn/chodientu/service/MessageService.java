package vn.chodientu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.EmailOutbox;
import vn.chodientu.entity.db.Message;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.input.MessageSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.EmailOutboxRepository;
import vn.chodientu.repository.MessageRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.util.TextUtils;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private Viewer viewer;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailService emailService;

    /**
     * Tìm kiếm có phân trang
     *
     * @param search
     * @return
     */
    public DataPage<Message> search(MessageSearch search) {
        DataPage<Message> dataPage = new DataPage<>();
        Sort sort;
        switch (search.getSort()) {
            case 1:
                sort = new Sort(Sort.Direction.ASC, "createTime");
                break;
            case 2:
            default:
                sort = new Sort(Sort.Direction.DESC, "updateTime");
        }
        Criteria cri = new Criteria();
        if (search.getFromEmail() != null && !search.getFromEmail().equals("")) {
            cri.and("fromEmail").is(search.getFromEmail());
        }
        if (search.getFromUserId() != null && !search.getFromUserId().equals("")) {
            cri.and("fromUserId").is(search.getFromUserId());
        }
        if (search.getToEmail() != null && !search.getToEmail().equals("")) {
            cri.and("toEmail").is(search.getToEmail());
        }
        if (search.getToUserId() != null && !search.getToUserId().equals("")) {
            cri.and("toUserId").is(search.getToUserId());
        }
        cri.and("remove").is(false);
        switch (search.getRead()) {
            case 0:
                cri.and("read").ne(null);
                break;
            case 1:
                cri.and("read").is(true);
                break;
            case 2:
                cri.and("read").is(false);
                break;
            default:

        }
        Query query = new Query(cri);
        dataPage.setDataCount(messageRepository.count(query));
        dataPage.setPageIndex(search.getPageIndex());
        dataPage.setPageSize(search.getPageSize());
        dataPage.setPageCount(dataPage.getDataCount() / search.getPageSize());
        if (dataPage.getDataCount() % search.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }
        dataPage.setData(messageRepository.find(query.limit(search.getPageSize()).skip(search.getPageIndex() * search.getPageSize()).with(sort)));
        return dataPage;
    }

    /**
     * Chi tiết tin nhắn
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    public Message read(String id) throws Exception {
        Message message = messageRepository.findMessage(id, TextUtils.getClientIpAddr(request));
        User user = userRepository.getByEmail(message.getFromEmail());
        User userTo = userRepository.getByEmail(message.getToEmail());
        if (user != null) {
            List<String> get = imageService.get(ImageType.AVATAR, user.getId());
            if (get != null && !get.isEmpty()) {
                message.setFromImage(imageService.getUrl(get.get(0)).thumbnail(32, 32, "outbound").getUrl(user.getName()));
            }
        }
        if (userTo != null) {
            List<String> gets = imageService.get(ImageType.AVATAR, userTo.getId());
            if (gets != null && !gets.isEmpty()) {
                message.setToImage(imageService.getUrl(gets.get(0)).thumbnail(32, 32, "outbound").getUrl(userTo.getName()));
            }
        }
        if (message == null) {
            throw new Exception("Không tìm thấy tin nhắn bạn yêu cầu");
        }
        return message;
    }

    /**
     * Cập nhật trạng thái chưa đọc
     *
     * @param ids
     * @return
     */
    public List<Message> markedUnread(List<String> ids) {
        return messageRepository.markedUnread(ids, TextUtils.getClientIpAddr(request));
    }

    /**
     * Xóa thông tin , remove = true là xóa khỏi databse thật, false xóa logic
     *
     * @param ids
     * @param remove
     */
    public void delete(List<String> ids, boolean remove) {
        if (!remove) {
            messageRepository.delete(ids, TextUtils.getClientIpAddr(request));
        } else {
            messageRepository.delete(ids);
        }
    }

    /**
     * Gửi tin nhắn
     *
     * @param to
     * @param subject
     * @param content
     * @param orderId
     * @param itemId
     * @return
     * @throws Exception
     */
    public Response send(String to, String subject, String content, String orderId, String itemId) throws Exception {
        if (viewer.getUser() == null) {
            throw new Exception("Bạn cần đăng nhập để thực hiện thao tác này ");
        }
        User user = userRepository.getByEmail(to);
        HashMap<String, String> error = new HashMap<>();
        String emailTo = "";
        String buyerName = "";
        if (user == null) {
//            error.put("toEmail", "Không tìm thấy địa chỉ email người nhận");
            // get email from order 
            Order order = orderService.get(orderId);
            if (order == null) {
                error.put("toEmail", "Không tìm thấy địa chỉ email người nhận.");
            } else {
                String sellerName = viewer.getUser().getName() != null ? viewer.getUser().getName() : (viewer.getUser().getUsername() != null ? viewer.getUser().getUsername() : viewer.getUser().getEmail());
                emailTo = order.getBuyerEmail();
                buyerName = order.getBuyerName();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("username", order.getBuyerName());
                data.put("message", "Người bán " + sellerName + " đã gửi một lời nhắn đến bạn trên hệ thống chợ điện tử: <br> "
                        + "Tiêu đề lời nhắn : " + subject + "<br>"
                        + "Nội dung lời nhắn : " + content + "<br>"
                        + "Mã đơn hàng : <a href='" + "http://chodientu.vn" + "/" + order.getId() + "/chi-tiet-don-hang.html' >" + order.getId().toUpperCase() + "</a>");
                try{emailService.send(EmailOutboxType.MESSAGE_TO_BYUER,
                        order.getBuyerEmail(),
                        "[Chợ điện tử] Tin nhắn từ người bán " + sellerName + "(" + order.getId() + ")",
                        "message", data);
                }catch(Exception ex){
                    return new Response(true, "Không gửi được Email tới người mua", null);
                }
//                return new Response(true, "Gửi Email thành công tới người mua", null);
            }
        } else {
            emailTo = user.getEmail();
        }
        if (subject == null || subject.equals("")) {
            error.put("subject", "Tiêu đề tin nhắn không được để trống");
        }
        if (content == null || content.equals("")) {
            error.put("content", "Nội dung tin nhắn không được để trống");
        }
        if (!error.isEmpty()) {
            return new Response(false, null, error);
        }
        Message message = new Message();
        message.setCreateTime(System.currentTimeMillis());
        message.setContent(content);
        message.setFromEmail(viewer.getUser().getEmail());
        message.setFromName(viewer.getUser().getName() != null ? viewer.getUser().getName() : (viewer.getUser().getUsername() != null ? viewer.getUser().getUsername() : viewer.getUser().getEmail()));
        message.setFromUserId(viewer.getUser().getId());
        message.setItemId(itemId);
        message.setLastIp(TextUtils.getClientIpAddr(request));
        message.setOrderId(orderId);
        message.setLastView(System.currentTimeMillis());
        message.setRead(false);
        message.setRemove(false);
        message.setSubject(subject);
        message.setToEmail(emailTo);
        if (user != null) {
            message.setToName(user.getName() != null ? user.getName() : (user.getUsername() != null ? user.getUsername() : user.getEmail()));
            message.setToUserId(user.getId());
        }else{
            message.setToName(buyerName);
            message.setToUserId(emailTo);
        }
        message.setUpdateTime(System.currentTimeMillis());
        messageRepository.save(message);

        realTimeService.add("Bạn vừa nhận được 1 tin nhắn từ " + message.getFromName(), message.getToUserId(), "/user/quan-ly-thu.html", "Xem tin nhắn", null);

        return new Response(true, "Gửi tin nhắn thành công", message);
    }

    /**
     * Thống kê
     *
     * @param userId
     * @return
     */
    public HashMap<String, Long> report(String userId) {
        HashMap<String, Long> report = new HashMap<>();
        Criteria cri = new Criteria();
        cri.and("fromUserId").is(userId);
        cri.and("remove").is(false);
        report.put("outbox", messageRepository.count(new Query(cri)));
        cri.and("read").is(true);
        report.put("outboxRead", messageRepository.count(new Query(cri)));
        cri = new Criteria();
        cri.and("remove").is(false);
        cri.and("toUserId").is(userId);
        report.put("inbox", messageRepository.count(new Query(cri)));
        cri.and("read").is(true);
        report.put("inboxRead", messageRepository.count(new Query(cri)));
        return report;
    }

    /**
     * get count inbox
     *
     * @param userId
     * @return
     */
    public long reportInbox(String userId) {
        long count = 0;
        Criteria cri = new Criteria();
        cri.and("remove").is(false);
        cri.and("read").is(false);
        cri.and("toUserId").is(userId);
        count = messageRepository.count(new Query(cri));
        return count;
    }

    /**
     * Lấy danh sách tin nhắn theo orderIds
     *
     * @param orderIds
     * @param fromUserId
     * @return
     */
    public List<Message> getByOrderIds(List<String> orderIds, String fromUserId) {
        Criteria criteria = new Criteria();
        criteria.and("orderId").in(orderIds);
        criteria.and("fromUserId").is(fromUserId);
        List<Message> messages = messageRepository.find(new Query(criteria));
        if (messages != null && !messages.isEmpty()) {
            return messages;
        } else {
            return null;
        }
    }
}
