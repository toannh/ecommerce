package vn.chodientu.service;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import vn.chodientu.component.NlClient;
import vn.chodientu.component.ScClientV2;
import vn.chodientu.component.Validator;
import vn.chodientu.entity.data.OrderSubItem;
import vn.chodientu.entity.db.BigLanding;
import vn.chodientu.entity.db.Cash;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.Coupon;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.Item;
import vn.chodientu.entity.db.Lading;
import vn.chodientu.entity.db.Message;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.SellerReview;
import vn.chodientu.entity.db.SellerSupportFee;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.db.UserVerify;
import vn.chodientu.entity.db.Ward;
import vn.chodientu.entity.enu.CashTransactionType;
import vn.chodientu.entity.enu.EmailOutboxType;
import vn.chodientu.entity.enu.FeeType;
import vn.chodientu.entity.enu.ImageType;
import vn.chodientu.entity.enu.ListingType;
import vn.chodientu.entity.enu.PaymentMethod;
import vn.chodientu.entity.enu.PaymentStatus;
import vn.chodientu.entity.enu.SellerHistoryType;
import vn.chodientu.entity.enu.ShipmentService;
import vn.chodientu.entity.enu.ShipmentStatus;
import vn.chodientu.entity.enu.ShipmentType;
import vn.chodientu.entity.enu.SmsOutboxType;
import vn.chodientu.entity.input.OrderSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.CashRepository;
import vn.chodientu.repository.ItemRepository;
import vn.chodientu.repository.LadingRepository;
import vn.chodientu.repository.MessageRepository;
import vn.chodientu.repository.OrderItemRepository;
import vn.chodientu.repository.OrderRepository;
import vn.chodientu.repository.ShopRepository;
import vn.chodientu.repository.UserRepository;
import vn.chodientu.repository.UserVerifyRepository;
import vn.chodientu.util.TextUtils;
import vn.chodientu.util.UrlUtils;

/**
 *
 * @author Phu
 */
