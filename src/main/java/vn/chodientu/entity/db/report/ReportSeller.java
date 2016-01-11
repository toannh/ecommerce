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
public class ReportSeller implements Serializable{

    @Id
    private long time;
    @Indexed
    private long createTime;
    @Indexed
    private long updateTime;
    private long newSeller;
    private long sucesSeller;
    private long firstSucesSeller;
    private long returnSeller;
    private long scSeller;
    private long totalNewSeller;
    private long totalSucesSeller;
    private long totalFirstSucesSeller;
    private long totalReturnSeller;
    private long totalSCSeller;

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

    public long getNewSeller() {
        return newSeller;
    }

    public void setNewSeller(long newSeller) {
        this.newSeller = newSeller;
    }

    public long getSucesSeller() {
        return sucesSeller;
    }

    public void setSucesSeller(long sucesSeller) {
        this.sucesSeller = sucesSeller;
    }

    public long getFirstSucesSeller() {
        return firstSucesSeller;
    }

    public void setFirstSucesSeller(long firstSucesSeller) {
        this.firstSucesSeller = firstSucesSeller;
    }

    public long getReturnSeller() {
        return returnSeller;
    }

    public void setReturnSeller(long returnSeller) {
        this.returnSeller = returnSeller;
    }

    public long getScSeller() {
        return scSeller;
    }

    public void setScSeller(long scSeller) {
        this.scSeller = scSeller;
    }

    public long getTotalNewSeller() {
        return totalNewSeller;
    }

    public void setTotalNewSeller(long totalNewSeller) {
        this.totalNewSeller = totalNewSeller;
    }

    public long getTotalSucesSeller() {
        return totalSucesSeller;
    }

    public void setTotalSucesSeller(long totalSucesSeller) {
        this.totalSucesSeller = totalSucesSeller;
    }

    public long getTotalFirstSucesSeller() {
        return totalFirstSucesSeller;
    }

    public void setTotalFirstSucesSeller(long totalFirstSucesSeller) {
        this.totalFirstSucesSeller = totalFirstSucesSeller;
    }

    public long getTotalReturnSeller() {
        return totalReturnSeller;
    }

    public void setTotalReturnSeller(long totalReturnSeller) {
        this.totalReturnSeller = totalReturnSeller;
    }

    public long getTotalSCSeller() {
        return totalSCSeller;
    }

    public void setTotalSCSeller(long totalSCSeller) {
        this.totalSCSeller = totalSCSeller;
    }

}
