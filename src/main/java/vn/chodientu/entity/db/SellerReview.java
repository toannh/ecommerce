/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.db;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.chodientu.entity.enu.ReviewType;

/**
 *
 * @author thanhvv
 */
@Document
public class SellerReview implements Serializable {

    @Id
    private String id;
    @Indexed
    private String sellerId;
    @Indexed
    private String orderId;
    private String content;
    @Indexed
    private ReviewType reviewType;
    @Indexed
    private int productQuality;
    @Indexed
    private int interactive;
    @Indexed
    private int shippingCosts;
    @Indexed
    private long createTime;
    @Indexed
    private String userId;
    @Indexed
    private String emailAdmin;
    @Indexed
    private boolean active;
    private String ip;
    private long updateTime;
    @Transient
    private String userNameBuyer;
    @Transient
    private String userNameSeller;
    @Transient
    private List<String> itemIds;
    private List<String> userIds;
    private String object;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

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

    public String getUserNameBuyer() {
        return userNameBuyer;
    }

    public void setUserNameBuyer(String userNameBuyer) {
        this.userNameBuyer = userNameBuyer;
    }

    public String getUserNameSeller() {
        return userNameSeller;
    }

    public void setUserNameSeller(String userNameSeller) {
        this.userNameSeller = userNameSeller;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
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

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }
}