@Service
public class OrderService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserVerifyRepository userVerifyRepository;
    @Autowired
    private CashRepository cashRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private NlClient nlClient;
    @Autowired
    private UserService userService;
    @Autowired
    private ScClientV2 scClient;
    @Autowired
    private Gson gson;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private Viewer viewer;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private WardService wardService;
    @Autowired
    private Validator validator;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SearchIndexService indexService;
    @Autowired
    private LadingRepository ladingRepository;
    @Autowired
    private SellerCustomerService customerService;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private SellerReviewService sellerReviewService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private SellerHistoryService sellerHistoryService;
    @Autowired
    private CashService cashService;
    @Autowired
    private SellerSupportFeeService sellerSupportFeeService;
    @Autowired
    private BigLandingService bigLandingService;
    @Autowired
    private MessageRepository messageRepository;
    private final String baseUrl = "http://chodientu.vn";
    @Autowired
    private HttpServletRequest request;

    /**
     * Thêm item vào giỏ hàng
     *
     * @param itemId
     * @param quantity
     * @param subItem
     * @return
     * @throws Exception
     */
    public Response addToCart(String itemId, int quantity, OrderSubItem subItem) throws Exception {
        Item item = itemRepository.find(itemId);
        Order order = null;
        if (item == null) {
            throw new Exception("Sản phẩm bạn muốn mua không tồn tại");
        }
        if (!item.isActive() || !item.isApproved() || !item.isCompleted()) {
            throw new Exception("Sản phẩm không đủ điều kiện thanh toán");
        }
        if (item.getStartTime() > System.currentTimeMillis()) {
            throw new Exception("Sản phẩm chưa đến hạn đăng bán, không thể giao dịch");
        }
        if (item.getListingType() == ListingType.AUCTION) {
            if (item.getHighestBider() != null && !item.getHighestBider().equals("")) {
                if (viewer.getUser() != null) {
                    if (!item.getHighestBider().equals(viewer.getUser().getId())) {
                        throw new Exception("Bạn không thể mua sản phẩm này");
                    }
                }
            }
        } else {
            if (item.getEndTime() < System.currentTimeMillis()) {
                throw new Exception("Sản phẩm đã hết hạn đăng bán, không thể giao dịch");
            }
        }
        if (viewer.getUser() != null && viewer.getUser().getId().equals(item.getSellerId())) {
            throw new Exception("Không thể tự mua sản phẩm của chính bạn");
        }
        boolean exist = false;
        if (viewer.getCart() == null) {
            viewer.setCart(new ArrayList<Order>());
        }
        List<Order> orders = viewer.getCart();
        for (Order o : orders) {
            if (o.getSellerId().equals(item.getSellerId())) {
                exist = true;
                order = o;
                break;
            }
        }
        if (!exist) {
            order = new Order();
            order.setId(orderRepository.genId());
            order.setSellerId(item.getSellerId());
            if (viewer.getUser() != null) {
                order.setBuyerId(viewer.getUser().getId());
                order.setBuyerEmail(viewer.getUser().getEmail());
                order.setBuyerName(viewer.getUser().getName());
                order.setBuyerPhone(viewer.getUser().getPhone());
                order.setBuyerAddress(viewer.getUser().getAddress());
                order.setBuyerCityId(viewer.getUser().getCityId());
                order.setBuyerDistrictId(viewer.getUser().getDistrictId());
                order.setItems(new ArrayList<OrderItem>());
            }
            orders.add(order);
        }

        exist = false;
        if (order.getItems() == null) {
            order.setItems(new ArrayList<OrderItem>());
        }
        for (OrderItem oi : order.getItems()) {
            if (oi.getItemId().equals(item.getId())) {
                exist = true;
                oi.setQuantity(oi.getQuantity() + (quantity > 0 ? quantity : 1));
                this.convertSubItem(oi, subItem);
                break;
            }
        }

        if (!exist) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(orderItemRepository.genId());
            orderItem.setItemId(item.getId());
            orderItem.setCategoryPath(item.getCategoryPath());
            orderItem.setItemName(item.getName());
            orderItem.setItemPrice(this.getItemPrice(item));
            orderItem.setQuantity(quantity > 0 ? quantity : 1);
            orderItem.setDiscountPrice(item.getDiscountPrice());
            orderItem.setDiscountPercent(item.getDiscountPercent());
            orderItem.setGiftDetail(item.getGiftDetail());
            orderItem.setShipmentPrice(item.getShipmentPrice());
            orderItem.setShipmentType(item.getShipmentType());
            orderItem.setWeight(item.getWeight());
            this.convertSubItem(orderItem, subItem);
            order.getItems().add(orderItem);
        }

        return new Response(true);
    }

    private void convertSubItem(OrderItem item, OrderSubItem subItem) {
        if (subItem == null) {
            return;
        }
        List<OrderSubItem> subItems = item.getSubItem();
        if (subItems == null) {
            subItems = new ArrayList<OrderSubItem>();
        }
        subItem.setColorValueName(subItem.getColorValueName() == null || subItem.getColorValueName().equals("") ? "" : subItem.getColorValueName());
        subItem.setSizeValueName(subItem.getSizeValueName() == null || subItem.getSizeValueName().equals("") ? "" : subItem.getSizeValueName());
        subItem.setAlias(TextUtils.createAlias(subItem.getColorValueName() + subItem.getSizeValueName()));
        boolean exist = false;
        int quantity = 0;
        for (OrderSubItem osi : subItems) {
            if (osi.getAlias().equals(subItem.getAlias())) {
                exist = true;
                osi.setQuantity(subItem.getQuantity());
            }
            quantity += osi.getQuantity();
        }
        if (!exist) {
            subItems.add(subItem);
            quantity += subItem.getQuantity();
        }
        item.setSubItem(subItems);
        item.setQuantity(quantity);
    }

    /**
     * Xóa item trong giỏ hàng
     *
     * @param item
     */
    public void removeFromCart(OrderItem item) {
        if (viewer.getCart() == null) {
            viewer.setCart(new ArrayList<Order>());
        }
        List<Order> orders = viewer.getCart();
        for (Order o : orders) {
            for (int i = 0; i < o.getItems().size(); i++) {
                OrderItem oi = o.getItems().get(i);
                if (oi.getId().equals(item.getId())) {
                    o.getItems().remove(i);
                    break;
                }
            }
        }
        for (Order o : orders) {
            if (o.getItems().isEmpty()) {
                orders.remove(o);
            }
        }
    }

    /**
     * Update giỏ hàng, truyền vào mã order của giỏ hàng và mã của order item +
     * số lượng, k phải mã item nhé
     *
     * @param items
     */
    public void updateCart(List<OrderItem> items) {
        List<Order> orders = viewer.getCart();
        for (Order o : orders) {
            if (!o.getItems().isEmpty()) {
                for (OrderItem oi : o.getItems()) {
                    for (OrderItem i : items) {
                        if (oi.getId().equals(i.getId())) {
                            oi.setQuantity(i.getQuantity() > 0 ? i.getQuantity() : 1);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Lấy giỏ hàng
     *
     * @return
     */
    public List<Order> getCart() {
        return viewer.getCart();
    }

    /**
     * get by id
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    public Order get(String id) throws Exception {
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new Exception("Không tìm thấy đơn hàng yêu cầu");
        }
        return order;
    }

    /**
     * Lấy ra toàn bộ thông tin order, sản phẩm đã lấy id ảnh.
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Order getOrder(String id) throws Exception {
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new Exception("Không tìm thấy đơn hàng yêu cầu");
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
            try {
                for (OrderItem orderItem : orderItems) {
                    if (orderItem.getItemId().equals(orderItemId)) {
                        orderItem.setImages(img);
                    }
                }
            } catch (Exception e) {
            }
        }
        User user = userService.get(order.getSellerId());
        List<String> is = imageService.get(ImageType.AVATAR, user.getId());
        if (is != null && !is.isEmpty() && is.size() > 0) {
            user.setAvatar(is.get(0));
        }
        SellerReview sellerReview = sellerReviewService.getByOrderId(id);
        if (sellerReview != null) {
            order.setSellerReview(sellerReview);
        }
        order.setUser(user);
        order.setItems(orderItems);
        return order;
    }

    /**
     * Tính toán đơn hàng
     *
     * @param order
     * @param all
     * @param weightall
     * @param priceall
     * @param sellerCityId
     * @param sellerDistrictId
     * @param protec
     * @return
     * @throws Exception
     */
    public Order calculator(Order order, boolean all, int weightall, long priceall,
            String sellerCityId, String sellerDistrictId, boolean protec) throws Exception {

        long price = 0;
        int weight = 0;
        long shipmentPrice = 0;
        long shipPrice = 0;
        int weightPrice = 0;
        boolean fagFixed = false;
        List<String> ids = new ArrayList<>();
        for (OrderItem orderItem : order.getItems()) {
            ids.add(orderItem.getItemId());
        }
        List<Item> items = itemRepository.get(ids);
        for (OrderItem orderItem : order.getItems()) {
            for (Item item : items) {
                if (item.getId().equals(orderItem.getItemId())) {
                    if (orderItem.getQuantity() > item.getQuantity()) {
                        orderItem.setQuantity(orderItem.getQuantity());
                    }
                    break;
                }
            }
        }

        for (OrderItem orderItem : order.getItems()) {
            ids.add(orderItem.getItemId());
            price += orderItem.getQuantity() * orderItem.getItemPrice();
            weight += orderItem.getQuantity() * orderItem.getWeight();
            if (orderItem.getShipmentType() == ShipmentType.FIXED) {
                shipmentPrice += orderItem.getShipmentPrice() > 0 ? orderItem.getQuantity() * orderItem.getShipmentPrice() : 0;
                fagFixed = true;
            }
            if (orderItem.getShipmentType() == ShipmentType.BYWEIGHT) {
                shipPrice += orderItem.getQuantity() * orderItem.getItemPrice();
                weightPrice += orderItem.getQuantity() * orderItem.getWeight();
            }
        }
        if (order.getCouponId() != null && !order.getCouponId().equals("")) {
            Response<Coupon> validate = couponService.validate(order.getCouponId(), order.getSellerId());
            if (!validate.isSuccess()) {
                throw new Exception(validate.getMessage());
            }
            Coupon coupon = validate.getData();
            if (price < coupon.getMinOrderValue()) {
                throw new Exception("Giá trị đơn hàng không đủ để áp dụng mã khuyến mại này");
            }
            order.setCouponPrice(coupon.getDiscountPrice() > 0 ? coupon.getDiscountPrice() : (price * coupon.getDiscountPercent() / 100));
        }

        Seller seller = sellerService.getById(order.getSellerId());
        User user = userService.get(order.getSellerId());
        if (user.getCityId() == null || user.getCityId().equals("")) {
            Shop shop = shopService.getShop(user.getId());
            if (shop.getCityId() == null || shop.getCityId().equals("")) {
                throw new Exception("Địa chỉ người bán chưa cập nhật nên không tính được phí vận chuyển..");
            }
            user.setCityId(shop.getCityId());
            user.setDistrictId(shop.getDistrictId());
        }
        if (all) {
            shipPrice = price;
            weightPrice = weight;
            user.setDistrictId(sellerDistrictId);
            user.setCityId(sellerCityId);
        }
        if (weightall > 0) {
            weightPrice = weightall;
        }
        if (priceall > 0) {
            shipPrice = priceall;
        }
        boolean checkSellerDiscountPayment = false;
        if (seller.getScEmail() != null && !seller.getScEmail().equals("") && shipPrice > 0 && weightPrice > 0) {
            List<String> local = new ArrayList<>();
            local.add(user.getCityId());
            local.add(order.getReceiverCityId());
            List<City> citys = cityService.getCityByIds(local);
            String rCityId = "";
            String rDistrictId = "";
            String rWardId = "";
            for (City city : citys) {
                if (city.getId().equals(user.getCityId())) {
                    user.setCityId(city.getScId());
                    District district = districtService.get(user.getDistrictId());
                    if (district != null) {
                        user.setDistrictId(district.getScId().trim());
                    }
                }
                if (order.getReceiverCityId().equals(city.getId())) {
                    rCityId = city.getScId();
                    District district = districtService.get(order.getReceiverDistrictId().trim());
                    if (district != null) {
                        rDistrictId = district.getScId();
                        if (order.getReceiverWardId() != null) {
                            if (!order.getReceiverWardId().equals("") && !order.getReceiverWardId().equals("0")) {
                                Ward ward = wardService.get(order.getReceiverWardId());
                                if (ward != null) {
                                    rWardId = ward.getScId().trim();
                                }
                            }
                        }
                    }
                }

            }
            long hprice = 0;
            boolean isCod = false;
            boolean isPayment = false;

            if (order.getPaymentMethod() != null) {
                isCod = (order.getPaymentMethod() == PaymentMethod.COD) ? true : false;
                isPayment = (!order.getPaymentMethod().equals(PaymentMethod.COD) || !order.getPaymentMethod().equals(PaymentMethod.NONE)) ? true : false;
            }
            ScClientV2.FeeShip caculateFee = scClient.caculateFee(seller.getScEmail(),
                    order.getShipmentService(),
                    user.getCityId(), user.getDistrictId(),
                    rCityId, rDistrictId, rWardId,
                    shipPrice, weightPrice, isCod, protec, isPayment);
            try {
                hprice += caculateFee.getShip();
                order.setShipmentPriceBW(caculateFee.getShip());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                order.setShipmentPCod(caculateFee.getPcod());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            long moneyCourier = 0;
            try {
                List<ScClientV2.Courier> couriers = caculateFee.getCourier();
                order.setCouriers(couriers);
                boolean isCourier = false;
                if (order.getCourierId() != 0) {
                    for (ScClientV2.Courier courier : couriers) {
                        if (courier.getCourierId() == order.getCourierId()) {
                            moneyCourier = courier.getMoneyPickup() + courier.getMoneyDelivery();
                            isCourier = true;
                            break;
                        }
                    }
                }
                if (!isCourier) {
                    order.setCourierId(0);
                }
            } catch (Exception e) {

            }
            hprice += moneyCourier;
            // Người bán hỗ trợ thanh toán vận chuyển
            order.setSellerDiscountPayment(0);
            if (order.getPaymentMethod() != null && order.getPaymentMethod() != PaymentMethod.NONE && order.getPaymentMethod() != PaymentMethod.COD) {
                SellerSupportFee sellerSupportFee = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), price, FeeType.ONLINEPAYMENT);
                if (sellerSupportFee != null) {
                    int discountPercent = sellerSupportFee.getDiscountPercent();
                    if (discountPercent >= 2) {
                        long sellerDiscountPayment = (price * discountPercent) / 100;
                        order.setSellerDiscountPayment(sellerDiscountPayment);
                        checkSellerDiscountPayment = true;
                    }
                }
            }
            order.setSellerDiscountShipment(0);
            if (hprice > 0) {
                SellerSupportFee sellerSupportFee = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), price, FeeType.COD);
                if (sellerSupportFee != null) {
                    long discountShip = sellerSupportFee.getDiscountPrice();
                    if (discountShip > 0) {
                        if (discountShip > hprice) {
                            discountShip = hprice;
                        }
                        order.setSellerDiscountShipment(discountShip);
                        //hprice = hprice - discountShip;
                    }
                }
            }

            //Chương trình khuyến mại
            /**
             * @T1. Giảm toàn bộ phí vận chuyển khi thanh toán ngân lượng linh
             * hoạt theo sc cho đơn hàng klg < 2kg
             * @T2. Giảm tối đa 50k khi thanh toán qua visa cho đơn hàng ngành
             * hàng thời trang
             */
            if (priceall == 0 || (priceall > 0 && order.getPaymentStatus() == PaymentStatus.PAID)) {
                //Tính phí (all = false)
                if (weightPrice <= 2000 && (order.getPaymentMethod() != null && order.getPaymentMethod() != PaymentMethod.NONE && order.getPaymentMethod() != PaymentMethod.COD)) {
                    order.setCdtDiscountShipment(hprice - order.getSellerDiscountShipment());
                } else {
//                    //chương trình khuyễn mãi 50k phí vận chuyển khi thanh toán qua visa cho ngành hàng thời trang
//                    for (OrderItem orderItem : order.getItems()) {
//                        if (order.getPaymentMethod() == PaymentMethod.VISA && orderItem.getCategoryPath().contains("2924")) {
//                            if (hprice > 0 && hprice <= 50000) {
//                                order.setCdtDiscountShipment(hprice);
//                            } else if (hprice - 50000 > 0) {
//                                order.setCdtDiscountShipment(50000);
//                            }
//                            break;
//                        }
//                    }
                    order.setCdtDiscountShipment(0);
                }
            } else {
                order.setCdtDiscountShipment(0);
            }
            shipmentPrice += hprice;
            if (all) {
                shipmentPrice = caculateFee.getShip();
                shipmentPrice += moneyCourier;
            }

        }
        if (fagFixed) {
            order.setSellerDiscountShipment(0);
            if (shipmentPrice > 0) {
                SellerSupportFee sellerSupportFee = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), price, FeeType.COD);
                if (sellerSupportFee != null) {
                    long discountShip = sellerSupportFee.getDiscountPrice();
                    if (discountShip > 0) {
                        if (discountShip > shipmentPrice) {
                            discountShip = shipmentPrice;
                        }
                        order.setSellerDiscountShipment(discountShip);
                        //hprice = hprice - discountShip;
                    }
                }
            }
        }
        // Người bán hỗ trợ thanh toán vận chuyển - và kiểm tra xem đơn hàng đã được trừ tiền hàng hóa bởi NB hỗ trợ
        if (!checkSellerDiscountPayment) {
            order.setSellerDiscountPayment(0);
            if (order.getPaymentMethod() != null && order.getPaymentMethod() != PaymentMethod.NONE && order.getPaymentMethod() != PaymentMethod.COD) {
                SellerSupportFee sellerSupportFee = sellerSupportFeeService.getTopByOrderPrice(seller.getUserId(), price, FeeType.ONLINEPAYMENT);
                if (sellerSupportFee != null) {
                    int discountPercent = sellerSupportFee.getDiscountPercent();
                    if (discountPercent >= 2) {
                        long sellerDiscountPayment = (price * discountPercent) / 100;
                        order.setSellerDiscountPayment(sellerDiscountPayment);
                    }
                }
            }
        }

        BigLanding existCurent = bigLandingService.getExistCurent();
        if (existCurent != null && seller.isNlIntegrated() && seller.isScIntegrated()) {
            if (order.getCreateTime() <= 0) {
                order.setCreateTime(System.currentTimeMillis());
                long createTime = System.currentTimeMillis();
                long startTime = existCurent.getStartTime();
                long endTime = existCurent.getEndTime();
                if (createTime >= startTime && createTime <= endTime && weightPrice <= 2000 && !fagFixed && (order.getPaymentMethod() != null && order.getPaymentMethod() != PaymentMethod.NONE)) {
                    order.setCdtDiscountShipment(shipmentPrice - order.getSellerDiscountShipment());
                }
            } else {
                long createTime = order.getCreateTime();
                long startTime = existCurent.getStartTime();
                long endTime = existCurent.getEndTime();
                long currentTime = System.currentTimeMillis();
                if (createTime >= startTime && createTime <= endTime && weightPrice <= 2000 && !fagFixed) {
                    if (currentTime >= existCurent.getStartTimeSeller() && currentTime <= existCurent.getEndTimeSeller()) {
                        order.setCdtDiscountShipment(shipmentPrice - order.getSellerDiscountShipment());
                    }
                }
            }

        }
        order.setShipmentPrice(shipmentPrice);
        order.setTotalPrice(price);
        order.setRemoveBuyer(false);
        order.setFinalPrice(order.getTotalPrice() - order.getCouponPrice() + order.getShipmentPrice() - order.getCdtDiscountShipment() - order.getSellerDiscountPayment() - order.getSellerDiscountShipment());
        viewer.setInvoice(order);
        return order;
    }

    /**
     * Tạo đơn hàng
     *
     * @param order
     * @return
     * @throws Exception
     */
    public Response createOrder(Order order) throws Exception {
        Map<String, String> error = validator.validate(order);
        if (order.getSellerId() == null || order.getSellerId().equals("")) {
            throw new Exception("Mã người bán không được để trống");
        }
        Seller seller = sellerService.getById(order.getSellerId());
        if (order.getBuyerCityId() == null || order.getBuyerCityId().equals("0")) {
            error.put("buyerCityId", "Địa chỉ tỉnh,thành phố người mua không được để trống");
        }
        if (order.getBuyerDistrictId() == null || order.getBuyerDistrictId().equals("0")) {
            error.put("buyerDistrictId", "Địa chỉ quận,huyện người mua không được để trống");
        }
        if (order.getReceiverCityId() == null || order.getReceiverCityId().equals("0")) {
            error.put("receiverCityId", "Địa chỉ tỉnh,thành phố người nhận không được để trống");
        }
        if (order.getReceiverDistrictId() == null || order.getReceiverDistrictId().equals("0")) {
            error.put("receiverDistrictId", "Địa chỉ quận,huyện người nhận không được để trống");
        } else {
            if (order.getReceiverWardId() == null || order.getReceiverWardId().equals("0")) {

                if (scClient.checkDistrict(order.getReceiverDistrictId())) {
                    error.put("receiverWardId", "Địa chỉ phường,xã người nhận không được để trống");
                }
            }
        }

        if (order.getPaymentMethod() == null) {
            order.setPaymentMethod(PaymentMethod.NONE);
        }
        List<OrderItem> orderItems = order.getItems();
        List<String> itemIds = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            itemIds.add(orderItem.getItemId());
        }
        List<Item> items = itemRepository.get(itemIds);
        List<OrderItem> oItems = new ArrayList<>();
        boolean fagShipmentPrice = true;
        for (OrderItem orderItem : orderItems) {
            for (Item item : items) {
                if (orderItem.getItemId().equals(item.getId())) {
                    orderItem.setOrderId(order.getId());
                    orderItem.setItemPrice(this.getItemPrice(item));
                    orderItem.setItemStartPrice(item.getSellPrice());
                    orderItem.setCategoryPath(item.getCategoryPath());
                    orderItem.setDiscountPercent(item.getDiscountPercent());
                    orderItem.setDiscountPrice(item.getDiscountPrice());
                    orderItem.setGiftDetail(item.getGiftDetail());
                    orderItem.setItemName(item.getName());
                    orderItem.setShipmentPrice(item.getShipmentPrice());
                    orderItem.setShipmentType(item.getShipmentType());
                    orderItem.setWeight(item.getWeight());
                    oItems.add(orderItem);
                }
                if (item.getShipmentType() == ShipmentType.BYWEIGHT) {
                    fagShipmentPrice = false;
                }

            }
        }
        if (order.getCourierId() == 0 && !fagShipmentPrice && seller.isScIntegrated()) {
            error.put("courierId", "Hãng vận chuyển không được để trống");
        }
        if (!error.isEmpty()) {
            return new Response(false, "Thông tin đơn hàng chưa chính xác", error);
        }
        order.setItems(oItems);
        order = calculator(order, false, 0, 0, null, null, false);
        order.setCreateTime(System.currentTimeMillis());
        order.setRemoveBuyer(false);
        order.setUpdateTime(System.currentTimeMillis());
        order.setPaymentStatus(PaymentStatus.NEW);
        if (!fagShipmentPrice && order.getShipmentPrice() <= 0 && order.getShipmentPriceBW() < 0) {
            String toString = null;
            if (order.getShipmentService() == ShipmentService.SLOW) {
                toString = "Tiết kiệm";
            } else {
                toString = "Nhanh";
            }
            User user = userService.get(order.getSellerId());
            String addSeller = null;
            if (user != null) {
                Response citybyId = cityService.getCitybyId(user.getCityId());
                Response districtById = districtService.getDistrictById(user.getDistrictId());
                City city = (City) citybyId.getData();
                District district = (District) districtById.getData();
                Response citybyIdOr = cityService.getCitybyId(order.getReceiverCityId());
                Response districtByIdOr = districtService.getDistrictById(order.getReceiverDistrictId());
                City dataOr = (City) citybyIdOr.getData();
                District data = (District) districtByIdOr.getData();
                addSeller = "<strong>(" + district.getName() + "/" + city.getName() + ")</strong> tới <strong>(" + data.getName() + "/" + dataOr.getName() + ")</strong>";
            }
            error.put("shipmentPriceCheck", "Không hỗ trợ hình thức thanh toán" + toString);
            return new Response(false, "ChợĐiệnTử không hỗ trợ vận chuyển từ " + addSeller, error);
        }
        if (oItems == null || oItems.isEmpty()) {
            error.put("shipmentPriceCheck", "Hiện không có sản phẩm nào trong đơn hàng.. Tính lại đi");
            return new Response(false, "Hiện không có sản phẩm nào trong đơn hàng", error);
        }
        orderRepository.save(order);
        for (OrderItem orderItem : oItems) {
            orderItemRepository.save(orderItem);
        }
        sellerHistoryService.create(SellerHistoryType.ORDER, order.getId(), true, 0, order.getSellerId());
        customerService.addCustomer(order);
        String messGestCO = "";
        if (order.isGuestcheckout()) {
            if (!userRepository.existsEmail(order.getBuyerEmail().trim())) {
                User user = new User();
                String passUser = orderItemRepository.genId();
                user.setId(userRepository.genId());
                user.setEmail(order.getBuyerEmail().trim());
                user.setName(order.getBuyerName());
                user.setPhone(order.getBuyerPhone());
                user.setSalt(RandomStringUtils.randomAlphanumeric(25));
                user.setPassword(DigestUtils.md5DigestAsHex((passUser + user.getSalt()).getBytes()));
                user.setJoinTime(System.currentTimeMillis());
                user.setUpdateTime(System.currentTimeMillis());
                user.setCityId(order.getBuyerCityId());
                user.setDistrictId(order.getBuyerDistrictId());
                user.setActive(true);
                userRepository.save(user);
                try {
                    Cash cash = new Cash();
                    cash.setUserId(user.getId());
                    cashRepository.save(cash);
                    cashService.reward(CashTransactionType.REGISTER, user.getId(), user.getId(), "/user/" + user.getId() + "/ho-so-nguoi-ban.html", null, null);
                } catch (Exception e) {
                }
                UserVerify uv = new UserVerify();
                uv.setUserId(user.getId());
                uv.setTime(System.currentTimeMillis());
                userVerifyRepository.save(uv);

                Map<String, Object> data = new HashMap<>();
                data.put("code", uv.getId());
                data.put("password", passUser);
                data.put("username", (user.getUsername() == null || user.getUsername().equals("")) ? user.getEmail() : user.getUsername());
                emailService.send(EmailOutboxType.GUEST_CHECKOUT, user.getEmail(), "Kích hoạt tài khoản tại Chợ Điện Tử", "guestcheckout", data);
                messGestCO = "Tài khoản đã được tạo thành công, thông tin hệ thống đã gửi vào email(" + user.getEmail() + ").";
            } else {
                messGestCO = "Email người mua đã tồn tại trên ChợĐiệnTử nên không thể tạo được tài khoản.";
            }
        }
        if (messGestCO != null && !messGestCO.equals("")) {
            messGestCO = "</br><span style='color: rgb(203, 203, 203);font-size: 11px;'>" + messGestCO + "</span>";
        }

        realTimeService.add("Đơn hàng " + order.getId() + " vừa được đặt hàng", order.getSellerId(), "/" + order.getId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
        viewer.setCart(null);
        return new Response(true, "Đơn đặt hàng của bạn đã được chuyển đến người bán, người bán sẽ liên hệ với bạn trong trong thời gian sớm nhất" + messGestCO, error);
    }

    public long getItemPrice(Item item) {
        long price = item.getSellPrice();
        try {
            if (item.getListingType() == ListingType.BUYNOW) {
                if (item.isDiscount()) {
                    if (item.getDiscountPrice() > 0 && price - item.getDiscountPrice() > 0) {
                        price = price - item.getDiscountPrice();
                    } else if (item.getDiscountPercent() <= 100) {
                        price = price * (100 - item.getDiscountPercent()) / 100;
                        price = price * 1000;
                        price = (long) Math.ceil(price);
                        price = price / 1000;
                    }
                }
            } else {
                if (item.getEndTime() > System.currentTimeMillis()) {
                    if (item.getSellPrice() > 0) {
                        price = item.getSellPrice();
                    } else {
                        throw new Exception("Sản phẩm đấu giá không có giá mua ngay");
                    }
                } else if (item.getHighestBider().equals(viewer.getUser().getId())) {
                    price = item.getHighestBid();
                } else {
                    throw new Exception("Bạn không phải người đấu giá thắng của sản phẩm này");
                }
            }
        } catch (Exception e) {
        }

        return price;
    }

    public String payment(Order order, Seller seller, String baseUrl) throws Exception {
        if (!seller.isNlIntegrated()) {
            throw new Exception("Tài khoản của người bán chưa tích hợp NL");
        }
        if (seller.getNlEmail() == null || seller.getNlEmail().equals("")) {
            throw new Exception("Hệ thống không tìm thấy tài khoản ngân lượng của người bán, bạn hãy chọn hình thức thanh toán khác");
        }
        NlClient.MakePaymentRequest request = nlClient.new MakePaymentRequest();
        request.setBankCode(order.getPaymentMethod().toString());
        request.setBuyerAddress(order.getBuyerAddress());
        request.setBuyerEmail(order.getBuyerEmail());
        request.setBuyerName(order.getBuyerName());
        request.setBuyerPhone(order.getBuyerPhone());
        request.setCancelUrl(baseUrl + "/" + order.getId() + "/paymentcallback.html?cancel=true");
        request.setDiscountAmount(order.getCouponPrice());
        request.setItems(new ArrayList<NlClient.OrderItem>());
        long price = 0;
        for (OrderItem orderItem : order.getItems()) {
            price += orderItem.getItemPrice() * orderItem.getQuantity();
            request.getItems().add(nlClient.new OrderItem(UrlUtils.item(orderItem.getItemId(), orderItem.getItemName()), orderItem.getItemName(), orderItem.getQuantity(), orderItem.getItemPrice()));
        }
        if (order.getSellerDiscountPayment() > 0) {
            price = price - order.getSellerDiscountPayment();
        }

        request.setOrderDesctiption("Đơn hàng từ hệ thống Chợ điện tử");
        request.setOrderId(order.getId());
        request.setPaymentMethod("ATM_ONLINE");
        if (order.getPaymentMethod() == PaymentMethod.NL) {
            request.setPaymentMethod("NL");
        }
        if (order.getPaymentMethod() == PaymentMethod.VISA) {
            request.setPaymentMethod("VISA");
        }
        if (order.getPaymentMethod() == PaymentMethod.MASTER) {
            request.setPaymentMethod("VISA");
        }
        request.setReceiverEmail(seller.getNlEmail());
        request.setReturnUrl(baseUrl + "/" + order.getId() + "/paymentcallback.html");
        request.setShippingFee(order.getShipmentPrice() - order.getCdtDiscountShipment() - order.getSellerDiscountShipment());
        request.setTotalAmount(price);
        NlClient.MakePaymentResponse makePayment = nlClient.makePayment(request);
        if (!makePayment.isSuccess()) {
            return baseUrl + "/" + order.getId() + "/don-hang-thanh-cong.html";
        }
        Order find = orderRepository.find(order.getId());
        find.setToken(makePayment.getToken());
        orderRepository.save(find);
        return makePayment.getCheckoutUrl();
    }

    public int getOrderWeigh(Order order) {
        int weight = 0;
        for (OrderItem orderItem : order.getItems()) {
            weight += orderItem.getQuantity() * orderItem.getWeight();
        }
        return weight;
    }

    /**
     * Thanh toán
     *
     * @param orderId
     * @param token
     * @return
     * @throws Exception
     */
    public Order payment(String orderId, String token) throws Exception {
        Order order = this.get(orderId);
        order.setItems(orderItemRepository.getByOrderId(orderId));
        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            return order;
        }
        NlClient.CheckPaymentResponse checkPayment = nlClient.checkPayment(token);
        order.setNlId(checkPayment.getTransactionId());
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setMarkBuyerPayment(PaymentStatus.PAID);
        order.setMarkSellerPayment(PaymentStatus.PAID);
        order.setPaidTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);

        List<OrderItem> oItems = orderItemRepository.getByOrderId(orderId);
        if (oItems != null && !oItems.isEmpty()) {
            List<String> itemIds = new ArrayList<>();
            for (OrderItem orderItem : oItems) {
                itemIds.add(orderItem.getItemId());
            }
            List<Item> items = itemRepository.get(itemIds);
            for (Item item : items) {
                for (OrderItem orderItem : oItems) {
                    if (orderItem.getItemId().equals(item.getId())) {
                        if (item.getListingType() == ListingType.AUCTION) {
                            try {
                                auctionService.endBidByPayment(item.getId());
                            } catch (Exception e) {
                            }
                        } else {
                            item.setSoldQuantity(item.getSoldQuantity() + orderItem.getQuantity());
                            item.setQuantity(item.getQuantity() - orderItem.getQuantity());
                            if (item.getQuantity() < 0) {
                                item.setQuantity(0);
                            }
                        }
                        item.setUpdateTime(System.currentTimeMillis());
                        itemRepository.save(item);
                    }
                }
            }
            indexService.processIndexPageItem(items);
            int countShipment = 0;
            if (order.getNlId() != null || order.getScId() != null && order.getShipmentStatus() == ShipmentStatus.DELIVERED) {
                for (Item item : items) {
                    if (item.getShipmentPrice() == 0 && item.getShipmentType() == ShipmentType.FIXED) {
                        countShipment++;
                    }
                }
                if (countShipment > 0) {
                    sellerReviewService.intergrateReview(order.getSellerId(), "Miễn phí vận chuyển", 2, orderId, System.currentTimeMillis());
                }
            }
        }
        customerService.addCustomer(order);
        realTimeService.add("Đơn hàng " + order.getId() + " vừa được thanh toán", order.getSellerId(), "/" + order.getId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
        sellerHistoryService.create(SellerHistoryType.PAYMENT, order.getId(), true, 0, order.getSellerId());

        try {

            //Tang xeng, coupon Ebay 
            BigLanding bigLanding = bigLandingService.getExistCurent();
            if (bigLanding != null) {
                long createTime = order.getCreateTime();
                long startTime = bigLanding.getStartTime();
                long endTime = bigLanding.getEndTime();
                long currentTime = System.currentTimeMillis();
                long startTimeSeller = bigLanding.getStartTimeSeller();
                long endTimeSeller = bigLanding.getStartTimeSeller();
                if (createTime >= startTime && createTime <= endTime) {
                    if (order.getBuyerId() != null && !order.getBuyerId().equals("") && getOrderWeigh(order) <= 2000 && getOrderWeigh(order) > 0) {
                        Seller seller = sellerService.getById(order.getSellerId());
                        User user = userRepository.find(order.getBuyerId());
                        if (seller.isNlIntegrated() && seller.isScIntegrated() && user != null) {
                            String ebayCoupon = ebayCoupon();
                            if (!ebayCoupon.equals("")) {
                                sendMessageCoupon(order, baseUrl, ebayCoupon);
                            }

//                            if (order.getFinalPrice() >= 10000) {
//                                cashService.reward(CashTransactionType.EVENT_BIGLANDING, order.getBuyerId(), order.getId(), "/" + order.getId() + "/chi-tiet-don-hang.html", null, null);
//                            }
                        }
                    }
                }
            } else {
                cashService.reward(CashTransactionType.PAYMENT_SUSSESS_NL, viewer.getUser().getId(), order.getId(), "/" + order.getId() + "/chi-tiet-don-hang.html", null, null);
            }
        } catch (Exception e) {
        }
        return order;
    }

    /**
     * Tìm kiếm danh sách đơn hàng
     *
     * @param search
     * @return
     */
    public DataPage<Order> search(OrderSearch search) {
        Criteria cri = this.buildCriteria(search);
        Sort sort;
        switch (search.getSortOrderBy()) {
            case 1:
                sort = new Sort(Sort.Direction.ASC, "createTime");
                break;
            case 2:
                sort = new Sort(Sort.Direction.DESC, "createTime");
                break;
            case 3:
                sort = new Sort(Sort.Direction.ASC, "shipmentCreateTime");
                cri.and("scId").ne(null);
                break;
            case 4:
                sort = new Sort(Sort.Direction.DESC, "shipmentCreateTime");
                cri.and("scId").ne(null);
                break;
            default:
                sort = new Sort(Sort.Direction.DESC, "updateTime");
                break;
        }
        Query query = new Query(cri);
        DataPage<Order> dataPage = new DataPage<>();
        dataPage.setDataCount(orderRepository.count(query));
        dataPage.setPageIndex(search.getPageIndex());
        dataPage.setPageSize(search.getPageSize());
        dataPage.setPageCount(dataPage.getDataCount() / search.getPageSize());
        if (dataPage.getDataCount() % search.getPageSize() != 0) {
            dataPage.setPageCount(dataPage.getPageCount() + 1);
        }
        List<Order> list = orderRepository.find(query.limit(search.getPageSize()).skip(search.getPageIndex() * search.getPageSize()).with(sort));
        for (Order order : list) {
            List<OrderItem> orderItems = orderItemRepository.getByOrderId(order.getId());
            if (orderItems == null) {
                orderItems = new ArrayList<>();
            }
            List<String> itemIds = new ArrayList<>();
            for (OrderItem orderItem : orderItems) {
                itemIds.add(orderItem.getItemId());
            }
            order.setItems(orderItems);
            Map<String, List<String>> images = imageService.get(ImageType.ITEM, itemIds);
            for (Map.Entry<String, List<String>> entry : images.entrySet()) {
                String orderItemId = entry.getKey();
                List<String> img = entry.getValue();
                for (OrderItem orderItem : order.getItems()) {
                    if (orderItem.getItemId().equals(orderItemId)) {
                        orderItem.setImages(img);
                    }
                }

            }

        }
        dataPage.setData(list);
        return dataPage;
    }

    /**
     * *
     * Tìm kiếm danh sách đơn hàng
     *
     * @param createTimeFrom
     * @param createTimeTo
     * @param sellerId
     * @return
     */
    public List<Order> searchPromotionOders(long createTimeFrom, long createTimeTo, String sellerId) {
        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setCreateTimeFrom(createTimeFrom);
        orderSearch.setCreateTimeTo(createTimeTo);
        orderSearch.setSellerId(sellerId);
        Criteria cri = this.buildCriteria(orderSearch);
        Query query = new Query(cri);
        List<Order> orderList = orderRepository.find(query);
        return orderList;
    }

    public Criteria buildCriteria(OrderSearch search) {
        Criteria cri = new Criteria();
        List<String> userIds = new ArrayList<>();
        if (search.getSellerEmail() != null && !search.getSellerEmail().equals("")) {
            List<User> users = userRepository.find(new Query(new Criteria("email").regex(search.getSellerEmail(), "i")));
            for (User user : users) {
                userIds.add(user.getId());
            }
        }
        if (search.getSellerId() != null && !search.getSellerId().equals("")) {
            userIds.add(search.getSellerId());
        }
        if (search.getOrderShop() != null && !search.getOrderShop().equals("")) {
            userIds.add(search.getOrderShop());
        }
        if (search.getOrderNlSc() != null && !search.getOrderNlSc().equals("")) {
            userIds.add(search.getOrderNlSc());
        }
        if (search.getOrderNoNlSc() != null && !search.getOrderNoNlSc().equals("")) {
            userIds.add(search.getOrderNoNlSc());
        }

        if (search.getSellerPhone() != null && !search.getSellerPhone().equals("")) {
            List<User> users = userRepository.find(new Query(new Criteria("phone").is(search.getSellerPhone())));
            for (User user : users) {
                if (!userIds.contains(user.getId())) {
                    userIds.add(user.getId());
                }
            }
        }
        if (search.getSellerCityId() != null && !search.getSellerCityId().equals("0")) {
            Criteria criteria = new Criteria();
            List<String> userIdCheck = new ArrayList<>();
            List<Order> orders = orderRepository.find(new Query(new Criteria()));
            for (Order order : orders) {
                if (!userIdCheck.contains(order.getSellerId())) {
                    userIdCheck.add(order.getSellerId());
                }
            }
            criteria.and("cityId").is(search.getSellerCityId());
            if (search.getSellerDistrictId() != null && !search.getSellerDistrictId().equals("0")) {
                criteria.and("districtId").is(search.getSellerDistrictId());
            }
            criteria.and("_id").in(userIdCheck);
            List<User> users = userRepository.find(new Query(criteria));
            for (User user : users) {
                if (!userIds.contains(user.getId())) {
                    userIds.add(user.getId());
                }
            }
        }
        if (search.getReceiverCityId() != null && !search.getReceiverCityId().equals("0")) {
            cri.and("receiverCityId").is(search.getReceiverCityId());
            if (search.getReceiverDistrictId() != null && !search.getReceiverDistrictId().equals("0")) {
                cri.and("receiverDistrictId").is(search.getReceiverDistrictId());

            }
        }
        if (search.getBuyerCityId() != null && !search.getBuyerCityId().equals("0")) {
            cri.and("buyerCityId").is(search.getBuyerCityId());
            if (search.getBuyerDistrictId() != null && !search.getBuyerDistrictId().equals("0")) {
                cri.and("buyerDistrictId").is(search.getBuyerDistrictId());

            }
        }
        if (userIds != null && !userIds.isEmpty()) {
            cri.and("sellerId").in(userIds);
        }
        if (search.getBuyerId() != null && !search.getBuyerId().equals("")) {
            cri.and("buyerId").is(search.getBuyerId());
        }
        if (search.getItemId() != null && !search.getItemId().equals("")) {
            Criteria criOi = new Criteria();
            criOi.and("itemId").is(search.getItemId());

            List<OrderItem> orderItems = orderItemRepository.find(new Query(criOi));
            List<String> ids = new ArrayList<>();
            if (orderItems != null && !orderItems.isEmpty()) {
                for (OrderItem orderItem : orderItems) {
                    ids.add(orderItem.getOrderId());
                }
            }
            cri.and("id").in(ids);
        }
        if (search.getId() != null && !search.getId().equals("")) {
            cri.and("id").is(search.getId());
        } else if (search.getKeyword() != null && !search.getKeyword().equals("")) {
            Criteria criOi = new Criteria();
            criOi.and("itemName").regex(".*" + search.getKeyword().trim() + ".*", "i");

            List<OrderItem> orderItems = orderItemRepository.find(new Query(criOi));
            List<String> ids = new ArrayList<>();
            ids.add(search.getKeyword().trim());
            if (orderItems != null && !orderItems.isEmpty()) {
                for (OrderItem orderItem : orderItems) {
                    ids.add(orderItem.getOrderId());
                }
            }
            cri.and("id").in(ids);
        }
        if (search.getPaymentStatus() != null) {
            if (search.getPaymentStatus() != PaymentStatus.PAID) {
                if (search.getSellerId() != null && !search.getSellerId().equals("")) {
                    cri.and("markSellerPayment").ne(PaymentStatus.PAID.toString());
                }
                if (search.getBuyerId() != null && !search.getBuyerId().equals("")) {
                    cri.and("markBuyerPayment").ne(PaymentStatus.PAID.toString());
                }
            } else {
                if (search.getSellerId() != null && !search.getSellerId().equals("")) {
                    cri.and("markSellerPayment").is(PaymentStatus.PAID.toString());
                }
                if (search.getBuyerId() != null && !search.getBuyerId().equals("")) {
                    cri.and("markBuyerPayment").is(PaymentStatus.PAID.toString());
                }
            }
        }
        if (search.getShipmentStatus() != null) {
            if (search.getSellerId() != null && !search.getSellerId().equals("")) {
                if (search.getShipmentStatus() != ShipmentStatus.DELIVERED) {
                    cri.and("markSellerShipment").ne(ShipmentStatus.DELIVERED.toString());
                } else {
                    cri.and("markSellerShipment").is(ShipmentStatus.DELIVERED.toString());
                }
            }
            if (search.getBuyerId() != null && !search.getBuyerId().equals("")) {
                if (search.getShipmentStatus() != ShipmentStatus.DELIVERED) {
                    cri.and("markBuyerShipment").ne(ShipmentStatus.DELIVERED.toString());
                } else {
                    cri.and("markBuyerShipment").is(ShipmentStatus.DELIVERED.toString());
                }
            }
        }
        if (search.getRemove() > 0) {
            cri.and("remove").is(search.getRemove() == 1);
        } else if (search.getRemove() == 0) {

        } else {
            cri.and("remove").is(false);
        }
        if (search.getRemoveBuyer() > 0) {
            cri.and("removeBuyer").is(search.getRemoveBuyer() == 1);
        } else if (search.getRemoveBuyer() == 0) {

        } else {
            cri.and("removeBuyer").is(false);
        }
        if (search.getCreateTimeFrom() > 0 && search.getCreateTimeTo() > 0) {
            cri.and("createTime").gte(search.getCreateTimeFrom()).lt(search.getCreateTimeTo());
        }
        if (search.getShipmentPrice() < 0) {
            cri.and("shipmentPrice").lte(0);
            cri.and("scId").ne(null);
        }
        if (search.getShipmentCreateTimeFrom() > 0 || search.getShipmentCreateTimeTo() > 0) {
            if (search.getShipmentCreateTimeFrom() > 0 && search.getShipmentCreateTimeTo() > search.getShipmentCreateTimeFrom()) {
                cri.and("shipmentCreateTime").lte(search.getShipmentCreateTimeTo()).gte(search.getShipmentCreateTimeFrom());
            } else if (search.getShipmentCreateTimeFrom() > 0) {
                cri.and("shipmentCreateTime").gte(search.getShipmentCreateTimeFrom());
            } else {
                cri.and("shipmentCreateTime").lte(search.getShipmentCreateTimeTo());
            }
        }
        if (search.getScId() != null && !search.getScId().equals("")) {
            cri.and("scId").is(search.getScId());
        }
        if (search.getNlId() != null && !search.getNlId().equals("")) {
            cri.and("nlId").is(search.getNlId());
        }
        if (search.getPaidTimeFrom() > 0 && search.getPaidTimeTo() > 0) {
            cri.and("paidTime").gte(search.getPaidTimeFrom()).lt(search.getPaidTimeTo());
        }
        if (search.getShipmentUpdateTimeFrom() > 0 && search.getShipmentUpdateTimeTo() > 0) {
            cri.and("shipmentUpdateTime").gte(search.getShipmentUpdateTimeFrom()).lt(search.getShipmentUpdateTimeTo());
        }
        if (search.getReceiverEmail() != null && !search.getReceiverEmail().equals("")) {
            cri.and("receiverEmail").is(search.getReceiverEmail());
        }
        if (search.getReceiverPhone() != null && !search.getReceiverPhone().equals("")) {
            cri.and("receiverPhone").is(search.getReceiverPhone());
        }
        if (search.getReceiverName() != null && !search.getReceiverName().equals("")) {
            cri.and("receiverName").is(search.getReceiverName());
        }
        if (search.getBuyEmail() != null && !search.getBuyEmail().equals("")) {
            cri.and("buyerEmail").is(search.getBuyEmail());
        }
        if (search.getBuyPhone() != null && !search.getBuyPhone().equals("")) {
            cri.and("buyerPhone").is(search.getBuyPhone());
        }
        if (search.getBuyName() != null && !search.getBuyName().equals("")) {
            cri.and("buyerName").regex(search.getBuyName());
        }
        if (search.getPaymentMethod() > 0) {

            switch (search.getPaymentMethod()) {
                case 1:
                    cri.and("paymentMethod").is(PaymentMethod.COD.toString());
                    break;
                case 2:
                    Criteria c5 = new Criteria("paymentMethod").ne(PaymentMethod.COD.toString());
                    Criteria c6 = new Criteria("paymentMethod").ne(PaymentMethod.NONE.toString());
                    cri.andOperator(c5, c6);
                    break;
                case 3:
                case 4:
                    Criteria c1 = new Criteria("paymentMethod").ne(PaymentMethod.COD.toString());
                    Criteria c2 = new Criteria("paymentMethod").ne(PaymentMethod.NL.toString());
                    Criteria c3 = new Criteria("paymentMethod").ne(PaymentMethod.MASTER.toString());
                    Criteria c4 = new Criteria("paymentMethod").ne(PaymentMethod.NONE.toString());
                    cri.andOperator(c1, c2, c3, c4);
                    break;
                default:
                    cri.and("paymentMethod").is(PaymentMethod.NONE.toString());
                    break;
            }
        }
        if (search.getPaymentStatusSearch() > 0) {
            switch (search.getPaymentStatusSearch()) {
                case 1:
                    cri.and("paymentStatus").ne(PaymentStatus.PAID.toString());
                    break;
                case 2:
                    cri.and("paymentStatus").is(PaymentStatus.PAID.toString());
                    break;
                case 3:
                    cri.and("paymentStatus").is(PaymentStatus.PENDING.toString());
                    break;
                default:

            }
        }
        if (search.getShipmentStatusSearch() > 0) {
            switch (search.getShipmentStatusSearch()) {
                case 1:
                    cri.and("shipmentStatus").is(ShipmentStatus.NEW.toString());
                    break;
                case 2:
                    cri.and("shipmentStatus").is(ShipmentStatus.STOCKING.toString());
                    break;
                case 3:
                    cri.and("shipmentStatus").is(ShipmentStatus.DELIVERING.toString());
                    break;
                case 4:
                    cri.and("shipmentStatus").is(ShipmentStatus.RETURN.toString());
                    break;
                case 5:
                    cri.and("shipmentStatus").is(ShipmentStatus.DENIED.toString());
                    break;
                case 6:
                    cri.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
                    break;
                default:

            }
        }
        if (search.getRefundStatus() > 0) {
            switch (search.getRefundStatus()) {
                case 1:
                    cri.and("refundStatus").is(false);
                    break;
                case 2:
                    cri.and("refundStatus").is(true);
                    break;
                default:

            }
        }
        return cri;
    }

    /**
     * Xóa hoặc hoàn tác đơn hàng của người bán
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public Response remove(String orderId) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        order.setRemove(!order.isRemove());
        order.setUpdateTime(System.currentTimeMillis());
        // Hủy đơn hàng
        if (order.isRemove()) {
            sellerReviewService.intergrateReview(viewer.getUser().getId(), "Hủy đơn hàng người bán", -2, order.getId(), System.currentTimeMillis());
        }
        orderRepository.save(order);
        return new Response(true, "Đơn hàng đã được " + (order.isRemove() ? "đưa vào thùng rác" : "hoàn tác"));
    }

    /**
     * Xóa hoặc hoàn tác đơn hàng của người mua
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public Response removeBuyer(String orderId) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        order.setRemoveBuyer(!order.isRemoveBuyer());
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Đơn hàng đã được " + (order.isRemoveBuyer() ? "hủy và đưa vào thùng rác" : "hoàn tác"));
    }

    /**
     * Đánh dáu hoàn tiền
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    public Response refund(String orderId) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        order.setRefundStatus(!order.isRefundStatus());
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Đơn hàng đã được " + (order.isRefundStatus() ? " chuyển sang trạng thái hoàn tiền" : "hoàn tác"));
    }

    public Response refundAPI(String orderId) throws Exception {
        Order order = this.get(orderId);
        order.setRefundStatus(!order.isRefundStatus());
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Đơn hàng đã được " + (order.isRefundStatus() ? " chuyển sang trạng thái hoàn tiền" : "hoàn tác"));
    }

    private void authen(Order order) throws Exception {
        if (viewer.getUser() == null) {
            throw new Exception("Bạn cần đăng nhập để thực hiện thao tác này");
        }
        if (viewer.getAdministrator() == null && !viewer.getUser().getId().equals(order.getBuyerId()) && !viewer.getUser().getId().equals(order.getSellerId())) {
            throw new Exception("Bạn không có quyền thực hiện thao tác này");
        }
    }

    /**
     * Cập nhật ghi chú cho người bắn hoặc người mua
     *
     * @param orderId
     * @param note
     * @param seller
     * @return
     * @throws Exception
     */
    public Response note(String orderId, String note, boolean seller) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        note = note.trim();
        if (seller) {
            order.setSellerNote(note);
        } else {
            order.setNote(note);
        }
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Ghi chú đã được cập nhật vào đơn hàng", order);
    }

    /**
     * Đánh dấu trạng thái thanh toán
     *
     * @param orderId
     * @param status
     * @param seller
     * @return
     * @throws Exception
     */
    public Response markPaymentStatus(String orderId, PaymentStatus status, boolean seller) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        if (seller) {
            order.setMarkSellerPayment(status);
        } else {
            order.setMarkBuyerPayment(status);
        }
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Trạng thái thanh toán đã được cập nhật vào đơn hàng");
    }

    public Response markPaymentStatusAPI(String orderId, PaymentStatus status, boolean seller) throws Exception {
        Order order = this.get(orderId);
        if (seller) {
            order.setMarkSellerPayment(status);
        } else {
            order.setMarkBuyerPayment(status);
        }
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Trạng thái thanh toán đã được cập nhật vào đơn hàng");
    }

    /**
     * Dánh dấu trạng thái vận chuyển
     *
     * @param orderId
     * @param status
     * @param seller
     * @return
     * @throws Exception
     */
    public Response markShipmentStatus(String orderId, ShipmentStatus status, boolean seller) throws Exception {
        Order order = this.get(orderId);
        this.authen(order);
        if (seller) {
            order.setMarkSellerShipment(status);
        } else {
            order.setMarkBuyerShipment(status);
        }
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Trạng thái vận chuyển đã được cập nhật vào đơn hàng");
    }

    public Response markShipmentStatusAPI(String orderId, ShipmentStatus status, boolean seller) throws Exception {
        Order order = this.get(orderId);
        if (seller) {
            order.setMarkSellerShipment(status);
        } else {
            order.setMarkBuyerShipment(status);
        }
        order.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(order);
        return new Response(true, "Trạng thái vận chuyển đã được cập nhật vào đơn hàng");
    }

    public Response sendMessage(String orderId, String message, boolean seller) throws Exception {
        return new Response(false, "Chức năng đang trong quá trình hoàn thành");
    }

    public Order clone(Order order) {
        try {
            Order nOrder = order.clone();
            nOrder.setId(orderRepository.genId());
            for (OrderItem orderItem : nOrder.getItems()) {
                orderItem.setId(orderItemRepository.genId());
            }
            return nOrder;
        } catch (CloneNotSupportedException ex) {
            return order;
        }
    }

    public Response checkBigLading(Lading form) throws Exception {
        Order order = this.getOrder(form.getOrderId());
        try {
            Seller seller = sellerService.getById(viewer.getUser().getId());
            int weight = form.getWeight();
            if (weight <= 2000 && seller.isScIntegrated() && seller.isNlIntegrated()) {
                BigLanding bigLanding = bigLandingService.getExistCurent();
                if (bigLanding != null) {
                    long createTime = order.getCreateTime();
                    long startTime = bigLanding.getStartTime();
                    long endTime = bigLanding.getEndTime();
                    long currentTime = System.currentTimeMillis();
                    long startTimeSeller = bigLanding.getStartTimeSeller();
                    if (createTime >= startTime && createTime <= endTime) {
                        if (currentTime >= startTime && currentTime < startTimeSeller) {
                            return new Response(true, bigLanding.getDescription(), order);
                        }
                    }
                }

            }
        } catch (Exception e) {
            return new Response(false, "FAIL", order);
        }
        return new Response(false, "", order);
    }

    public Response createLading(Lading form) throws Exception {

        Seller seller = sellerService.getById(viewer.getUser().getId());

        if (!seller.isScIntegrated()) {
            throw new Exception("Tài khoản của bạn chưa được tích hợp hình thức vận chuyển qua shipchung");
        }
        if (seller.getScEmail() == null || seller.getScEmail().equals("")) {
            throw new Exception("Tài khoản email tích hợp vận chuyển qua ship chung không tồn tại");
        }

        if (seller.getMerchantKey() == null || seller.getMerchantKey().equals("")) {

            throw new Exception("Tài khoản phải tích hợp lại với shipchung");
        }
        Order order = this.getOrder(form.getOrderId());
        if (order.getScId() != null && !order.getScId().equals("")) {
            throw new Exception("Đơn hàng của bạn đã được tạo vận đơn trên shipchung, mã vận đơn : " + order.getScId());
        }
        String merchantKey = seller.getMerchantKey();
        this.authen(order);

        Map<String, String> error = validator.validate(form);
        if (form.getSellerCityId() == null || form.getSellerCityId().equals("0")) {
            error.put("sellerCityId", "Địa chỉ tỉnh,thành phố người bán không được để trống");
        }
        if (form.getSellerDistrictId() == null || form.getSellerDistrictId().equals("0")) {
            error.put("sellerDistrictId", "Địa chỉ quận,huyện người bán không được để trống");
        }
        if (form.getReceiverCityId() == null || form.getReceiverCityId().equals("0")) {
            error.put("receiverCityId", "Địa chỉ tỉnh,thành phố người nhận không được để trống");
        }
        if (form.getCourierId() == 0) {
            error.put("courierId", "Hãng vận chuyển không được để trống");
        }
        if (form.getReceiverDistrictId() == null || form.getReceiverDistrictId().equals("0")) {
            error.put("receiverDistrictId", "Địa chỉ quận,huyện người nhận không được để trống");
        } else {
            if (form.getReceiverWardId() == null || form.getReceiverWardId().equals("0")) {
                if (scClient.checkDistrict(order.getReceiverDistrictId())) {
                    error.put("receiverWardId", "Địa chỉ phường,xã người nhận không được để trống");
                }
            }
        }
        if (form.getReceiverWardId() == null) {
            form.setReceiverWardId("0");
        }
        if (!error.isEmpty()) {
            return new Response(false, "Thông tin chưa đầy đủ", error);
        }

        order.setReceiverAddress(form.getReceiverAddress());
        order.setReceiverCityId(form.getReceiverCityId());
        order.setReceiverDistrictId(form.getReceiverDistrictId());
        order.setReceiverWardId(form.getReceiverWardId());
        order.setReceiverEmail(form.getReceiverEmail());
        order.setReceiverName(form.getReceiverName());
        order.setReceiverPhone(form.getReceiverPhone());
        order.setCodPrice(form.getCodPrice());
        order.setCourierId(form.getCourierId());

        List<String> local = new ArrayList<>();
        local.add(form.getSellerCityId());
        local.add(form.getReceiverCityId());
        List<City> citys = cityService.getCityByIds(local);
        for (City city : citys) {
            if (city.getId().equals(form.getSellerCityId())) {
                form.setSellerCityId(city.getScId());
                District district = districtService.get(form.getSellerDistrictId());
                if (district != null) {
                    form.setSellerDistrictId(district.getScId());
                }
            }
            if (form.getReceiverCityId().equals(city.getId())) {
                form.setReceiverCityId(city.getScId());
                District district = districtService.get(form.getReceiverDistrictId());
                if (district != null) {
                    form.setReceiverDistrictId(district.getScId());
                    if (!form.getReceiverWardId().equals("") && !form.getReceiverWardId().equals("0")) {
                        Ward ward = wardService.get(order.getReceiverWardId());
                        if (ward != null) {
                            form.setReceiverWardId(ward.getScId());
                        }
                    }
                }
            }
        }
        int weight = form.getWeight();
        ShipmentService shipmentService = form.getShipmentService();
        if (shipmentService == ShipmentService.FAST && seller.isScIntegrated() && seller.isNlIntegrated()) {
            BigLanding bigLanding = bigLandingService.getExistCurentSeller();
            if (bigLanding != null) {
                long createTime = order.getCreateTime();
                long startTime = bigLanding.getStartTime();
                long endTime = bigLanding.getEndTime();
                long currentTime = System.currentTimeMillis();
                long startTimeSeller = bigLanding.getStartTimeSeller();
                long endTimeSeller = bigLanding.getEndTimeSeller();
                if (createTime >= startTime && createTime <= endTime) {
                    if (currentTime >= startTimeSeller && currentTime <= endTimeSeller) {
                        if (weight > 2000) {
                            form.setName(form.getName() + " [FD0]");
                        } else {
                            form.setName(form.getName() + " [FD100]");
                        }
                    }

                }
            }
        }
        List<OrderItem> orderItems = order.getItems();
        boolean payment = true;
        if (order.getPaymentMethod().equals(PaymentMethod.COD) || order.getPaymentMethod().equals(PaymentMethod.NONE)) {
            payment = false;
        }
        String orderId = order.getId();

        long balance = scClient.balance(merchantKey, form.getType() == PaymentMethod.COD ? 1 : 2);
        if (form.getShipmentPrice() > balance) {
            return new Response(false, "Số tiền trong tài khoản ShipChung của bạn không đủ. <a target='_blank' href='http://www.shipchung.vn/huong-dan-nap-tien-vao-tai-khoan-shipchung-tren-thong-moi-tu-1222015/'>Hướng dẫn nạp tiền</a>");
        }
        int orderCount = orderRepository.getOrderCount(form.getOrderId());
        if (orderCount != 1) {
            //order.setCount(0);
            //orderRepository.save(order);
            throw new Exception("Vận đơn đang được xử lý bạn vui lòng đợi trong giây lát..");
        }
        Response<ScClientV2.FeeShip> createShipment = scClient.createShipment(orderItems, payment, seller.getScEmail(), seller.getScId(),
                form.getShipmentService(),
                form.getSellerName(), viewer.getUser().getEmail(),
                form.getSellerPhone(), form.getSellerAddress(),
                form.getSellerCityId(), form.getSellerDistrictId(),
                form.getReceiverName(), form.getReceiverEmail(),
                form.getReceiverPhone(), form.getReceiverAddress(),
                form.getReceiverCityId(), form.getReceiverDistrictId(), form.getReceiverWardId(),
                form.getName(), form.getDescription(),
                order.getTotalPrice(),
                order.getCodPrice(),
                (order.getCdtDiscountShipment() > 0 ? order.getShipmentPrice() - order.getCdtDiscountShipment() : -1),
                form.getWeight(), form.getType() == PaymentMethod.COD, form.isProtec(), form.getCourierId(), merchantKey, orderId);
        if (!createShipment.isSuccess()) {
            order.setCount(0);
            orderRepository.save(order);
            return createShipment;
        }
        try {
            form.setShipmentStatus(ShipmentStatus.NEW);
            order.setMarkBuyerShipment(ShipmentStatus.NEW);
            order.setMarkSellerShipment(ShipmentStatus.NEW);
            form.setScId(createShipment.getMessage());
            form.setShipmentPrice(createShipment.getData().getShip());
            form.setShipmentPCod(createShipment.getData().getPcod());
            form.setCreateTime(System.currentTimeMillis());
            form.setUpdateTime(System.currentTimeMillis());

            if (form.getType() == PaymentMethod.COD) {
//                order.setPaymentMethod(form.getType());
            } else {
                //form.setType(order.getPaymentMethod());
            }
            ladingRepository.save(form);
            order.setShipmentCreateTime(System.currentTimeMillis());
            order.setShipmentUpdateTime(System.currentTimeMillis());
            //order.setShipmentService(form.getShipmentService());
            order.setScId(createShipment.getMessage());
            order.setShipmentStatus(ShipmentStatus.NEW);
//            order.setShipmentPrice(createShipment.getData().getShip());
            order.setShipmentPCod(createShipment.getData().getPcod());
            scClient.accept(createShipment.getMessage(), merchantKey);

        } catch (Exception e) {
        }
        orderRepository.save(order);
        sellerHistoryService.create(SellerHistoryType.LADING, order.getId(), true, 0, viewer.getUser().getId());
        try {
            // Duyệt vận đơn từ 3-6h
            long createTime = order.getCreateTime() + 6 * 60 * 60 * 1000;
            long timeCurrent = System.currentTimeMillis();
            if (timeCurrent < createTime) {
                sellerReviewService.intergrateReview(viewer.getUser().getId(), "Duyệt vận đơn (3-6h)", 2, order.getId(), System.currentTimeMillis());
            }
            cashService.reward(CashTransactionType.BROWSE_LADING, viewer.getUser().getId(), order.getId(), "/" + order.getId() + "/chi-tiet-don-hang.html", null, null);
            return new Response(true, "Vận đơn COD đã được gửi sang shipchung", order);
        } catch (Exception e) {
            return new Response(true, "BROWSE_LADING_FAIL", order);
        }

    }

    /**
     * Đếm số lượng hóa đơn đơn hàng theo trạng thái thanh toán
     *
     * @param paymentStatus
     * @param userId
     * @param seller
     * @return
     * @throws Exception
     */
    public long countByPaymentStatus(PaymentStatus paymentStatus, boolean seller, String userId) throws Exception {
        return orderRepository.countByPaymentStatus(paymentStatus, seller, userId);
    }

    /**
     * Đếm số lượng hóa đơn đơn hàng theo trạng thái thanh toán
     *
     * @param shipmentStatus
     * @param userId
     * @param seller
     * @return
     * @throws Exception
     */
    public long countByShipmentStatus(ShipmentStatus shipmentStatus, boolean seller, String userId) throws Exception {
        return orderRepository.countByShipmentStatus(shipmentStatus, seller, userId);
    }

    /**
     * Đếm số lượng hóa đơn đơn hàng theo trạng thái thanh toán
     *
     * @param remove
     * @param userId
     * @param seller
     * @return
     * @throws Exception
     */
    public long countByRemove(boolean remove, boolean seller, String userId) throws Exception {
        return orderRepository.countByRemove(remove, seller, userId);
    }

    /**
     * Đếm số lượng hóa đơn đơn hàng
     *
     * @param userId
     * @param seller
     * @return
     * @throws Exception
     */
    public long countByOrderAll(boolean seller, String userId) throws Exception {
        return orderRepository.countByOrderAll(seller, userId);
    }

    // @Scheduled(fixedDelay = 10 * 60 * 1000)
    //@Scheduled(fixedDelay = 1000)
    public void run() {
        while (true) {
            Order order = orderRepository.getThread();
            if (order == null) {
                break;
            }
            order.setShipmentUpdateTime(System.currentTimeMillis());
            orderRepository.save(order);
            this.processShipment(order);
        }
    }

    @Async
    public void processShipment(Order order) {
        try {
            Response<ShipmentStatus> status = scClient.status(order.getScId());
            status.setData(ShipmentStatus.DELIVERED);
            status.setSuccess(true);
            if (status.isSuccess()) {
                order.setShipmentMessage(status.getMessage());
                order.setShipmentStatus(status.getData());
                order.setMarkBuyerShipment(status.getData());
                order.setMarkSellerShipment(status.getData());
                order.setShipmentUpdateTime(System.currentTimeMillis());
                orderRepository.save(order);

                if (status.getData() == ShipmentStatus.DELIVERED) {

                    List<OrderItem> oItems = orderItemRepository.getByOrderId(order.getId());
                    if (oItems != null && !oItems.isEmpty()) {
                        List<String> itemIds = new ArrayList<>();
                        for (OrderItem orderItem : oItems) {
                            itemIds.add(orderItem.getItemId());
                        }
                        List<Item> items = itemRepository.get(itemIds);
                        for (Item item : items) {
                            for (OrderItem orderItem : oItems) {
                                if (orderItem.getItemId().equals(item.getId())) {
                                    if (item.getListingType() == ListingType.AUCTION) {
                                        try {
                                            auctionService.endBidByPayment(item.getId());
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        item.setSoldQuantity(item.getSoldQuantity() + orderItem.getQuantity());
                                        item.setQuantity(item.getQuantity() - orderItem.getQuantity());
                                        if (item.getQuantity() < 0) {
                                            item.setQuantity(0);
                                        }
                                    }
                                    item.setUpdateTime(System.currentTimeMillis());
                                    itemRepository.save(item);
                                }
                            }
                        }
                        indexService.processIndexPageItem(items);
                    }
                    BigLanding bigLanding = bigLandingService.getExistCurent();
                    /**
                     * Tang 1% xeng gia tri don hang
                     *
                     */
                    if (order.getPaymentStatus() != PaymentStatus.PAID) {

                        if (bigLanding == null) {
                            cashService.reward(CashTransactionType.COD_DELIVERED, order.getBuyerId(), order.getId(), "/" + order.getId() + "/chi-tiet-don-hang.html", null, null);
                        }
                    }
                }

                Lading lading = ladingRepository.get(order.getId());
                if (lading != null) {
                    // Giao hàng thành công và cập nhật trạng thái ship chung
                    sellerReviewService.intergrateReview(order.getSellerId(), "Giao hàng thành công (Cập nhật trạng thái SC)", 2, order.getId(), System.currentTimeMillis());
                    lading.setShipmentStatus(status.getData());
                    lading.setUpdateTime(System.currentTimeMillis());
                    ladingRepository.save(lading);
                }
                if (order.getSellerId() != null && !order.getSellerId().equals("")) {
                    realTimeService.add("Đơn hàng " + order.getId() + " " + order.getShipmentMessage(), order.getSellerId(), "/" + order.getId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
                }
                if (order.getBuyerId() != null && !order.getBuyerId().equals("")) {
                    realTimeService.add("Đơn hàng " + order.getId() + " " + order.getShipmentMessage(), order.getBuyerId(), "/" + order.getId() + "/chi-tiet-don-hang.html", "Chi tiết đơn hàng", null);
                }
                /**
                 * Tang xeng, coupon Ebay khi don hang thanh cong
                 */
                ShipmentService shipmentService = lading.getShipmentService();
                Seller seller = sellerService.getById(order.getSellerId());
                boolean isFD = false;
                //shipmentService == ShipmentService.FAST && 
                if (shipmentService == ShipmentService.FAST && seller.isScIntegrated() && seller.isNlIntegrated()) {
                    if (lading.getName().endsWith("[FD100]") || lading.getName().endsWith("[FD0]")) {

                        isFD = true;

                    }
                }
                if (isFD && status.getData() == ShipmentStatus.DELIVERED) {
//                    if (order.getPaymentStatus() != PaymentStatus.PAID) {
                    if (order.getBuyerId() != null && !order.getBuyerId().equals("")) {
                        String ebayCoupon = ebayCoupon();
                        if (!ebayCoupon.equals("")) {
                            sendMessageCoupon(order, baseUrl, ebayCoupon);
                        }
                        // 200k tro len moi dc tang xeng
                        if (order.getFinalPrice() >= 200000 && order.getPaymentStatus() != PaymentStatus.PAID) {

                            try {
                                cashService.reward(CashTransactionType.EVENT_BIGLANDING, order.getBuyerId(), order.getId(), "/" + order.getId() + "/chi-tiet-don-hang.html", null, null);
                            } catch (Exception e) {

                            }
                        }
                    }
//                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public String ebayCoupon() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://www.ebay.vn/backservice/api/genCDTCoupon");
            post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, Object> params = new HashMap<>();
            params.put("hash", "b3d9b0d4e1a7291b22b26302e76a7a41c82bcc25");
            List<NameValuePair> urlParameters = new ArrayList<>();
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    urlParameters.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
            HttpResponse response = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                text.append(line);
            }
            JSONObject jSONObject = new JSONObject(text.toString());
            if ((boolean) jSONObject.get("status").equals(true)) {
                return jSONObject.get("data").toString();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public void sendMessageCreateOrder(Order order, String baseUrl) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("username", order.getBuyerName());
            data.put("message", "Bạn vừa tạo thành công 1 đơn hàng trên hệ thống chợ điện tử <br> "
                    + "Mã đơn hàng : <a href='" + baseUrl + "/" + order.getId() + "/chi-tiet-don-hang.html' >" + order.getId().toUpperCase() + "</a>");
            emailService.send(EmailOutboxType.CREATE_ORDER_BUYER,
                    order.getBuyerEmail(),
                    "[Chợ điện tử] Thông báo đặt hàng thành công (" + order.getId() + ")",
                    "createorder", data);

            User seller = userService.get(order.getSellerId());
            String userName = "";
            if (seller.getName() == null || seller.getName().equals("")) {
                if (seller.getUsername() == null || seller.getUsername().equals("")) {
                    userName = seller.getEmail();
                } else {
                    userName = seller.getUsername();
                }
            } else {
                userName = seller.getName();
            }
            data.put("username", userName);
            data.put("message", "Người mua " + order.getBuyerName() + " vừa tạo thành công 1 đơn hàng trên hệ thống chợ điện tử <br> "
                    + "Mã đơn hàng : <a href='" + baseUrl + "/" + order.getId() + "/chi-tiet-don-hang.html' >" + order.getId().toUpperCase() + "</a>");
            emailService.send(EmailOutboxType.CREATE_ORDER_SELLER,
                    seller.getEmail(),
                    "[Chợ điện tử] Người mua " + order.getBuyerName() + " vừa đặt một đơn hàng (" + order.getId() + ")",
                    "createorder", data);

            smsService.send(seller.getPhone().trim(), "Khach hang " + order.getBuyerName() + " vua dat mua 1 don hang co ma hoa don: " + order.getId()
                    + ", gia tri thanh toan "
                    + TextUtils.numberFormat(Double.parseDouble(order.getFinalPrice() + "")) + " d tu ChoDienTu.vn",
                    SmsOutboxType.CREATE_ORDER_SELLER, System.currentTimeMillis() + 30 * 1000, 1);
            String priceSC = "";
            String ttOnline = "";
            if (order.getShipmentPrice() > 0) {
                priceSC = "; Phi van chuyen " + TextUtils.numberFormat(Double.parseDouble(order.getShipmentPrice() + "")) + "d";
            }
            int weightItem = 0;
            List<OrderItem> byOrderId = orderItemRepository.getByOrderId(order.getId());
            if (byOrderId != null && !byOrderId.isEmpty()) {
                boolean fag = true;
                for (OrderItem orderItem : byOrderId) {
                    weightItem += orderItem.getWeight();
                    if (orderItem.getShipmentType() != ShipmentType.BYWEIGHT) {
                        fag = false;
                        break;
                    }
                }
                if (fag && weightItem <= 2000) {
                    ttOnline = ";Ban nen thanh toan online de duoc mien phi van chuyen";
                }
            }

            smsService.send(order.getBuyerPhone().trim(), "Ban dat mua don hang co ma hoa don:" + order.getId()
                    + ", tri gia "
                    + TextUtils.numberFormat(Double.parseDouble(order.getTotalPrice() + "")) + "d" + priceSC + ttOnline,
                    SmsOutboxType.CREATE_ORDER_BUYER, System.currentTimeMillis() + 30 * 1000, 1);

        } catch (Exception ex) {
            order.setNoteError("Lỗi khi gửi mail và sms : " + ex.getMessage());
            orderRepository.save(order);
        }

    }

    public void sendMessageCoupon(Order order, String baseUrl, String coupon) {
        try {
            Map<String, Object> data = new HashMap();
            data.put("username", order.getBuyerName());
            data.put("message", "Bạn vừa được tặng Coupon Ebay.vn trị giá 100K nhờ mua hàng trên hệ thống chợ điện tử <br> "
                    + "Mã coupon Ebay.vn: " + coupon + " (Thời hạn 1 tháng kể từ ngày 19/01/2015)<br>"
                    + "Mã đơn hàng : <a href='" + baseUrl + "/" + order.getId() + "/chi-tiet-don-hang.html' >" + order.getId().toUpperCase() + "</a>");
            emailService.send(EmailOutboxType.COUPON_ORDER_SELLER,
                    order.getBuyerEmail(),
                    "[Chợ điện tử] Thông tin tặng Coupon Ebay.vn",
                    "message", data);
            String title = "Tặng coupon Ebay.vn trị giá 100K";
            String content = "Bạn vừa được tặng Coupon Ebay.vn trị giá 100K nhờ mua hàng trên hệ thống chợ điện tử <br> "
                    + "Mã coupon Ebay.vn: " + coupon + " (Thời hạn 1 tháng kể từ ngày 19/01/2015)<br>"
                    + "Mã đơn hàng : <a href='" + baseUrl + "/" + order.getId() + "/chi-tiet-don-hang.html' >" + order.getId().toUpperCase() + "</a>";
            sendMessage(order.getBuyerEmail(), title, content, order.getId(), null);

        } catch (Exception ex) {
        }

    }

    public Response sendMessage(String to, String subject, String content, String orderId, String itemId) throws Exception {
        User user = userRepository.getByEmail(to);
        HashMap<String, String> error = new HashMap<>();
        String emailTo = "";
        if (user == null) {
//            error.put("toEmail", "Không tìm thấy địa chỉ email người nhận");
            // get email from order 
            Order order = get(orderId);
            if (order == null) {
                error.put("toEmail", "Không tìm thấy địa chỉ email người nhận.");
            } else {
                emailTo = order.getBuyerEmail();
            }
        } else {
            emailTo = user.getEmail();
        }
        if (!error.isEmpty()) {
            return new Response(false, null, error);
        }
        Message message = new Message();
        message.setCreateTime(System.currentTimeMillis());
        message.setContent(content);
        message.setFromEmail("no-reply@chodientu.vn");
        message.setFromName("Chợ điện tử");
        message.setFromUserId("");
        message.setItemId(itemId);
        message.setLastIp("");
        //message.setLastIp(TextUtils.getClientIpAddr(request));
        message.setOrderId(orderId);
        message.setLastView(System.currentTimeMillis());
        message.setRead(false);
        message.setRemove(false);
        message.setSubject(subject);
        message.setToEmail(emailTo);
        message.setToName(user.getName() != null ? user.getName() : (user.getUsername() != null ? user.getUsername() : user.getEmail()));
        message.setToUserId(user.getId());
        message.setUpdateTime(System.currentTimeMillis());
        messageRepository.save(message);

        realTimeService.add("Bạn vừa nhận được 1 tin nhắn từ " + message.getFromName(), message.getToUserId(), "/user/quan-ly-thu.html", "Xem tin nhắn", null);

        return new Response(true, "Gửi tin nhắn thành công", message);
    }

    private void sendMessagePayment(Order order) {
    }

    /**
     * Tính tổng tiền theo điều kiện
     *
     * @param search
     * @return
     */
    public Map<String, Long> sumPrice(OrderSearch search) {
        return orderRepository.sumPrice(this.buildCriteria(search));
    }

    /**
     * Danh sách đơn hàng theo ids
     *
     * @param ids
     * @return
     */
    public List<Order> findByIds(List<String> ids) throws Exception {
        List<Order> orders = orderRepository.find(new Query(new Criteria("_id").in(ids)));
        List<OrderItem> orderItems = orderItemRepository.find(new Query(new Criteria("orderId").in(ids)));
        String error = null;
        for (Order order : orders) {
            if (order.getScId() != null) {
                if (error == null) {
                    error = "Đơn hàng ";
                }
                error += order.getId() + ", ";
            }
            if (order.getItems() == null) {
                order.setItems(new ArrayList<OrderItem>());
            }
            for (OrderItem orderItem : orderItems) {
                if (order.getId().equals(orderItem.getOrderId())) {
                    order.getItems().add(orderItem);
                }
            }
        }
        if (error != null) {
            error += " đã tồn tại mã vận chuyện, bạn vui lòng kiểm tra lại";
            throw new Exception(error);
        }
        return orders;
    }

    /**
     * Viết ra nhằm mục đích test hàm API
     *
     * @param ids
     * @return
     */
    public List<OrderItem> getOrderItems(List<String> ids) {
        Criteria criteria = new Criteria();
        criteria.and("orderId").in(ids);
        criteria.and("shipmentType").is(ShipmentType.BYWEIGHT.toString());
        List<OrderItem> orderItems = orderItemRepository.find(new Query(criteria));
        return orderItems;
    }

    public long countBuyerUnique(boolean all) {
        return orderRepository.countBuyerUnique(all);
    }

    public long countBuyerReturn(boolean all) {
        return orderRepository.countBuyerReturn(all);
    }

    public long countReturnedSeller(boolean all) {
        return orderRepository.countReturnedSeller(all);
    }

    public long countReturnedSeller(boolean all, long time) {
        return orderRepository.countReturnedSeller(all, time);
    }

    public long countBuyerSCIntegrated(boolean all) {
        long countResult = 0;
        List<String> listBuyerIdUnique = orderRepository.listBuyerIdUnique(all);
        for (String buyerId : listBuyerIdUnique) {
            Seller seller = sellerService.getSelletSC(buyerId);
            if (seller != null) {
                countResult++;
            }

        }
        return countResult;

    }

    public long countBuyerOnce(boolean all) {
        return orderRepository.countBuyerOnce(all);
    }

    public long countSuccessSeller(boolean all) {
        return orderRepository.countSuccessSeller(all);
    }

    public long countSuccessSeller(boolean all, long time) {
        return orderRepository.countSuccessSeller(all, time);
    }

    public long countFirstSuccessSeller(boolean all) {
        return orderRepository.countFirstSuccessSeller(all);
    }

    public long countFirstSuccessSeller(boolean all, long time) {
        return orderRepository.countFirstSuccessSeller(all, time);
    }

    public List<User> getBuyerIdFromOrder(long startTime, long endTime) {
        List<String> orders = orderRepository.getBuyerIdFromOrder(startTime, endTime);
        List<String> userId = new ArrayList<>();
        for (String order : orders) {
            userId.add(order);
        }
        List<User> users = userRepository.get(userId);
        return users;
    }

    public long countTotalSuccessSeller(long startTime, long endTime) {
        return orderRepository.countTotalSuccessSeller(startTime, endTime);
    }

    public long countTotalReturnedSeller(long startTime, long endTime) {
        return orderRepository.countReturnedSeller(startTime, endTime);
    }

    public long countTotalOrderedSeller(long startTime, long endTime) {
        return orderRepository.countTotalOrderedSeller(startTime, endTime);
    }

    public long countBuyerUnique(long startTime, long endTime) {
        return orderRepository.countBuyerUnique(startTime, endTime);
    }

    public long getTotalBuyerReturn(long startTime, long endTime) {
        return orderRepository.getTotalBuyerReturn(startTime, endTime);
    }

    public long getTotalBuyerSC(long startTime, long endTime) {
        long countResult = 0;
        List<String> listBuyerIdUnique = orderRepository.listBuyerIdUnique(startTime, endTime);
        for (String buyerId : listBuyerIdUnique) {
            Seller seller = sellerService.getSelletSC(buyerId);
            if (seller != null) {
                countResult++;
            }
        }
        return countResult;
    }

    public long getTotalSoldBuyers(long startTime, long endTime) {
        return orderRepository.getTotalSoldBuyers(startTime, endTime);
//        return 0;
    }

    /**
     * *
     * Lấy ra list orderItem
     *
     * @param orderIds
     * @return
     */
    public List<OrderItem> getOrderItemsByOrderIds(List<String> orderIds) {
        Criteria criteria = new Criteria();
        criteria.and("orderId").in(orderIds);
        return orderItemRepository.find(new Query(criteria));
    }

}
