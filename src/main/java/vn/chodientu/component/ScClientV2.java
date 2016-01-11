package vn.chodientu.component;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.chodientu.entity.data.ScDistrict;
import vn.chodientu.entity.data.ScWard;
import vn.chodientu.entity.db.City;
import vn.chodientu.entity.db.District;
import vn.chodientu.entity.db.OrderItem;
import vn.chodientu.entity.db.ScHistory;
import vn.chodientu.entity.db.Seller;
import vn.chodientu.entity.enu.ShipmentService;
import vn.chodientu.entity.enu.ShipmentStatus;
import vn.chodientu.entity.output.Response;
import vn.chodientu.entity.web.Viewer;
import vn.chodientu.repository.CityRepository;
import vn.chodientu.repository.DistrictRepository;
import vn.chodientu.repository.ScHistoryRepository;

/**
 *
 * @author Phu
 */
@Component
public class ScClientV2 {

    @Autowired
    private Gson gson;
    @Autowired
    private ScHistoryRepository historyRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private Viewer viewer;

    private String call(String url, Map<String, Object> params, String token, String scHistoryId) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (token != null) {
            post.setHeader("Authorization", "Bearer " + token);
        }
        List<NameValuePair> urlParameters = new ArrayList<>();
        if (params != null) {
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                    urlParameters.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
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

        // log sc
        ScHistory scHistory = new ScHistory();
        scHistory.setId(scHistoryId);
        scHistory.setParams(params);
        scHistory.setResp(text.toString());
        scHistory.setUrl(url);
        scHistory.setTime(System.currentTimeMillis());
        historyRepository.save(scHistory);

        return text.toString();
    }

