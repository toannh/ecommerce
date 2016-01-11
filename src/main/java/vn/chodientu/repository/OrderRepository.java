package vn.chodientu.repository;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import static java.lang.Long.parseLong;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.Order;
import vn.chodientu.entity.db.User;
import vn.chodientu.entity.enu.PaymentStatus;
import vn.chodientu.entity.enu.ShipmentStatus;
import vn.chodientu.util.TextUtils;

/**
 *
 * @author Phu
 */
@Repository
public class OrderRepository extends BaseRepository<Order> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public OrderRepository() {
        super(Order.class);
    }

    /**
     * Đếm số lượng hóa đơn đơn hàng theo trạng thái thanh toán
     *
     * @param paymentStatus
     * @param seller
     * @param userId
     * @return
     */
    public long countByPaymentStatus(PaymentStatus paymentStatus, boolean seller, String userId) {
        Criteria criteria = new Criteria();
        if (seller == true) {
            if (paymentStatus == PaymentStatus.PAID) {
                criteria.and("sellerId").is(userId);
                criteria.and("markSellerPayment").is(paymentStatus);

            } else {
                criteria.and("sellerId").is(userId);
                criteria.and("markSellerPayment").ne(PaymentStatus.PAID);
            }
            criteria.and("remove").is(false);

        } else {
            if (paymentStatus == PaymentStatus.PAID) {
                criteria.and("buyerId").is(userId);
                criteria.and("markBuyerPayment").is(paymentStatus);

            } else {
                criteria.and("buyerId").is(userId);
                criteria.and("markBuyerPayment").ne(PaymentStatus.PAID);
            }
            criteria.and("removeBuyer").is(false);

        }

        return count(new Query(criteria));
    }

    /**
     * *
     * Đếm số lượng hóa đơn đơn hàng theo trạng thái vận chuyển
     *
     * @param shipmentStatus
     * @param seller
     * @param userId
     * @return
     */
    public long countByShipmentStatus(ShipmentStatus shipmentStatus, boolean seller, String userId) {
        Criteria criteria = new Criteria();
        if (seller == true) {
            criteria.and("sellerId").is(userId);
            if (shipmentStatus == ShipmentStatus.DELIVERED) {
                criteria.and("markSellerShipment").is(shipmentStatus);
            } else {
                criteria.and("markSellerShipment").ne(ShipmentStatus.DELIVERED);
            }
            criteria.and("remove").is(false);
        } else {
            criteria.and("buyerId").is(userId);
            if (shipmentStatus == ShipmentStatus.DELIVERED) {
                criteria.and("markBuyerShipment").is(shipmentStatus);
            } else {
                criteria.and("markBuyerShipment").ne(ShipmentStatus.DELIVERED);
            }
            criteria.and("removeBuyer").is(false);
        }

        return count(new Query(criteria));
    }

    /**
     * *
     * Đếm số lượng hóa đơn đơn hàng ở thùng rác
     *
     * @param remove
     * @param seller
     * @param userId
     * @return
     */
    public long countByRemove(boolean remove, boolean seller, String userId) {
        Criteria criteria = new Criteria();

        if (seller == true) {
            criteria.and("remove").is(remove);
            criteria.and("sellerId").is(userId);
        } else {
            criteria.and("removeBuyer").is(remove);
            criteria.and("buyerId").is(userId);
        }
        return count(new Query(criteria));
    }

    /**
     * *
     * Đếm số lượng hóa đơn đơn hàng
     *
     * @param remove
     * @param seller
     * @param userId
     * @return
     */
    public long countByOrderAll(boolean seller, String userId) {
        Criteria criteria = new Criteria();
        if (seller == true) {
            criteria.and("sellerId").is(userId);
            criteria.and("remove").is(false);
        } else {
            criteria.and("buyerId").is(userId);
            criteria.and("removeBuyer").is(false);
        }
        return count(new Query(criteria));
    }

    /**
     * @shipmentCreateTime Lấy những đơn hàng có mã đơn hàng dưới 1 tháng
     * @shipmentUpdateTime 2 tiếng cập nhật 1 lần
     * @shipmentStatus đang được vận hành
     * @scId có mã vận đơn
     * @return
     */
    public Order getThread() {
        Date date = new Date();
        date.setMonth(date.getMonth() + 1);
        Criteria cri = new Criteria();
        cri.and("shipmentCreateTime").lte(date.getTime());
        cri.and("shipmentUpdateTime").
                lt(System.currentTimeMillis() - 2 * 60 * 60 * 1000);
        cri.and("scId").ne(null);
        List<ShipmentStatus> shipment = new ArrayList<>();
        shipment.add(ShipmentStatus.RETURN);
        shipment.add(ShipmentStatus.DELIVERED);
        shipment.add(ShipmentStatus.DENIED);
        cri.and("shipmentStatus").nin(shipment);
        return getMongo().findAndModify(new Query(cri).with(new Sort(Sort.Direction.ASC, "shipmentUpdateTime")).limit(1),
                new Update().update("shipmentUpdateTime", System.currentTimeMillis()), getEntityClass());
    }

    /**
     * Khách hàng chưa được add
     *
     * @return
     */
    public Order getCustomer() {
        Criteria cri = new Criteria();
        cri.and("customer").is(false);
        return getMongo().findAndModify(new Query(cri), new Update().update("customer", true), getEntityClass());
    }

    /**
     * lấy thông tin theo thread
     *
     * @param buyerId
     * @return
     */
    public Order getCustomerOrder() {
        Criteria cri = new Criteria();
        cri.and("ignoreCustomer").is(false);
        cri.and("buyerId").ne(null);
        return getMongo().findAndModify(new Query(cri), new Update().update("ignoreCustomer", true), getEntityClass());
    }

    /**
     * Tính tổng tiền theo điều kiện search
     *
     * @param cri
     * @return
     */
    public Map<String, Long> sumPrice(Criteria cri) {
        Aggregation aggregations = Aggregation.newAggregation(
                match(cri),
                group("null").sum("finalPrice").as("finalPrice")
                .sum("totalPrice").as("totalPrice"),
                project("finalPrice", "totalPrice")
        );
        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregations, "order", DBObject.class);
        List<DBObject> fieldList = results.getMappedResults();
        Map<String, Long> map = new HashMap<>();
        if (fieldList != null && !fieldList.isEmpty()) {
            for (DBObject db : fieldList) {
                map.put("finalPrice", parseLong(db.get("finalPrice").toString()));
                map.put("totalPrice", parseLong(db.get("totalPrice").toString()));
            }
        }
        return map;
    }

    public Order addContact() {
        Criteria cri = new Criteria();
        cri.and("pushC").ne(true);
        return getMongo().findAndModify(new Query(cri),
                new Update().set("pushC", true), new FindAndModifyOptions().returnNew(true), getEntityClass());
    }

    public Order pushPayment() {
        Criteria cri = new Criteria();
        cri.and("pushPayment").ne(true);
        return getMongo().findAndModify(new Query(cri),
                new Update().set("pushPayment", true), new FindAndModifyOptions().returnNew(true), getEntityClass());
    }

    public long countBuyerUnique(boolean all) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("createTime", new BasicDBObject("$gte", getTime(System.currentTimeMillis(), false)).append("$lt", getTime(System.currentTimeMillis(), true)));
        }
        List listBuyerUnique = myColl.distinct("buyerPhone", gtQuery);
