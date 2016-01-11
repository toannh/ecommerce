/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.form;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author thanhvv
 */
@Document
public class SellerReviewForm implements Serializable {

    private String id;
    private String sellerId;
    private String orderId;
    private String content;
    private String reviewType;
    private int productQuality;
    private int interactive;
    private int shippingCosts;
    private long shipmentPrice;
    private long createTime;
    private String userId;
    private boolean active;
    private String ip;
    private long updateTime;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getReviewType() {
        return reviewType;
    }

    public void setReviewType(String reviewType) {
        this.reviewType = reviewType;
    }

    public int getProductQuality() {
        return productQuality;
    }

    public void setProductQuality(int productQuality) {
        this.productQuality = productQuality;
    }

    public int getInteractive() {
        return interactive;
    }

    public void setInteractive(int interactive) {
        this.interactive = interactive;
    }

    public int getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(int shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    public long getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(long shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }
}
