package vn.chodientu.repository;

import com.mongodb.DBObject;
import static java.lang.Long.parseLong;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import vn.chodientu.entity.db.report.ReportOrder;

/**
 * @since May 20, 2014
 * @author PhuongDT
 */
@Repository
public class ReportOrderRepository extends BaseRepository<ReportOrder> {

    public ReportOrderRepository() {
        super(ReportOrder.class);
    }

    public ReportOrder find(long time) {
        return getMongo().findOne(new Query(new Criteria("time").is(time)), getEntityClass());
    }

    public Map<String, Long> reportSumOrderFinal(Criteria cri) {
        Aggregation aggregations = Aggregation.newAggregation(
                match(new Criteria()),
                group("null")
                .sum("newStatus").as("newStatus")
                .sum("nlPayment").as("nlPayment")
                .sum("codPayment").as("codPayment")
                .sum("paidStatus").as("paidStatus")
                .sum("nlPaymentPaid").as("nlPaymentPaid")
                .sum("codPaymentPaid").as("codPaymentPaid")
                .sum("orderReturn").as("orderReturn")
                .sum("quantity").as("quantity")
                .sum("nonePayment").as("nonePayment"),
                project("newStatus",
                        "nlPayment",
                        "codPayment",
                        "paidStatus",
                        "nlPaymentPaid",
                        "codPaymentPaid",
                        "orderReturn",
                        "quantity","nonePayment")
        );
        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregations, "reportOrder", DBObject.class);
        List<DBObject> fieldList = results.getMappedResults();
        Map<String, Long> map = new HashMap<>();
        if (fieldList != null && !fieldList.isEmpty()) {
            for (DBObject db : fieldList) {
                map.put("newStatus", parseLong(db.get("newStatus").toString()));
                map.put("nlPayment", parseLong(db.get("nlPayment").toString()));
                map.put("codPayment", parseLong(db.get("codPayment").toString()));
                map.put("paidStatus", parseLong(db.get("paidStatus").toString()));
                map.put("nlPaymentPaid", parseLong(db.get("nlPaymentPaid").toString()));
                map.put("codPaymentPaid", parseLong(db.get("codPaymentPaid").toString()));
                map.put("orderReturn", parseLong(db.get("orderReturn").toString()));
                map.put("quantity", parseLong(db.get("quantity").toString()));
                map.put("nonePayment", parseLong(db.get("nonePayment").toString()));
            }
        }
        return map;
    }

    public Map<String, Long> reportSumOrder(Criteria cri) {
        List<ReportOrder> reportOrders = this.find(new Query(cri));
        long newStatus = 0;
        long nlPayment = 0;
        long codPayment = 0;
        long paidStatus = 0;
        long nlPaymentPaid = 0;
        long codPaymentPaid = 0;
        long orderReturn = 0;
        long quantity = 0;
        long nonePayment = 0;

        for (ReportOrder reportOrder : reportOrders) {
            newStatus += reportOrder.getNewStatus();
            nlPayment += reportOrder.getNlPayment();
            codPayment += reportOrder.getCodPayment();
            paidStatus += reportOrder.getPaidStatus();
            nlPaymentPaid += reportOrder.getNlPaymentPaid();
            codPaymentPaid += reportOrder.getCodPaymentPaid();
            orderReturn += reportOrder.getOrderReturn();
            quantity += reportOrder.getQuantity();
            nonePayment += reportOrder.getNonePayment();
        }
        Map<String, Long> map = new HashMap<>();
        map.put("newStatus", newStatus);
        map.put("nlPayment", nlPayment);
        map.put("codPayment", codPayment);
        map.put("paidStatus", paidStatus);
        map.put("nlPaymentPaid", nlPaymentPaid);
        map.put("codPaymentPaid", codPaymentPaid);
        map.put("orderReturn", orderReturn);
        map.put("quantity", quantity);
        map.put("nonePayment", nonePayment);
        return map;

    }

    public Map<String, Long> reportSumGMV(Criteria cri) {
        Aggregation aggregations = Aggregation.newAggregation(
                match(new Criteria()),
                group("null")
                .sum("finalPriceNewStatus").as("finalPriceNewStatus")
                .sum("finalPriceNlPayment").as("finalPriceNlPayment")
                .sum("finalPriceCodPayment").as("finalPriceCodPayment")
                .sum("finalPrice").as("finalPrice")
                .sum("finalPricePaidStatus").as("finalPricePaidStatus")
                .sum("newStatus").as("newStatus")
                .sum("nlPayment").as("nlPayment")
                .sum("codPayment").as("codPayment")
                .sum("paidStatus").as("paidStatus")
                .sum("finalPricePaidStatusNL").as("finalPricePaidStatusNL")
                .sum("finalPricePaidStatusCOD").as("finalPricePaidStatusCOD")
                .sum("finalPriceReturn").as("finalPriceReturn")
                .sum("nlPaymentPaid").as("nlPaymentPaid")
                .sum("codPaymentPaid").as("codPaymentPaid")
                .sum("orderReturn").as("orderReturn")
                .sum("quantity").as("quantity")
                .sum("nonePayment").as("nonePayment")
                .sum("finalPriceNonePayment").as("finalPriceNonePayment"),
                project("finalPriceNewStatus",
                        "finalPriceNlPayment",
                        "finalPriceCodPayment",
                        "finalPrice", "finalPricePaidStatus", "newStatus",
                        "nlPayment",
                        "codPayment",
                        "paidStatus",
                        "finalPricePaidStatusNL",
                        "finalPricePaidStatusCOD",
                        "finalPriceReturn",
                        "nlPaymentPaid",
                        "codPaymentPaid",
                        "orderReturn",
                        "quantity",
                        "nonePayment",
                        "finalPriceNonePayment"
                        )
        );
        AggregationResults<DBObject> results = mongoTemplate.aggregate(aggregations, "reportOrder", DBObject.class);
        List<DBObject> fieldList = results.getMappedResults();
        Map<String, Long> map = new HashMap<>();
        if (fieldList != null && !fieldList.isEmpty()) {
            for (DBObject db : fieldList) {
                map.put("finalPriceNewStatus", parseLong(db.get("finalPriceNewStatus").toString()));
                map.put("finalPriceNlPayment", parseLong(db.get("finalPriceNlPayment").toString()));
                map.put("finalPriceCodPayment", parseLong(db.get("finalPriceCodPayment").toString()));
                map.put("finalPrice", parseLong(db.get("finalPrice").toString()));
                map.put("finalPricePaidStatus", parseLong(db.get("finalPricePaidStatus").toString()));
                map.put("newStatus", parseLong(db.get("newStatus").toString()));
                map.put("nlPayment", parseLong(db.get("nlPayment").toString()));
                map.put("codPayment", parseLong(db.get("codPayment").toString()));
                map.put("paidStatus", parseLong(db.get("paidStatus").toString()));
                map.put("finalPricePaidStatusNL", parseLong(db.get("finalPricePaidStatusNL").toString()));
                map.put("finalPricePaidStatusCOD", parseLong(db.get("finalPricePaidStatusCOD").toString()));
                map.put("finalPriceReturn", parseLong(db.get("finalPriceReturn").toString()));
                map.put("nlPaymentPaid", parseLong(db.get("nlPaymentPaid").toString()));
                map.put("codPaymentPaid", parseLong(db.get("codPaymentPaid").toString()));
                map.put("orderReturn", parseLong(db.get("orderReturn").toString()));
                map.put("quantity", parseLong(db.get("quantity").toString()));
                map.put("nonePayment", parseLong(db.get("nonePayment").toString()));
                map.put("finalPriceNonePayment", parseLong(db.get("finalPriceNonePayment").toString()));
            }
        }
        return map;
    }

    public Map<String, Long> reportSumGMVTime(Criteria cri) {
        List<ReportOrder> reportOrders = this.find(new Query(cri));
        long finalPriceNewStatus = 0;
        long finalPriceNlPayment = 0;
        long finalPriceCodPayment = 0;
        long finalPrice = 0;
        long finalPricePaidStatus = 0;
        long newStatus = 0;
        long nlPayment = 0;
        long codPayment = 0;
        long paidStatus = 0;
        long finalPricePaidStatusNL = 0;
        long finalPricePaidStatusCOD = 0;
        long finalPriceReturn = 0;
        long nlPaymentPaid = 0;
        long codPaymentPaid = 0;
        long orderReturn = 0;
        long quantity = 0;
        long nonePayment = 0;
        long finalPriceNonePayment = 0;

        for (ReportOrder reportOrder : reportOrders) {
            finalPriceNewStatus += reportOrder.getFinalPriceNewStatus();
            finalPriceNlPayment += reportOrder.getFinalPriceNlPayment();
            finalPriceCodPayment += reportOrder.getFinalPriceCodPayment();
            finalPrice += reportOrder.getFinalPrice();
            finalPricePaidStatus += reportOrder.getFinalPricePaidStatus();
            newStatus += reportOrder.getNewStatus();
            nlPayment += reportOrder.getNlPayment();
            codPayment += reportOrder.getCodPayment();
            paidStatus += reportOrder.getPaidStatus();
            finalPricePaidStatusNL += reportOrder.getFinalPricePaidStatusNL();
            finalPricePaidStatusCOD += reportOrder.getFinalPricePaidStatusCOD();
            finalPriceReturn += reportOrder.getFinalPriceReturn();
            nlPaymentPaid += reportOrder.getNlPaymentPaid();
            codPaymentPaid += reportOrder.getCodPaymentPaid();
            orderReturn += reportOrder.getOrderReturn();
            quantity += reportOrder.getQuantity();
            nonePayment += reportOrder.getNonePayment();
            finalPriceNonePayment += reportOrder.getFinalPriceNonePayment();
        }
        Map<String, Long> map = new HashMap<>();
        map.put("finalPriceNewStatus", finalPriceNewStatus);
        map.put("finalPriceNlPayment", finalPriceNlPayment);
        map.put("finalPriceCodPayment", finalPriceCodPayment);
        map.put("finalPrice", finalPrice);
        map.put("finalPricePaidStatus", finalPricePaidStatus);
        map.put("newStatus", newStatus);
        map.put("nlPayment", nlPayment);
        map.put("codPayment", codPayment);
        map.put("paidStatus", paidStatus);
        map.put("finalPricePaidStatusNL", finalPricePaidStatusNL);
        map.put("finalPricePaidStatusCOD", finalPricePaidStatusCOD);
        map.put("finalPriceReturn", finalPriceReturn);
        map.put("nlPaymentPaid", nlPaymentPaid);
        map.put("codPaymentPaid", codPaymentPaid);
        map.put("orderReturn", orderReturn);
        map.put("quantity", quantity);
        map.put("nonePayment", nonePayment);
        map.put("finalPriceNonePayment", finalPriceNonePayment);
        return map;

    }

}
