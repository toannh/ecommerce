package vn.chodientu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.SellerReview;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.ReviewType;
import vn.chodientu.entity.input.SellerReviewSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.OrderItemRepository;
import vn.chodientu.repository.OrderRepository;
import vn.chodientu.repository.SellerReviewRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.util.TextUtils;

@Service
public class SellerReviewService {

    @Autowired
    private SellerReviewRepository sellerReviewRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Viewer viewer;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;

    /**
     * Đánh giá ưu tín cho đơn hàng
     *
     * @param orderId
     * @param comment
     * @param point
     * @param request
     * @return
     * @throws Exception
     */
    public SellerReview review(String orderId, String content, int productQuality, int interactive, int shippingCosts, String reviewType,
            HttpServletRequest request) throws Exception {
        Order order = orderRepository.find(orderId);
        if (order == null) {
            throw new Exception("Đơn hàng không tồn tại trên hệ thống ChoDienTu");
        }
        if (viewer.getUser() == null) {
            throw new Exception("Bạn cần đăng nhập để thực hiện thao tác này ");
        }
        if (!viewer.getUser().getId().equals(order.getSellerId()) && !viewer.getUser().getId().equals(order.getBuyerId())) {
            throw new Exception("Bạn không có quyền thực hiện chức năng này" + viewer.getUser().getId());
        }
        SellerReview review = sellerReviewRepository.find(orderId, viewer.getUser().getId());
        if (review == null) {
            review = new SellerReview();
            review.setCreateTime(System.currentTimeMillis());
            review.setOrderId(orderId);
            if (viewer.getUser().getId().equals(order.getSellerId())) {
                review.setUserId(order.getSellerId());
                review.setSellerId(order.getBuyerId());
            } else {
                review.setUserId(viewer.getUser().getId());
                review.setSellerId(order.getSellerId());
            }
        } else {
            throw new Exception("Bạn đã đánh giá đơn hàng này rồi");
        }
        if (reviewType.equals("1")) {
            review.setReviewType(ReviewType.BUY);
        } else if (reviewType.equals("2")) {
            review.setReviewType(ReviewType.NOREVIEW);
        } else if (reviewType.equals("3")) {
            review.setReviewType(ReviewType.DONOTBUY);
        }
        review.setProductQuality(productQuality);
        review.setContent(content);
        review.setInteractive(interactive);
        review.setShippingCosts(shippingCosts);
        review.setUpdateTime(System.currentTimeMillis());
        review.setActive(true);
        review.setIp(TextUtils.getClientIpAddr(request));
        sellerReviewRepository.save(review);
        return review;
    }

    /**
     * Đánh giá uy tín khi tích hợp
     *
     * @param order
     */
    public SellerReview intergrateReview(String userId, String content, int productQuality, String objectId, long createTime) throws Exception {
        SellerReview review = new SellerReview();
        review.setSellerId(userId);
        review.setContent(content);
        review.setReviewType(ReviewType.BUY);
        review.setProductQuality(productQuality);
        review.setObject(objectId);
        review.setCreateTime(createTime);
        sellerReviewRepository.save(review);
        return review;
    }

    /**
     * Đánh giá uy tín khi tạo đơn hàng
     *
     * @param order
     */
    public void createDefaultReview(Order order) {
        if (order == null) {
            return;
        }
        if (order.getSellerId() == null || order.getBuyerId() == null) {
            return;
        }
        SellerReview review = new SellerReview();
        review.setUpdateTime(System.currentTimeMillis());
//        review.setPoint(1);
        review.setActive(true);
        review.setIp(":01");
        User seller = userRepository.find(order.getSellerId());
        if (seller != null) {
            review.setContent("Người mua " + order.getBuyerName() + " đánh giá bình thường");
            review.setSellerId(seller.getId());
            review.setUserId(order.getBuyerId());
            review.setId(sellerReviewRepository.genId());
            review.setCreateTime(System.currentTimeMillis());
            review.setOrderId(order.getId());
            sellerReviewRepository.save(review);
        }
        review.setContent("Người bán " + (seller.getName() != null ? seller.getName() : seller.getEmail()) + " đánh giá bình thường");
        review.setId(sellerReviewRepository.genId());
        review.setUserId(seller.getId());
        review.setSellerId(order.getBuyerId());
        sellerReviewRepository.save(review);
    }

