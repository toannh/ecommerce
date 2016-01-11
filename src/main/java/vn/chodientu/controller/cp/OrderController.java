package vn.chodientu.controller.cp;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.db.Shop;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.input.OrderSearch;
import vn.chodientu.entity.output.DataPage;
import vn.chodientu.entity.output.Response;
import vn.chodientu.repository.OrderItemRepository;
import vn.chodientu.service.CityService;
import vn.chodientu.service.DistrictService;
import vn.chodientu.service.OrderService;
import vn.chodientu.service.SellerService;
import vn.chodientu.service.ShopService;
import vn.chodientu.service.UserService;
import vn.chodientu.util.TextUtils;

@Controller("cpOrder")
@RequestMapping("/cp/order")
public class OrderController extends BaseCp {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private UserService userService;
    @Autowired
    private CityService cityService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private Gson gson;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(ModelMap map, HttpSession session, @RequestParam(value = "page", defaultValue = "1") int page) throws Exception {
        OrderSearch orderSearch = new OrderSearch();
        if (session.getAttribute("orderSearch") != null && page != 0) {
            orderSearch = (OrderSearch) session.getAttribute("orderSearch");
        } else {
            session.setAttribute("orderSearch", orderSearch);
        }
        orderSearch.setPageIndex(page - 1);
        orderSearch.setPageSize(30);
        orderSearch.setRemove(0);
        DataPage<Order> dataPage = orderService.search(orderSearch);
        List<Order> data = dataPage.getData();
        List<String> sellerIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (Order order : data) {
                sellerIds.add(order.getSellerId());
                orderIds.add(order.getId());
            }
        }
        List<User> users = userService.getUserByIds(sellerIds);
        if (users != null && !users.isEmpty()) {
            for (Order order : data) {
                for (User user : users) {
                    if (user.getId().equals(order.getSellerId())) {
                        order.setUser(user);
                    }
                }
            }
        }
        Map<String, Long> sumPrice = orderService.sumPrice(orderSearch);
        map.put("orderSearch", orderSearch);
        map.put("sumPrice", sumPrice);
        map.put("dataPage", dataPage);
        map.put("clientScript", "var citys = " + gson.toJson(cityService.list())
                + "; var districts = " + gson.toJson(districtService.list())
                + "; var orderIds = " + gson.toJson(orderIds)
                + "; order.init();");
        return "cp.order.list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String search(ModelMap map, HttpSession session, @ModelAttribute OrderSearch orderSearch) {
        session.setAttribute("orderSearch", orderSearch);
        orderSearch.setPageIndex(0);
        orderSearch.setPageSize(30);
        orderSearch.setRemove(0);
        DataPage<Order> dataPage = orderService.search(orderSearch);
        List<Order> data = dataPage.getData();
        List<String> sellerIds = new ArrayList<>();
        List<String> orderIds = new ArrayList<>();
        if (data != null && !data.isEmpty()) {
            for (Order order : data) {
                sellerIds.add(order.getSellerId());
                orderIds.add(order.getId());
            }
        }
        List<User> users = userService.getUserByIds(sellerIds);
        if (users != null && !users.isEmpty()) {
            for (Order order : data) {
                for (User user : users) {
                    if (user.getId().equals(order.getSellerId())) {
                        order.setUser(user);
                    }
                }
            }
        }
        Map<String, Long> sumPrice = orderService.sumPrice(orderSearch);
        List<String> shopIds = new ArrayList<>();
        if (orderSearch.getCreateTimeFrom() > 0 && orderSearch.getCreateTimeTo() > 0) {
            List<Order> listOrder = dataPage.getData();
            for (Order order : listOrder) {
                if (!shopIds.contains(order.getSellerId())) {
                    shopIds.add(order.getSellerId());
                }
            }
        }
        List<Shop> shops = new ArrayList<>();
        if (shopIds != null && !shopIds.isEmpty()) {
            shops = shopService.getShops(shopIds);
        }
        List<Seller> sellers = new ArrayList<>();
        List<User> userIds = new ArrayList<>();
        List<String> listSeller = new ArrayList<>();
        if (shopIds != null && !shopIds.isEmpty()) {
            try {
                sellers = sellerService.getNLSCIntergrate(shopIds);
                if (sellers != null && !sellers.isEmpty()) {
                    for (Seller seller : sellers) {
                        listSeller.add(seller.getUserId());
                    }
                }
                if (listSeller != null && !listSeller.isEmpty()) {
                    userIds = userService.getAllUserByIds(listSeller);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Seller> sellersNo = new ArrayList<>();
        List<String> listSellerNo = new ArrayList<>();
        if (shopIds != null && !shopIds.isEmpty()) {
            try {
                sellersNo = sellerService.getNLSCNoIntergrate(shopIds);
                if (sellersNo != null && !sellersNo.isEmpty()) {
                    for (Seller seller : sellersNo) {
                        listSellerNo.add(seller.getUserId());
                    }
                }
                if (listSellerNo != null && !listSellerNo.isEmpty()) {
                    userIds = userService.getAllUserByIds(listSellerNo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        map.put("orderSearch", orderSearch);
        map.put("sumPrice", sumPrice);
        map.put("dataPage", dataPage);
        map.put("clientScript", "var citys = " + gson.toJson(cityService.list())
                + "; var districts = " + gson.toJson(districtService.list())
                + "; var ordershop = " + gson.toJson(shops)
                + "; var orderNLSC = " + gson.toJson(sellers)
                + "; var orderNoNLSC = " + gson.toJson(sellersNo)
                + "; var userIds = " + gson.toJson(userIds)
                + "; var orderIds = " + gson.toJson(orderIds)
                + "; order.init();");
        return "cp.order.list";
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public void excel(HttpServletResponse response,
            @RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "itemId", defaultValue = "") String itemId,
            @RequestParam(value = "sellerId", defaultValue = "") String sellerId,
            @RequestParam(value = "sellerEmail", defaultValue = "") String sellerEmail,
            @RequestParam(value = "sellerPhone", defaultValue = "") String sellerPhone,
            @RequestParam(value = "sellerCityId", defaultValue = "") String sellerCityId,
            @RequestParam(value = "sellerDistrictId", defaultValue = "") String sellerDistrictId,
            @RequestParam(value = "paymentMethod", defaultValue = "0") int paymentMethod,
            @RequestParam(value = "paymentStatusSearch", defaultValue = "0") int paymentStatusSearch,
            @RequestParam(value = "shipmentStatusSearch", defaultValue = "0") int shipmentStatusSearch,
            @RequestParam(value = "refundStatus", defaultValue = "0") int refundStatus,
            @RequestParam(value = "createTimeFrom", defaultValue = "0") long createTimeFrom,
            @RequestParam(value = "createTimeTo", defaultValue = "0") long createTimeTo,
            @RequestParam(value = "paidTimeFrom", defaultValue = "0") long paidTimeFrom,
            @RequestParam(value = "paidTimeTo", defaultValue = "0") long paidTimeTo,
            @RequestParam(value = "shipmentCreateTimeFrom", defaultValue = "0") long shipmentCreateTimeFrom,
            @RequestParam(value = "shipmentCreateTimeTo", defaultValue = "0") long shipmentCreateTimeTo,
            @RequestParam(value = "receiverEmail", defaultValue = "") String receiverEmail,
            @RequestParam(value = "receiverPhone", defaultValue = "") String receiverPhone,
            @RequestParam(value = "receiverName", defaultValue = "") String receiverName,
            @RequestParam(value = "receiverCityId", defaultValue = "") String receiverCityId,
            @RequestParam(value = "receiverDistrictId", defaultValue = "") String receiverDistrictId,
            @RequestParam(value = "buyEmail", defaultValue = "") String buyEmail,
            @RequestParam(value = "buyPhone", defaultValue = "") String buyPhone,
            @RequestParam(value = "buyName", defaultValue = "") String buyName,
            @RequestParam(value = "buyerCityId", defaultValue = "") String buyerCityId,
            @RequestParam(value = "buyerDistrictId", defaultValue = "") String buyerDistrictId
    ) throws Exception {
        OrderSearch orderSearchS = new OrderSearch();
        if (id != null && !id.equals("")) {
            orderSearchS.setId(id);
        }
        if (itemId != null && !itemId.equals("")) {
            orderSearchS.setItemId(id);
        }
        if (sellerId != null && !sellerId.equals("")) {
            orderSearchS.setSellerId(id);
        }
        if (sellerEmail != null && !sellerEmail.equals("")) {
            orderSearchS.setSellerEmail(sellerEmail);
        }
        if (sellerPhone != null && !sellerPhone.equals("")) {
            orderSearchS.setSellerPhone(sellerPhone);
        }
        if (sellerCityId != null && !sellerCityId.equals("")) {
            orderSearchS.setSellerCityId(sellerCityId);
        }
        if (sellerDistrictId != null && !sellerDistrictId.equals("")) {
            orderSearchS.setSellerDistrictId(sellerDistrictId);
        }
        if (paymentMethod > 0) {
            orderSearchS.setPaymentMethod(paymentMethod);
        }
        if (paymentStatusSearch > 0) {
            orderSearchS.setPaymentStatusSearch(paymentStatusSearch);
        }
        if (shipmentStatusSearch > 0) {
            orderSearchS.setShipmentStatusSearch(shipmentStatusSearch);
        }
        if (refundStatus > 0) {
            orderSearchS.setRefundStatus(refundStatus);
        }
        if (createTimeFrom > 0 && createTimeTo > 0) {
            orderSearchS.setCreateTimeFrom(createTimeFrom);
            orderSearchS.setCreateTimeTo(createTimeTo);
        }
        if (paidTimeFrom > 0 && paidTimeTo > 0) {
            orderSearchS.setPaidTimeFrom(paidTimeFrom);
            orderSearchS.setPaidTimeTo(paidTimeTo);
        }
        if (shipmentCreateTimeFrom > 0 && shipmentCreateTimeTo > 0) {
            orderSearchS.setShipmentCreateTimeFrom(shipmentCreateTimeFrom);
            orderSearchS.setShipmentCreateTimeTo(shipmentCreateTimeTo);
        }
        if (receiverEmail != null && !receiverEmail.equals("")) {
            orderSearchS.setReceiverEmail(receiverEmail);
        }
        if (receiverPhone != null && !receiverPhone.equals("")) {
            orderSearchS.setReceiverPhone(receiverPhone);
        }
        if (receiverName != null && !receiverName.equals("")) {
            orderSearchS.setReceiverName(receiverName);
        }
        if (receiverCityId != null && !receiverCityId.equals("")) {
            orderSearchS.setReceiverCityId(receiverCityId);
        }
        if (receiverCityId != null && !receiverCityId.equals("")) {
            orderSearchS.setReceiverCityId(receiverCityId);
        }
        if (receiverDistrictId != null && !receiverDistrictId.equals("")) {
            orderSearchS.setReceiverDistrictId(receiverDistrictId);
        }
        if (buyEmail != null && !buyEmail.equals("")) {
            orderSearchS.setBuyEmail(buyEmail);
        }
        if (buyPhone != null && !buyPhone.equals("")) {
            orderSearchS.setBuyPhone(buyPhone);
        }
        if (buyName != null && !buyName.equals("")) {
            orderSearchS.setBuyName(buyName);
        }
        if (buyerCityId != null && !buyerCityId.equals("")) {
            orderSearchS.setBuyerCityId(buyerCityId);
        }
        if (buyerDistrictId != null && !buyerDistrictId.equals("")) {
            orderSearchS.setBuyerDistrictId(buyerDistrictId);
        }
        Workbook wb = new HSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        org.apache.poi.ss.usermodel.Row row = sheet.createRow((short) 0);
        row = sheet.createRow((short) 1);
        row.createCell(0).setCellValue("Mã hóa đơn");
        row.createCell(1).setCellValue("Mã vận đơn");
        row.createCell(2).setCellValue("Mã thanh toán");
        row.createCell(3).setCellValue("Người bán");
        row.createCell(4).setCellValue("Giá trị hàng hóa");
        row.createCell(5).setCellValue("Phí vận chuyển");
        row.createCell(6).setCellValue("Tổng thanh toán");

        int i = 1;
        orderSearchS.setPageIndex(0);
        orderSearchS.setPageSize(60000);
        DataPage<Order> dataPage = orderService.search(orderSearchS);
        long priceT = 0;
        for (Order order : dataPage.getData()) {
            Response user = userService.getById(order.getSellerId());
            String username = null;
            if (user.isSuccess()) {
                User data = (User) user.getData();
                username = (data.getUsername() == null || data.getUsername().equals("")) ? data.getEmail() : data.getUsername();
            }
            Long create = order.getCreateTime();
            Date cre = new Date(create);
            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            String Jointime = dt.format(cre);
            i++;
            row = sheet.createRow((short) i);
            row.createCell(0).setCellValue(String.valueOf(order.getId()));
            row.createCell(1).setCellValue(String.valueOf((order.getScId() == null) ? "" : order.getScId()));
            row.createCell(2).setCellValue(String.valueOf((order.getNlId() == null) ? "" : order.getNlId()));
            row.createCell(3).setCellValue(String.valueOf(username));
            row.createCell(4).setCellValue(String.valueOf(order.getTotalPrice()));
            row.createCell(5).setCellValue(String.valueOf(order.getShipmentPrice()));
            row.createCell(6).setCellValue(String.valueOf(order.getFinalPrice()));
            priceT += order.getFinalPrice();
        }
        String s = Long.toString(priceT);
        row = sheet.createRow((short) i + 1);
        row.createCell(5).setCellValue("Tổng tiền: ");
        row.createCell(6).setCellValue(TextUtils.numberFormat(Double.parseDouble(s)) + "VNĐ");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=xuat-excel-hoa-don-ban-hang.xls");
        wb.write(response.getOutputStream());
    }

}
