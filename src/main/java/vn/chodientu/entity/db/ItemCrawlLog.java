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
import vn.chodientu.entity.api.Request;
import vn.chodientu.entity.data.KeyVal64;
import vn.chodientu.entity.enu.CrawlImageStatus;
import vn.chodientu.entity.enu.CrawlType;
import vn.chodientu.entity.output.Response;

/**
 *
 * @author CANH
 */
@Document
public class ItemCrawlLog implements Serializable {

    @Id
    @Indexed
    private String id;
    private String sourceLink;
    @Indexed
    private String itemId;
    @Indexed
    private String sellerId;
    @Indexed
    private CrawlType type; // add / edit (ENUM)
    @Indexed
    private List<String> errorCode = new ArrayList<>(); //(cần nơi để lấy từ code ra message)
    @Indexed
    private List<String> alertCode = new ArrayList<>(); //(cần nơi để lấy từ code ra message)
    @Indexed
    private CrawlImageStatus imageStatus;
    @Indexed
    private long time;
    @Indexed
    private boolean status; // data SP Crawl về OK hoặc k
    @Indexed
    private List<KeyVal64> request;
    @Transient
    private List<String> errorMessage;
    @Transient
    private List<String> alertMessage;
    @Transient
    private Item item;

    public Item getItem() {
        return item;
    }

    public List<KeyVal64> getRequest() {
        return request;
    }

    public void setRequest(List<KeyVal64> request) {
        this.request = request;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public CrawlType getType() {
        return type;
    }

    public void setType(CrawlType type) {
        this.type = type;
    }

    public List<String> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(List<String> errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getAlertCode() {
        return alertCode;
    }

    public void setAlertCode(List<String> alertCode) {
        this.alertCode = alertCode;
    }

    public CrawlImageStatus getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(CrawlImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(List<String> alertMessage) {
        this.alertMessage = alertMessage;
    }

}