    /**
     * Thống kê theo người bán
     *
     * @param sellerId
     * @return
     */
    public HashMap<String, Long> report(String sellerId) {
        HashMap<String, Long> data = new HashMap<>();
        try {
            long pointGood = sellerReviewRepository.getCountSellerBuy(sellerId);
            long pointNormal = sellerReviewRepository.getCountSellerNoReview(sellerId);
            long pointBad = sellerReviewRepository.getCountSellerDoNotBuy(sellerId);
            data.put("good", pointGood);
            data.put("normal", pointNormal);
            data.put("bad", pointBad);
            List<SellerReview> pointReputable = getPointReputable(sellerId);
            long point = 0;
            if (pointReputable != null && !pointReputable.isEmpty()) {
                for (SellerReview sellerPoint : pointReputable) {
                    point += sellerPoint.getProductQuality();
                }
            }
            double calPointPlus = calPointPlus(sellerId);
            data.put("total", Math.round(point + calPointPlus));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * *
     * Tìm kiếm đánh giá uy tín theo điều khiện
     *
     * @param search
     * @return
     */
    public DataPage<SellerReview> search(SellerReviewSearch search) {

        Criteria cri = new Criteria();
        if (search.getActive() > 0) {
            cri.and("active").is(search.getActive() == 1);
        }
        if (search.getUserId() != null && !search.getUserId().equals("")) {
            cri.and("userId").is(search.getUserId());
        }
        if (search.getSellerId() != null && !search.getSellerId().equals("")) {
            cri.and("sellerId").is(search.getSellerId());
        }
        Sort sort;
        switch (search.getOrderBy()) {
            case 1:
                sort = new Sort(Sort.Direction.DESC, "createTime");
                break;
            case 2:
            default:
                sort = new Sort(Sort.Direction.DESC, "point");
                break;
        }
        DataPage<SellerReview> dataPage = new DataPage<>();
        Query query = new Query(cri);
        dataPage.setDataCount(sellerReviewRepository.count(new Query(cri)));
        dataPage.setPageIndex(search.getPageIndex());
        dataPage.setPageSize(search.getPageSize());
        dataPage.setPageCount(dataPage.getDataCount() / search.getPageSize());
        if (dataPage.getDataCount() % search.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }
        query.with(new PageRequest(search.getPageIndex(), search.getPageSize(), sort));
        dataPage.setData(sellerReviewRepository.find(query));
        return dataPage;

    }

    /**
     * Đổi trạng thái đánh giá
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SellerReview changeActive(String id) throws Exception {
        SellerReview sellerReview = sellerReviewRepository.find(id);
        if (sellerReview == null) {
            throw new Exception("Không tìm thấy đánh giá yêu cầu");
        }
        sellerReview.setActive(!sellerReview.isActive());
        sellerReview.setUpdateTime(System.currentTimeMillis());
        sellerReviewRepository.save(sellerReview);
        return sellerReview;
    }

    /**
     * *
     * Lấy đánh giá theo mã đơn hàng
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SellerReview getByOrderId(String id) throws Exception {
        SellerReview sellerReview = sellerReviewRepository.getByOrderId(id);
        if (sellerReview == null) {
            return null;
        }
        return sellerReview;
    }

    public long countSellerReview(String sellerId) {
        long sellerReview = sellerReviewRepository.getCountSeller(sellerId);
        return sellerReview;
    }

    public long countSellerBuy(String sellerId) {
        long sellerBuy = sellerReviewRepository.getCountSellerBuy(sellerId);
        return sellerBuy;
    }

    public long countSellerNoReview(String sellerId) {
        long sellerNoReview = sellerReviewRepository.getCountSellerNoReview(sellerId);
        return sellerNoReview;
    }

    public long countSellerDoNotBuy(String sellerId) {
        long sellerDoNotBuy = sellerReviewRepository.getCountSellerDoNotBuy(sellerId);
        return sellerDoNotBuy;
    }

    public List<SellerReview> getPointReputable(String sellerId) {
        List<SellerReview> pointRepurable = sellerReviewRepository.getPointReputable(sellerId);
        return pointRepurable;
    }

    public List<SellerReview> countPointSeller(String sellerId) {
        List<SellerReview> listSeller = sellerReviewRepository.getPointProductQuality(sellerId);
        return listSeller;
    }

    /**
     * Lấy danh sách đánh giá theo Ids
     *
     * @param ids
     * @param userId
     * @param seller
     * @return
     */
    public List<SellerReview> getByOrderIds(List<String> ids, String userId, boolean seller) {
        Criteria criteria = new Criteria();
        if (seller) {
            criteria.and("sellerId").is(userId);
        } else {
            criteria.and("userId").is(userId);
        }
        criteria.and("orderId").in(ids);
        List<SellerReview> sellerReview = sellerReviewRepository.find(new Query(criteria));
        if (sellerReview != null && !sellerReview.isEmpty()) {
            return sellerReview;
        } else {
            return null;
        }
    }

    public Order getOrderReview(String id) throws Exception {
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new Exception("Không tim thấy đơn hàng yêu cầu");
        }
        List<OrderItem> orderItems = orderItemRepository.getByOrderId(id);
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
        List<String> itemIds = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            itemIds.add(orderItem.getItemId());
        }
        Map<String, List<String>> images = imageService.get(ImageType.ITEM, itemIds);
        for (Map.Entry<String, List<String>> entry : images.entrySet()) {
            String orderItemId = entry.getKey();
            List<String> img = entry.getValue();
            for (OrderItem orderItem : orderItems) {
                if (orderItem.getOrderId().equals(orderItemId)) {
                    orderItem.setImages(img);
                }
            }
        }
        boolean fag = false;
        Shop shop = shopService.getShop(order.getSellerId());
        if (shop != null) {
            List<String> image = imageService.get(ImageType.SHOP_LOGO, order.getSellerId());
            if (image != null && !image.isEmpty() && image.size() > 0) {
                shop.setLogo(imageService.getUrl(image.get(0)).getUrl());
                fag = true;
            }

            order.setShop(shop);
        }
        User user = userService.get(order.getSellerId());
        List<String> image = imageService.get(ImageType.AVATAR, order.getSellerId());
        if (fag == true) {
            user.setAvatar(shop.getLogo());
        } else {
            if (image != null && !image.isEmpty() && image.size() > 0) {
                user.setAvatar(imageService.getUrl(image.get(0)).getUrl());
            }
        }

        SellerReview sellerReview = getByOrderId(id);
        if (sellerReview != null) {
            order.setSellerReview(sellerReview);
            double calReputationPoints = Math.round(calReputationPoints(sellerReview.getSellerId()));
            order.setPointSellerReviewer(calReputationPoints);
        }
        order.setUser(user);
        order.setItems(orderItems);
        return order;
    }