    private String call(String url, JSONObject params, String token) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            post.setHeader("Authorization", "Bearer " + token);
        }
        StringEntity entity = new StringEntity(params.toString(), HTTP.UTF_8);
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }

        // log sc
        ScHistory scHistory = new ScHistory();

        Map<String, Object> map = new HashMap<>();
        map.put("params", params.toString());
        scHistory.setParams(map);
        scHistory.setResp(text.toString());
        scHistory.setUrl(url);
        scHistory.setTime(System.currentTimeMillis());
        historyRepository.save(scHistory);

        return text.toString();
    }

    private String callGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder text = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            text.append(line);
        }
        return text.toString();
    }

    public String getNlId(String email) throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("function", "GetNganLuongId");
            params.put("params[email]", email);
            String call = call("http://api.shipchung.vn/ShipChungUser/", params, null, historyRepository.genId());
            return gson.fromJson(call, params.getClass()).get("user_nl_id").toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Tài khoản " + email + " chưa được liên kết (tạo tài khoản) trên shipchung.vn");
        }
    }

    public String checkMerchantKey(String merchantKey) throws Exception {
        try {
            String token = getToken();
            Map<String, Object> params = new HashMap<>();
            params.put("MerchantKey", merchantKey);

            JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/checkmerchantkey", params, token, historyRepository.genId()));
            String resul = res.getString("error");
            if (resul.equals("success")) {
                return res.getJSONObject("data").getString("email");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("MerchantKey không chính xác");
        }
        return "";
    }

    public String getToken() throws Exception {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("client_id", "itest");
            params.put("client_secret", "itest");
            params.put("grant_type", "patner_credentials");
            params.put("redirect_uri", "");
            String call = call("http://services.shipchung.vn/oauth/token/access", params, null, historyRepository.genId());
            return gson.fromJson(call, params.getClass()).get("access_token").toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Create token accept Courier's Api error");
        }
    }

    public List<ScDistrict> getDistrict(String cityID) throws Exception {
        List<ScDistrict> districts = new ArrayList<>();
        try {
            JSONObject res = new JSONObject(callGet("http://services.shipchung.vn/api/rest/lading/province/" + cityID));
            if (res.getString("error_message").equals("success")) {
                JSONArray jArray = res.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
                    int id = jArray.getJSONObject(i).getInt("ProvinceId");
                    String name = jArray.getJSONObject(i).getString("ProvinceName");
                    ScDistrict district = new ScDistrict(id, name);
                    districts.add(district);
                }
                return districts;
            }
        } catch (Exception ex) {
            throw new Exception("Không lấy được danh sách Quận huyện");
        }
        return districts;
    }

    public List<ScWard> getWard(String districID) throws Exception {
        List<ScWard> scWards = new ArrayList<>();
        try {
            JSONObject res = new JSONObject(callGet("http://services.shipchung.vn/api/rest/lading/ward/" + districID));
            if (res.getString("error_message").equals("success")) {
                JSONArray jArray = res.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
                    int id = jArray.getJSONObject(i).getInt("WardId");
                    String name = jArray.getJSONObject(i).getString("WardName");
                    ScWard ward = new ScWard(id, name);
                    scWards.add(ward);
                }
                return scWards;
            }
        } catch (Exception ex) {
            throw new Exception("Không lấy được danh sách Phường xã");
        }
        return scWards;
    }

    public FeeShip caculateFee(String email, ShipmentService service, String fromCity, String fromDistrict, String toCity, String toDistrict, String toWard, long price, int weight, boolean isCod, boolean isProtected, boolean isPayment) throws Exception {
//        Map<String, Object> params = new HashMap<>();

        //From
//        Map<String, Object> jsonFrom = new HashMap<>();
        JSONObject sexJSON = new JSONObject();
        JSONObject toJSON = new JSONObject();
        JSONObject orderJSON = new JSONObject();
        JSONObject configJSON = new JSONObject();
        JSONObject paramJSON = new JSONObject();
//        jsonFrom.put("City", fromCity);
//        jsonFrom.put("Province", fromDistrict);
        sexJSON.put("City", fromCity);
        sexJSON.put("Province", fromDistrict);
        paramJSON.put("From", sexJSON);
//        params.put("From", gson.toJson(jsonFrom));
        //To
//        Map<String, Object> jsonTo = new HashMap<>();
//        jsonTo.put("City", toCity);
//        jsonTo.put("Province", toDistrict);
//        jsonTo.put("Ward", "");
        toJSON.put("City", toCity);
        toJSON.put("Province", toDistrict);
        toJSON.put("Ward", toWard);
        paramJSON.put("To", toJSON);
//        params.put("To", gson.toJson(jsonTo));
        //Order
//        Map<String, Object> jsonOrder = new HashMap<>();
//        jsonOrder.put("Amount", price);
//        jsonOrder.put("Weight", weight);
//        jsonOrder.put("BoxSize", "");
        orderJSON.put("Amount", price);
        orderJSON.put("Weight", weight);
        orderJSON.put("BoxSize", "");
//        params.put("Order", gson.toJson(jsonOrder));
        paramJSON.put("Order", orderJSON);
        //Config
//        Map<String, Object> jsonConfig = new HashMap<>();
        if (service == ShipmentService.SLOW) {
//            jsonConfig.put("Service", 2);
            configJSON.put("Service", 1);
        }
        if (service == ShipmentService.FAST) {
//            jsonConfig.put("Service", 1);
            configJSON.put("Service", 2);
        }
        if (isCod) {
//            jsonConfig.put("CoD", 1);
            configJSON.put("CoD", 1);
        } else {
//            jsonConfig.put("CoD", 2);
            configJSON.put("CoD", 2);
        }
        if (isProtected) {
//            jsonConfig.put("Protect", 1);
            configJSON.put("Protected", 1);
        } else {
//            jsonConfig.put("Protect", 2);
            configJSON.put("Protected", 2);
        }
        if (isPayment) {
//            jsonConfig.put("Payment", 1);
            configJSON.put("Payment", 1);
        } else {
//            jsonConfig.put("Payment", 2);
            configJSON.put("Payment", 2);
        }
        configJSON.put("Checking", 2);
        configJSON.put("Fragile", 2);
//        params.put("Config", gson.toJson(jsonConfig));
        paramJSON.put("Config", configJSON);
        paramJSON.put("Domain", "chodientu.vn");
        try {
            JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/calculate", paramJSON, null));
            FeeShip feeShip = new FeeShip();
            List<Courier> couriers = new ArrayList<>();
            try {
                JSONObject jSONObject = res.getJSONObject("data").getJSONObject("courier");
                JSONArray jArray = jSONObject.getJSONArray("system");
                for (int i = 0; i < jArray.length(); i++) {
                    int id = jArray.getJSONObject(i).getInt("courier_id");
                    String name = jArray.getJSONObject(i).getString("courier_name");
                    int moneyPickup = jArray.getJSONObject(i).getInt("money_pickup");
                    int moneyDelivery = jArray.getJSONObject(i).getInt("money_delivery");
                    couriers.add(new Courier(id, name,moneyPickup,moneyDelivery));
                }
                feeShip.setCourier(couriers);
            } catch (Exception e) {
            }
            try {
                feeShip.setPcod(res.getJSONObject("data").getJSONObject("fee").getInt("total_vas"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                feeShip.setPvcDiscount(res.getJSONObject("data").getJSONObject("fee").getJSONObject("discount").getInt("pvc"));
            } catch (Exception e) {
                feeShip.setPvcDiscount(0);
                System.out.println(e.getMessage());
            }
            try {
                feeShip.setShip(res.getJSONObject("data").getJSONObject("fee").getInt("total_fee") - feeShip.getPvcDiscount());
            } catch (Exception e) {
                feeShip.setShip(-1);
                System.out.println(e.getMessage());
            }
            return feeShip;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return new FeeShip(-1, 0, 0, null);
        }
    }

    /**
     *
     * @param email
     * @param scId
     * @param service
     * @param fromName
     * @param fromEmail
     * @param fromPhone
     * @param fromAddress
     * @param fromCity
     * @param fromDistrict
     * @param toName
     * @param toEmail
     * @param toPhone
     * @param toAddress
     * @param toCity
     * @param toDistrict
     * @param itemName
     * @param itemDescription
     * @param price Tổng giá sp
     * @param chargePrice Phí cần thu hộ
     * @param feeShipping
     * @param weight
     * @param isCod
     * @param isProtected
     * @return Nếu success = true, mã vận đơn mà message, phí vc (thu của người
     * bán) là data
     * @throws java.lang.Exception
     */
    /**
     *
     * @param orderItems
     * @param payment
     * @param email
     * @param scId
     * @param service
     * @param fromName
     * @param fromEmail
     * @param fromPhone
     * @param fromAddress
     * @param fromCity
     * @param fromDistrict
     * @param toName
     * @param toEmail
     * @param toPhone
     * @param toAddress
     * @param toCity
     * @param toDistrict
     * @param itemName
     * @param itemDescription
     * @param price Tổng giá sp
     * @param chargePrice Phí cần thu hộ
     * @param feeShipping
     * @param weight
     * @param isCod
     * @param isProtected
     * @param merchantKey
     * @param courier
     * @return Nếu success = true, mã vận đơn mà message, phí vc (thu của người
     * bán) là data
     * @throws java.lang.Exception
     */
    public Response createShipment(
            List<OrderItem> orderItems,
            boolean payment,
            String email,
            String scId,
            ShipmentService service,
            String fromName,
            String fromEmail,
            String fromPhone,
            String fromAddress,
            String fromCity,
            String fromDistrict,
            String toName,
            String toEmail,
            String toPhone,
            String toAddress,
            String toCity,
            String toDistrict,
            String toWard,
            String itemName,
            String itemDescription,
            long price,
            long chargePrice,
            long feeShipping,
            int weight,
            boolean isCod,
            boolean isProtected,
            int courier, String merchantKey, String orderId) throws Exception {
        String token = getToken();
        Map<String, Object> params = new HashMap<>();
//        JSONObject params = new JSONObject();
        //From
        //Map<String, Object> jsonFrom = new HashMap<>();
//        JSONObject jsonFrom = new JSONObject();
        params.put("From[Name]", fromName);
        params.put("From[Phone]", fromPhone);
        params.put("From[Address]", fromAddress);
        params.put("From[City]", fromCity);
        params.put("From[Province]", fromDistrict.trim());
        //params.put("From", gson.toJson(jsonFrom));
        //To
        //Map<String, Object> jsonTo = new HashMap<>();
//        JSONObject jsonTo = new JSONObject();
        params.put("To[Name]", toName);
        params.put("To[Phone]", toPhone);
        params.put("To[Address]", toAddress);
        params.put("To[City]", toCity);
        params.put("To[Province]", toDistrict.trim());
        params.put("To[Ward]", toWard);
        //params.put("To", gson.toJson(jsonTo)); 

        //Items
        //Map<String, Object> jsonItem = new HashMap<>();
        //List<Map<String, Object>> list = new ArrayList<>();
        int quantity = 0;
        int i = 0;
        for (OrderItem orderItem : orderItems) {
            params.put("Item[" + i + "][Name]", orderItem.getItemName());
            params.put("Item[" + i + "][Price]", orderItem.getItemPrice());
            params.put("Item[" + i + "][Quantity]", orderItem.getQuantity());
            params.put("Item[" + i + "][Weight]", orderItem.getWeight() > 0 ? orderItem.getWeight() * orderItem.getQuantity() : orderItem.getWeight());
            //list.add(jsonItem);
            quantity += orderItem.getQuantity();
            i++;
        }
        //JSONArray jsonItems = new JSONArray(list);
        //params.put("Items", jsonItems);
        //Order
        Map<String, Object> jsonOrder = new HashMap<>();
//        JSONObject jsonOrder = new JSONObject();
        if(itemDescription !=null){
            if(itemDescription.length() > 250){
                itemDescription=itemDescription.substring(0, 249);
            }
        }
        params.put("Order[ProductName]", itemDescription);
        params.put("Order[Quantity]", quantity);
        //jsonOrder.put("Code", orderId);
        params.put("Order[Amount]", price);
        params.put("Order[Weight]", weight);
        params.put("Order[Collect]", chargePrice);
        //params.put("Order", gson.toJson(jsonOrder));
        //Config
        //Map<String, Object> jsonConfig = new HashMap<>();
        //JSONObject jsonConfig = new JSONObject();
        if (service == ShipmentService.SLOW) {
            params.put("Config[Service]", 1);
        }
        if (service == ShipmentService.FAST) {
            params.put("Config[Service]", 2);
        }
        if (isCod) {
            params.put("Config[CoD]", 1);
        } else {
            params.put("Config[CoD]", 2);
        }
        if (isProtected) {
            params.put("Config[Protected]", 1);
        } else {
            params.put("Config[Protected]", 2);
        }
        params.put("Config[Checking]", 2);
        params.put("Config[Fragile]", 2);
        if (payment) {
            //jsonConfig.put("Payment", 1);
        } else {
            //jsonConfig.put("Payment", 2);
        }
        //params.put("Config", jsonConfig);
        params.put("Courier", courier);
        params.put("MerchantKey", merchantKey);
        params.put("Domain", "chodientu.vn");
//        if (scId != null && !scId.equals("")) {
//            params.put("UserId", scId);
//        } else {
//            String nlId = getNlId(email);
//            if (nlId == null) {
//                return new Response(false, "Tài khoản người bán chưa tích hợp shipchung!");
//            }
//            params.put("UserId", nlId);
//        }
        try {
            FeeShip feeShip = new FeeShip();
            String scHistoryId = historyRepository.genId();
            JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/create", params, token, scHistoryId));
            if (res.getString("error").equals("success")) {
                feeShip = new FeeShip(res.getJSONObject("data").getJSONObject("ShowFee").getInt("pvc"), res.getJSONObject("data").getJSONObject("ShowFee").getInt("cod"), 0, res.getJSONObject("data").getInt("CourierId"));
                ScHistory scHistory = historyRepository.find(scHistoryId);
                if (scHistory != null) {
                    String string = res.getJSONObject("data").getString("TrackingCode");
                    scHistory.setScId(string);
                    historyRepository.save(scHistory);
                }
                return new Response(true, res.getJSONObject("data").getString("TrackingCode"), feeShip);
            } else {
                String err = res.getString("error");
                if (err.equals("NOT_SUPPORT_SHIPPING")) {
                    District districtFrom = districtRepository.getBySc(fromDistrict);
                    District districtTo = districtRepository.getBySc(toDistrict);
                    err = "Hệ thống không hỗ trợ vận chuyển từ <strong>" + districtFrom.getName() + "</strong> đến <strong>" + districtTo.getName() + "</strong>";
                }
                return new Response(false, err);
            }

        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

    public Response status(String scId) {
        Map<String, Object> params = new HashMap<>();
        params.put("TrackingCode", scId);
        try {
            JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/status", params, null, historyRepository.genId()));
            int statusCode = res.getJSONObject("data").getJSONObject(scId).getInt("StatusCode");

            ShipmentStatus status = null;
            switch (statusCode) {
                case 1:
                    status = ShipmentStatus.NEW;
                    break;
                case 2:
                    status = ShipmentStatus.STOCKING;
                    break;
                case 3:
                    status = ShipmentStatus.STOCKING;
                    break;
                case 4:
                    status = ShipmentStatus.STOCKING;
                    break;
                case 5:
                    status = ShipmentStatus.DELIVERING;
                    break;
                case 6:
                    status = ShipmentStatus.DELIVERING;
                    break;
                case 7:
                    status = ShipmentStatus.DELIVERING;
                    break;
                case 8:
                    status = ShipmentStatus.DELIVERED;
                    break;
                case 9:
                    status = ShipmentStatus.RETURN;
                    break;
                case 10:
                    status = ShipmentStatus.RETURN;
                    break;
                case 11:
                    status = ShipmentStatus.DENIED;
                    break;

            }

            return new Response(true, res.getJSONObject("lading").getString("status"), status);
        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

    public Response accept(String scId, String merchantKey) throws Exception {
        String token = getToken();
        Map<String, Object> params = new HashMap<>();
        params.put("TrackingCode", scId);
        params.put("MerchantKey", merchantKey);
        try {
            JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/accept", params, token, historyRepository.genId()));
            if (res.getString("message").equals("NOT_ENOUGH_MONEY")) {
                return new Response(true, "NOT_ENOUGH_MONEY", null);
            } else {
                return new Response(true, "Duyệt vận đơn thành công", null);
            }

        } catch (Exception ex) {
            return new Response(false, ex.getMessage());
        }
    }

    public long balance(String merchantKey, int ladingCoD) throws Exception {
        //String token = getToken();
        Map<String, Object> params = new HashMap<>();

        params.put("MerchantKey", merchantKey);
        params.put("LadingCoD", ladingCoD);
        params.put("Domain", "chodientu.vn");
        // try {
        JSONObject res = new JSONObject(call("http://services.shipchung.vn/api/rest/lading/balance", params, null, historyRepository.genId()));
        if (res.getString("error").equals("success")) {
            long moneyTotal = res.getJSONObject("data").getLong("money_total");
            return moneyTotal;
        } else {
            return 0;
        }

        //} catch (Exception ex) {
        //    return 0;
        //}
    }

    public boolean checkDistrict(String districtId) throws Exception {
        District district = districtRepository.find(districtId);
        String scDistrictId = district.getScId();
        String cityId = district.getCityId();
        City city = cityRepository.find(cityId);
        try {
            JSONObject res = new JSONObject(callGet("http://services.shipchung.vn/api/rest/lading/province/" + city.getScId()));
            if (res.getString("error_message").equals("success")) {
                JSONArray jArray = res.getJSONArray("data");
                for (int i = 0; i < jArray.length(); i++) {
                    int id = jArray.getJSONObject(i).getInt("ProvinceId");
                    if ((id + "").equals(scDistrictId)) {
                        int remote = jArray.getJSONObject(i).getInt("Remote");
                        if (remote == 2) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    public void signin(String code, Seller seller) {
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("grant_type", "authorization_code");
        jsonParams.put("client_id", "cdt_access");
        jsonParams.put("client_secret", "adaodajouq933jdajkdiw");
        jsonParams.put("code", code.trim());

        try {
            JSONObject res = new JSONObject(call("http://api.shipchung.vn/oauth_token.php?grant_type=authorization_code", jsonParams, null, historyRepository.genId()));
            String token = res.getString("access_token");
            res = new JSONObject(call("http://api.shipchung.vn/ShipChungUser/GetUserOpenID?access_token=" + token, jsonParams, null, historyRepository.genId()));
            seller.setScEmail(res.getJSONObject("DATA").getString("email"));
            seller.setScId(res.getJSONObject("DATA").getString("id"));
            seller.setScIntegrated(true);
        } catch (Exception ex) {
        }
    }

    public class FeeShip {

        private int ship;
        private int pcod;
        private int pvcDiscount;
        private int courierId;
        private List<Courier> courier;

        public FeeShip(int ship, int pcod, int pvcDiscount, List<Courier> courier) {
            this.ship = ship;
            this.pcod = pcod;
            this.pvcDiscount = pvcDiscount;
            this.courier = courier;
        }

        public FeeShip(int ship, int pcod, int pvcDiscount, int courierId) {
            this.ship = ship;
            this.pcod = pcod;
            this.pvcDiscount = pvcDiscount;
            this.courierId = courierId;
        }

        private FeeShip() {

        }

        public List<Courier> getCourier() {
            return courier;
        }

        public void setCourier(List<Courier> courier) {
            this.courier = courier;
        }

        public int getPvcDiscount() {
            return pvcDiscount;
        }

        public void setPvcDiscount(int pvcDiscount) {
            this.pvcDiscount = pvcDiscount;
        }

        public int getShip() {
            return ship;
        }

        public void setShip(int ship) {
            this.ship = ship;
        }

        public int getPcod() {
            return pcod;
        }

        public void setPcod(int pcod) {
            this.pcod = pcod;
        }

        public int getCourierId() {
            return courierId;
        }

        public void setCourierId(int courierId) {
            this.courierId = courierId;
        }

    }

    public class Courier implements Serializable{

        private int courierId;
        private String courierName;
private int moneyPickup;
        private int moneyDelivery;
        public Courier(int courierId, String courierName, int moneyPickup, int moneyDelivery) {
            this.courierId = courierId;
            this.courierName = courierName;
            this.moneyPickup = moneyPickup;
            this.moneyDelivery = moneyDelivery;
        }
        public Courier(int courierId, String courierName) {
            this.courierId = courierId;
            this.courierName = courierName;
        }

        private Courier() {

        }

        public int getCourierId() {
            return courierId;
        }

        public void setCourierId(int courierId) {
            this.courierId = courierId;
        }

        public String getCourierName() {
            return courierName;
        }

        public void setCourierName(String courierName) {
            this.courierName = courierName;
        }
        public int getMoneyPickup() {
            return moneyPickup;
        }

        public void setMoneyPickup(int moneyPickup) {
            this.moneyPickup = moneyPickup;
        }

        public int getMoneyDelivery() {
            return moneyDelivery;
        }

        public void setMoneyDelivery(int moneyDelivery) {
            this.moneyDelivery = moneyDelivery;
        }

    }
}