//        List listBuyerUnique = getMongo().getCollection("order").distinct("buyerEmail");
        return listBuyerUnique.size();
    }

    public List<String> listBuyerIdUnique(boolean all) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("createTime", new BasicDBObject("$gte", getTime(System.currentTimeMillis(), false)).append("$lt", getTime(System.currentTimeMillis(), true)));
        }
        List<String> list = myColl.distinct("buyerId", gtQuery);
        return list;
    }

//    public Map<String,Long> calculateShop(Criteria cri, long fromAmount, long toAmount){
//        Map<String, Long> result = new HashMap<String, Long>();
//        Aggregation aggregation = Aggregation.newAggregation(
//                match(cri),
//                group("sellerId").sum("totalPrice").as("totalAmount")
//        );
//        AggregationResults<DBObject> resultAggr = mongoTemplate.aggregate(aggregation,"order", DBObject.class);
//        List<DBObject> fieldList = resultAggr.getMappedResults();
////        Map<String, Long> map = new HashMap<>();
//        if (fieldList != null && !fieldList.isEmpty()) {
//            for (DBObject db : fieldList) {
//                Long totalAmount  = parseLong(db.get("balance").toString());
//                String sellerId = db.get("sellerId").toString();
//                result.put(sellerId, toAmount);
//            }
//        }
//        return result;
//    }
    public List<String> listBuyerIdUnique(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("createTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        List<String> list = myColl.distinct("buyerId", gtQuery);
        return list;
    }

    public long countBuyerReturn(boolean all) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("shipmentUpdateTime", new BasicDBObject("$gte", getTime(System.currentTimeMillis(), false)).append("$lt", getTime(System.currentTimeMillis(), true)));
        }
        gtQuery.append("shipmentStatus", "RETURN");
        List<String> list = myColl.distinct("buyerPhone", gtQuery);
        return list.size();
    }

    public long countBuyerOnce(boolean all) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("createTime", new BasicDBObject("$gte", getTime(System.currentTimeMillis(), false)).append("$lt", getTime(System.currentTimeMillis(), true)));
        }
        List<String> list = myColl.distinct("buyerPhone", gtQuery);
        List<String> result = new ArrayList();
        if (!all) {
            for (String buyerPhone : list) {
                Criteria cri = new Criteria("buyerPhone").is(buyerPhone);
                cri.and("createTime").lt(getTime(System.currentTimeMillis(), false));
                long count = count(new Query(cri));
                if (count == 0) {
                    result.add(buyerPhone);
                }
            }
        } else {
            result = list;
        }
        return result.size();
    }

    private long getTime(long time, boolean endday) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            cal.setTime(new Date(time));
            return sdfTime.parse(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + (endday ? " 23:59" : " 00:00")).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    public long countSuccessSeller(boolean all) {
        //"paymentStatus": "PAID",
        //"shipmentStatus": "DELIVERED",
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
//        gtQuery.put("$or", new BasicDBObject(null, all));
        BasicDBObject clause1 = new BasicDBObject();
        if (!all) {
            clause1.put("shipmentUpdateTime", new BasicDBObject("$gte", TextUtils.getTime(System.currentTimeMillis(), false)).append("$lt", TextUtils.getTime(System.currentTimeMillis(), true)));
        }
        clause1.append("shipmentStatus", "DELIVERED");
        BasicDBObject clause2 = new BasicDBObject();
        if (!all) {
            clause2.put("paidTime", new BasicDBObject("$gte", TextUtils.getTime(System.currentTimeMillis(), false)).append("$lt", TextUtils.getTime(System.currentTimeMillis(), true)));
        }
        clause2.append("paymentStatus", "PAID");
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        gtQuery.put("$or", or);
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public long countSuccessSeller(boolean all, long time) {
        //"paymentStatus": "PAID",
        //"shipmentStatus": "DELIVERED",
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
//        gtQuery.put("$or", new BasicDBObject(null, all));
        BasicDBObject clause1 = new BasicDBObject();
        if (!all) {
            clause1.put("shipmentUpdateTime", new BasicDBObject("$gte", TextUtils.getTime(time, false)).append("$lt", TextUtils.getTime(time, true)));
        }
        clause1.append("shipmentStatus", "DELIVERED");
        BasicDBObject clause2 = new BasicDBObject();
        if (!all) {
            clause2.put("paidTime", new BasicDBObject("$gte", TextUtils.getTime(time, false)).append("$lt", TextUtils.getTime(time, true)));
        }
        clause2.append("paymentStatus", "PAID");
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        gtQuery.put("$or", or);
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public long countFirstSuccessSeller(boolean all) {
        //"paymentStatus": "PAID",
        //"shipmentStatus": "DELIVERED",
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        BasicDBObject clause1 = new BasicDBObject();
        if (!all) {
            clause1.put("shipmentUpdateTime", new BasicDBObject("$gte", TextUtils.getTime(System.currentTimeMillis(), false)).append("$lt", TextUtils.getTime(System.currentTimeMillis(), true)));
        }
        clause1.append("shipmentStatus", "DELIVERED");
        BasicDBObject clause2 = new BasicDBObject();
        if (!all) {
            clause2.put("paidTime", new BasicDBObject("$gte", TextUtils.getTime(System.currentTimeMillis(), false)).append("$lt", TextUtils.getTime(System.currentTimeMillis(), true)));
        }
        clause2.append("paymentStatus", "PAID");
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        gtQuery.put("$or", or);
        List<String> list = myColl.distinct("sellerId", gtQuery);
        List<String> result = new ArrayList<String>();
        if (!all) {
            for (String sellerId : list) {
                // tim cac seller chua thuc hien duoc lan thanh cong nao truoc do
                BasicDBObject query = new BasicDBObject();
                query.append("sellerId", sellerId);
                //
                BasicDBObject clause3 = new BasicDBObject();
                clause3.put("shipmentUpdateTime", new BasicDBObject("$lt", TextUtils.getTime(System.currentTimeMillis(), false)));
                clause3.append("shipmentStatus", "DELIVERED");
                BasicDBObject clause4 = new BasicDBObject();
                clause4.put("paidTime", new BasicDBObject("$lt", TextUtils.getTime(System.currentTimeMillis(), false)));
                clause4.append("paymentStatus", "PAID");
                BasicDBList or2 = new BasicDBList();
                or2.add(clause3);
                or2.add(clause4);
                query.put("$or", or2);
                //
//                query.put("shipmentUpdateTime", new BasicDBObject("$lt", TextUtils.getTime(System.currentTimeMillis(), false)));
//                query.append("shipmentStatus", "DELIVERED");
                long countBefore = myColl.count(query);
                if (countBefore == 0) {
                    result.add(sellerId);
                }
            }
        } else {
            result = list;
        }
        return result.size();
    }

    public long countFirstSuccessSeller(boolean all, long time) {
        //"paymentStatus": "PAID",
        //"shipmentStatus": "DELIVERED",
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        BasicDBObject clause1 = new BasicDBObject();
        if (!all) {
            clause1.put("shipmentUpdateTime", new BasicDBObject("$gte", TextUtils.getTime(time, false)).append("$lt", TextUtils.getTime(time, true)));
        }
        clause1.append("shipmentStatus", "DELIVERED");
        BasicDBObject clause2 = new BasicDBObject();
        if (!all) {
            clause2.put("paidTime", new BasicDBObject("$gte", TextUtils.getTime(time, false)).append("$lt", TextUtils.getTime(time, true)));
        }
        clause2.append("paymentStatus", "PAID");
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        gtQuery.put("$or", or);
        List<String> list = myColl.distinct("sellerId", gtQuery);
        List<String> result = new ArrayList<String>();
        if (!all) {
            for (String sellerId : list) {
                // tim cac seller chua thuc hien duoc lan thanh cong nao truoc do
                BasicDBObject query = new BasicDBObject();
                query.append("sellerId", sellerId);
                //
                BasicDBObject clause3 = new BasicDBObject();
                clause3.put("shipmentUpdateTime", new BasicDBObject("$lt", TextUtils.getTime(time, false)));
                clause3.append("shipmentStatus", "DELIVERED");
                BasicDBObject clause4 = new BasicDBObject();
                clause4.put("paidTime", new BasicDBObject("$lt", TextUtils.getTime(time, false)));
                clause4.append("paymentStatus", "PAID");
                BasicDBList or2 = new BasicDBList();
                or2.add(clause3);
                or2.add(clause4);
                query.put("$or", or2);
                //
//                query.put("shipmentUpdateTime", new BasicDBObject("$lt", TextUtils.getTime(System.currentTimeMillis(), false)));
//                query.append("shipmentStatus", "DELIVERED");
                long countBefore = myColl.count(query);
                if (countBefore == 0) {
                    result.add(sellerId);
                }
            }
        } else {
            result = list;
        }
        return result.size();
    }

    public long countReturnedSeller(boolean all) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("shipmentUpdateTime", new BasicDBObject("$gte", getTime(System.currentTimeMillis(), false)).append("$lt", getTime(System.currentTimeMillis(), true)));
        }
        gtQuery.append("shipmentStatus", "RETURN");
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public long countReturnedSeller(boolean all, long time) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        if (!all) {
            gtQuery.put("shipmentUpdateTime", new BasicDBObject("$gte", getTime(time, false)).append("$lt", getTime(time, true)));
        }
        gtQuery.append("shipmentStatus", "RETURN");
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public List<String> getBuyerIdFromOrder(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("createTime", new BasicDBObject("$gte", startTime).append("$lte", endTime));
        List<String> list = myColl.distinct("buyerId", gtQuery);
        return list;
    }

    public long countTotalSuccessSeller(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        BasicDBObject clause1 = new BasicDBObject();
        clause1.put("shipmentUpdateTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        clause1.append("shipmentStatus", "DELIVERED");
        BasicDBObject clause2 = new BasicDBObject();
        clause2.put("paidTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        clause2.append("paymentStatus", "PAID");
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        gtQuery.put("$or", or);
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public long countReturnedSeller(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("shipmentUpdateTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        gtQuery.append("shipmentStatus", "RETURN");
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();

    }

    public long countTotalOrderedSeller(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("createTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        List<String> list = myColl.distinct("sellerId", gtQuery);
        return list.size();
    }

    public long countBuyerUnique(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("createTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        List listBuyerUnique = myColl.distinct("buyerPhone", gtQuery);
//        List listBuyerUnique = getMongo().getCollection("order").distinct("buyerEmail");
        return listBuyerUnique.size();
    }

    public long countBuyerFirstPhone(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("createTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));

        List<String> listPhoneBetween = myColl.distinct("buyerPhone", gtQuery);
        List<String> result = new ArrayList();
        BasicDBObject query = new BasicDBObject();
        query.put("createTime", new BasicDBObject("$lt", startTime));
        List<String> listPhoneOutSite = myColl.distinct("buyerPhone", query);

        for (String listOutSite : listPhoneBetween) {
            if (!listPhoneOutSite.contains(listOutSite)) {
                result.add(listOutSite);
            }
        }
        return result.size();
    }

    public long getTotalBuyerReturn(long startTime, long endTime) {
        DBCollection myColl = getMongo().getCollection("order");
        BasicDBObject gtQuery = new BasicDBObject();
        gtQuery.put("shipmentUpdateTime", new BasicDBObject("$gte", startTime).append("$lt", endTime));
        gtQuery.append("shipmentStatus", "RETURN");
        List<String> list = myColl.distinct("buyerPhone", gtQuery);
        return list.size();
    }

    public long getTotalSoldBuyers(long startTime, long endTime) {
        Criteria cri = new Criteria();
        List<String> userIds = new ArrayList<>();
        Criteria cri1 = new Criteria();
        cri1.and("paidTime").gte(startTime).lte(endTime);
        cri1.and("paymentStatus").is(PaymentStatus.PAID.toString());
        Criteria cri2 = new Criteria();
        cri2.and("shipmentUpdateTime").gte(startTime).lte(endTime);
        cri2.and("shipmentStatus").is(ShipmentStatus.DELIVERED.toString());
        cri.orOperator(cri1, cri2);
        List<Order> orders = find(new Query(cri));
        for (Order order : orders) {
            if (!userIds.contains(order.getBuyerPhone())) {
                userIds.add(order.getBuyerPhone());
            }
        }
        return userIds.size();
    }

    public Map<String, Long> sumPriceFinal(Criteria cri) {
        Aggregation aggregations = Aggregation.newAggregation(
                match(cri),
                group("sellerId").sum("finalPrice").as("finalPrice"),
                project("finalPrice")
        );
        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregations, "order", DBObject.class);
        List<DBObject> fieldList = results.getMappedResults();
        Map<String, Long> map = new HashMap<>();
        if (fieldList != null && !fieldList.isEmpty()) {
            for (DBObject db : fieldList) {
                map.put(db.get("_id").toString(), parseLong(db.get("finalPrice").toString()));
            }
        }
        return map;
    }

    public int getOrderCount(String orderId) {
        Order order = getMongo().findAndModify(new Query(new Criteria("_id").is(orderId)), new Update().inc("count", 1), new FindAndModifyOptions().returnNew(true), getEntityClass());
        return order.getCount();

    }

}
