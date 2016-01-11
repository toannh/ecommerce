package vn.chodientu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.chodientu.component.Validator;
import vn.chodientu.component.FacebookClient;
import vn.chodientu.entity.data.GroupFacebook;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.CashTransaction;
import vn.chodientu.entity.db.SellerPostFacebook;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.SellerPostStatus;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.ActiveKeyRepository;
import vn.chodientu.repository.CashRepository;
import vn.chodientu.repository.CashTransactionRepository;
import vn.chodientu.repository.SellerPostFacebookRepository;

@Service
public class FacebookService {

    @Autowired
    private FacebookClient facebookClient;
    @Autowired
    private SellerPostFacebookRepository sellerPostFacebookRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;
@Scheduled(fixedDelay = 60000)
    public void getStatus() throws Exception {
        SellerPostFacebook postFacebook = sellerPostFacebookRepository.getForCheck();
        if (postFacebook != null) {
            process(postFacebook);
        }
    }

    @Async
    private void process(SellerPostFacebook postFacebook) throws Exception {
        List<String> list = facebookClient.getStatus(postFacebook.getFacebookId(), postFacebook.getProductId(), postFacebook.getId());
        if (list != null) {
            postFacebook.setStatus(SellerPostStatus.SUCCESS);
            postFacebook.setEndTime(System.currentTimeMillis());
            postFacebook.setSucessGroup(list.size());
            String link = "";
            for (String s : list) {
                link += "<li><a href=" + s + " >" + s + "</a></li>";
            }
            postFacebook.setResultLink(link);
            sellerPostFacebookRepository.save(postFacebook);
            long l= postFacebook.getTotalGroup() - list.size();
            if(l > 0){
            CashTransaction cashTransaction = new CashTransaction();
            cashTransaction.setId(cashTransactionRepository.genId());
            cashTransaction.setTime(System.currentTimeMillis());
            cashTransaction.setType(CashTransactionType.REVERT_UP_FACEBOOK);
            cashTransaction.setSpentQuantity(1);
            cashTransaction.setUserId(postFacebook.getSellerId());
            cashTransaction.setAmount(l * 20);
            long monney = cashTransaction.getAmount() * cashTransaction.getSpentQuantity();
            Cash topupPaymentDone = cashRepository.topupPaymentDone(cashTransaction.getUserId(), monney);
            cashTransaction.setNewBalance(topupPaymentDone.getBalance());
            cashTransactionRepository.save(cashTransaction);
            }
        }
    }
    public Response getUrl(String sellerId) throws Exception {
        try {
            String url = facebookClient.getLoginUrl(sellerId);
            return new Response(true, "Thêm mới thành công!", url);
        } catch (Exception ex) {
            throw new Exception("Hệ thống xảy ra lỗi");
        }
    }

    public List<GroupFacebook> getGroup(String fid, String after) throws Exception {
        try {
            List<GroupFacebook> group = facebookClient.getGroup(fid, after);
            return group;
        } catch (Exception ex) {
            throw new Exception("Hệ thống xảy ra lỗi");
        }
    }

    public String booking(String sellerId, String message, String link, String name, String caption, String description, String image, String productId, String groups, String facebookId, long totalGroup) throws Exception {
        try {
            String jobId = sellerPostFacebookRepository.genId();
            String time = facebookClient.booking(message, link, name, caption, description, image, productId, groups, facebookId, jobId);
            SellerPostFacebook sellerPostFacebook = new SellerPostFacebook();
            sellerPostFacebook.setCreateTime(System.currentTimeMillis());
            sellerPostFacebook.setFacebookId(facebookId);
            sellerPostFacebook.setId(jobId);
            sellerPostFacebook.setStatus(SellerPostStatus.NEW);
            sellerPostFacebook.setProductId(productId);
            sellerPostFacebook.setTotalGroup(totalGroup);
            sellerPostFacebook.setTimeFinish(time);
            sellerPostFacebook.setSellerId(sellerId);
            sellerPostFacebook.setMessage(message);
            sellerPostFacebook.setTotalMoney(totalGroup * 20);
            sellerPostFacebookRepository.save(sellerPostFacebook);

            CashTransaction cashTransaction = new CashTransaction();
            cashTransaction.setId(cashTransactionRepository.genId());
            cashTransaction.setTime(System.currentTimeMillis());
            cashTransaction.setType(CashTransactionType.UP_FACEBOOK);
            cashTransaction.setSpentQuantity(1);
            cashTransaction.setUserId(sellerId);
            cashTransaction.setAmount(totalGroup * 20);
            long monney = cashTransaction.getAmount() * cashTransaction.getSpentQuantity() * -1;
            Cash topupPaymentDone = cashRepository.topupPaymentDone(cashTransaction.getUserId(), monney);
            cashTransaction.setNewBalance(topupPaymentDone.getBalance());
            cashTransactionRepository.save(cashTransaction);
            return time;
        } catch (Exception ex) {
            throw new Exception("Hệ thống xảy ra lỗi");
        }
    }

    public List<SellerPostFacebook> getHis(String sellerId) {
        return sellerPostFacebookRepository.list(sellerId);

    }
}
