/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author CANH
 */
@Document
public class SellerCrawlLog implements Serializable {

    @Id
    @Indexed
    private String id;
    private String sellerId;
    @Indexed
    private long totalRequest;
    @Indexed
    private long successRequest;
    @Indexed
    private long errorRequest;
    @Indexed
    private long addRequest;
    @Indexed
    private long editRequest;
    @Indexed
    private long noCondition;
    @Indexed
    private long enoughCondition;
    @Indexed
    private long time;
    @Transient
    private User seller;
    @Indexed
    private long itemOK;
    @Indexed
    private long itemMiss;

    public long getItemOK() {
        return itemOK;
    }

    public void setItemOK(long itemOK) {
        this.itemOK = itemOK;
    }

    public long getItemMiss() {
        return itemMiss;
    }

    public void setItemMiss(long itemMiss) {
        this.itemMiss = itemMiss;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public long getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public long getSuccessRequest() {
        return successRequest;
    }

    public void setSuccessRequest(long successRequest) {
        this.successRequest = successRequest;
    }

    public long getErrorRequest() {
        return errorRequest;
    }

    public void setErrorRequest(long errorRequest) {
        this.errorRequest = errorRequest;
    }

    public long getAddRequest() {
        return addRequest;
    }

    public void setAddRequest(long addRequest) {
        this.addRequest = addRequest;
    }

    public long getEditRequest() {
        return editRequest;
    }

    public void setEditRequest(long editRequest) {
        this.editRequest = editRequest;
    }

    public long getNoCondition() {
        return noCondition;
    }

    public void setNoCondition(long noCondition) {
        this.noCondition = noCondition;
    }

    public long getEnoughCondition() {
        return enoughCondition;
    }

    public void setEnoughCondition(long enoughCondition) {
        this.enoughCondition = enoughCondition;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

}
