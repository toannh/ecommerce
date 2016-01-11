/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.db.report;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author CANH
 */
@Document
public class ReportBuyer implements Serializable {

    @Id
    private long time;
    @Indexed
    private long createTime;
    @Indexed
    private long updateTime;
    private long buyer;
    private long buyerOnce;
    private long buyerReturn;
    private long buyerSCIntegrated;
    private long totalBuyerUnique;
    private long totalBuyerOnce;
    private long totalBuyerReturn;
    private long totalBuyerSCIntegrated;
    private long soldBuyer;
    private long totalSoldBuyer;
 

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getBuyer() {
        return buyer;
    }

    public void setBuyer(long buyer) {
        this.buyer = buyer;
    }

    public long getBuyerOnce() {
        return buyerOnce;
    }

    public void setBuyerOnce(long buyerOnce) {
        this.buyerOnce = buyerOnce;
    }

    public long getBuyerReturn() {
        return buyerReturn;
    }

    public void setBuyerReturn(long buyerReturn) {
        this.buyerReturn = buyerReturn;
    }

    public long getBuyerSCIntegrated() {
        return buyerSCIntegrated;
    }

    public void setBuyerSCIntegrated(long buyerSCIntegrated) {
        this.buyerSCIntegrated = buyerSCIntegrated;
    }

    public long getTotalBuyerUnique() {
        return totalBuyerUnique;
    }

    public void setTotalBuyerUnique(long totalBuyerUnique) {
        this.totalBuyerUnique = totalBuyerUnique;
    }

    public long getTotalBuyerOnce() {
        return totalBuyerOnce;
    }

    public void setTotalBuyerOnce(long totalBuyerOnce) {
        this.totalBuyerOnce = totalBuyerOnce;
    }

    public long getTotalBuyerReturn() {
        return totalBuyerReturn;
    }

    public void setTotalBuyerReturn(long totalBuyerReturn) {
        this.totalBuyerReturn = totalBuyerReturn;
    }

    public long getTotalBuyerSCIntegrated() {
        return totalBuyerSCIntegrated;
    }

    public void setTotalBuyerSCIntegrated(long totalBuyerSCIntegrated) {
        this.totalBuyerSCIntegrated = totalBuyerSCIntegrated;
    }

    public long getSoldBuyer() {
        return soldBuyer;
    }

    public void setSoldBuyer(long soldBuyer) {
        this.soldBuyer = soldBuyer;
    }

    public long getTotalSoldBuyer() {
        return totalSoldBuyer;
    }

    public void setTotalSoldBuyer(long totalSoldBuyer) {
        this.totalSoldBuyer = totalSoldBuyer;
    }


}