    public double calReputationPoints(String id) throws Exception {
        double pointBUY = sellerReviewRepository.getBySellerReviewId(id, ReviewType.BUY);
        double pointNOREVIEW = sellerReviewRepository.getBySellerReviewId(id, ReviewType.NOREVIEW);
        double pointDONOTBUY = sellerReviewRepository.getBySellerReviewId(id, ReviewType.DONOTBUY);
        double sum = 0;
        if ((pointBUY + pointNOREVIEW + Math.abs(pointDONOTBUY)) > 0) {
            sum = (pointBUY / (pointBUY + pointNOREVIEW + Math.abs(pointDONOTBUY))) * 100;
        }
        return sum;
    }

    public double calPointPlus(String id) throws Exception {
        double pointPlus = sellerReviewRepository.getPointSellerId(id);
        return pointPlus;
    }

    /**
     * Danh sách sellerreview
     *
     * @param search
     * @return
     */
    public DataPage<SellerReview> searchSellerReview(SellerReviewSearch sellerReviewSearch) {
        Criteria cri = new Criteria();
        if (sellerReviewSearch.getActive() > 0) {
            cri.and("active").is(sellerReviewSearch.getActive() == 1);
        }
        if (sellerReviewSearch.getSellerId() != null && !"".equals(sellerReviewSearch.getSellerId())
                && sellerReviewSearch.getUserId() != null && !"".equals(sellerReviewSearch.getUserId())) {
            Criteria c1 = new Criteria("sellerId").is(sellerReviewSearch.getSellerId());
            Criteria c2 = new Criteria("userId").is(sellerReviewSearch.getUserId());
            cri.orOperator(c1, c2);
        } else {
            if (sellerReviewSearch.getUserId() != null && !"".equals(sellerReviewSearch.getUserId())) {
                cri.and("userId").is(sellerReviewSearch.getUserId());
            }
            if (sellerReviewSearch.getSellerId() != null && !"".equals(sellerReviewSearch.getSellerId())) {
                cri.and("sellerId").is(sellerReviewSearch.getSellerId());
            }
        }
        cri.and("orderId").ne(null);
        cri.and("emailAdmin").is(null);

        Sort sort;
        switch (sellerReviewSearch.getOrderBy()) {
            case 1:
                sort = new Sort(Sort.Direction.DESC, "like");
                break;
            case 2:
            default:
                sort = new Sort(Sort.Direction.DESC, "createTime");
                break;
        }

        Query query = new Query(cri);
        query.with(sort);
        query.skip(sellerReviewSearch.getPageIndex() * sellerReviewSearch.getPageSize()).limit(sellerReviewSearch.getPageSize());
        DataPage<SellerReview> page = new DataPage<>();
        page.setPageSize(sellerReviewSearch.getPageSize());
        page.setPageIndex(sellerReviewSearch.getPageIndex());
        if (page.getPageSize() <= 0) {
            page.setPageSize(2);
        }
        page.setDataCount(sellerReviewRepository.count(query));
        page.setPageCount(page.getDataCount() / page.getPageSize());
        if (page.getDataCount() % page.getPageSize() != 0) {
            page.setPageCount(page.getPageCount() + 1);
        }
        page.setData(sellerReviewRepository.find(query));
        return page;
    }

    public SellerReview reviewAdmin(String orderId, String content, int productQuality, String reviewType,
            HttpServletRequest request, String emaiAdmin) throws Exception {
        Order order = orderRepository.find(orderId);
        if (order == null) {
            throw new Exception("Đơn hàng không tồn tại trên hệ thống ChoDienTu");
        }
        SellerReview review = new SellerReview();
        review.setCreateTime(System.currentTimeMillis());
        review.setOrderId(orderId);
        review.setUserId(order.getSellerId());
        review.setSellerId(order.getBuyerId());
        if (reviewType.equals("1")) {
            review.setReviewType(ReviewType.BUY);
            review.setProductQuality(2);
        } else if (reviewType.equals("2")) {
            review.setReviewType(ReviewType.NOREVIEW);
            review.setProductQuality(0);
        } else if (reviewType.equals("3")) {
            review.setReviewType(ReviewType.DONOTBUY);
            review.setProductQuality(-2);
        }
        review.setContent(content);
        review.setUpdateTime(System.currentTimeMillis());
        review.setActive(true);
        review.setIp(TextUtils.getClientIpAddr(request));
        review.setEmailAdmin(emaiAdmin);
        sellerReviewRepository.save(review);
        return review;
    }
}
