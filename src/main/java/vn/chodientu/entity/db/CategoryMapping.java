/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.chodientu.entity.enu.CategoryMappingType;

/**
 *
 * @author CANH
 */
@Document
public class CategoryMapping implements Serializable {

    @Id
    private String id;
    @Indexed
    private String originCategoryId;
    @Indexed
    private String destCategoryId;
    // save all same properties between origin cate and destination cate
    private Map<String, String> sameProperties;
    // save all property value of same cateproperties
    private Map<String, String> listPropertiesValues;
    private Map<String, String> newProperty;
    @Indexed
    private CategoryMappingType running;
    private String userId;
    private long createTime;
    private long startTime;
    private long endTime;
    @Indexed
    private boolean active;
    @Transient
    private Category oriCate;
    @Transient
    private Category destCate;
    @Transient
    private Administrator user;
    @Transient
    private List<CategoryProperty> listSameCateProp;
    @Transient
    private List<CategoryPropertyValue> listPropVal;

    public Category getOriCate() {
        return oriCate;
    }

    public void setOriCate(Category oriCate) {
        this.oriCate = oriCate;
    }

    public Category getDestCate() {
        return destCate;
    }

    public void setDestCate(Category destCate) {
        this.destCate = destCate;
    }

    public Administrator getUser() {
        return user;
    }

    public void setUser(Administrator user) {
        this.user = user;
    }

    public Map<String, String> getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(Map<String, String> newProperty) {
        this.newProperty = newProperty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginCategoryId() {
        return originCategoryId;
    }

    public void setOriginCategoryId(String originCategoryId) {
        this.originCategoryId = originCategoryId;
    }

    public String getDestCategoryId() {
        return destCategoryId;
    }

    public void setDestCategoryId(String destCategoryId) {
        this.destCategoryId = destCategoryId;
    }

    public Map<String, String> getSameProperties() {
        return sameProperties;
    }

    public void setSameProperties(Map<String, String> sameProperties) {
        this.sameProperties = sameProperties;
    }

    public Map<String, String> getListPropertiesValues() {
        return listPropertiesValues;
    }

    public void setListPropertiesValues(Map<String, String> listPropertiesValues) {
        this.listPropertiesValues = listPropertiesValues;
    }

    public CategoryMappingType getRunning() {
        return running;
    }

    public void setRunning(CategoryMappingType running) {
        this.running = running;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<CategoryProperty> getListSameCateProp() {
        return listSameCateProp;
    }

    public void setListSameCateProp(List<CategoryProperty> listSameCateProp) {
        this.listSameCateProp = listSameCateProp;
    }

    public List<CategoryPropertyValue> getListPropVal() {
        return listPropVal;
    }

    public void setListPropVal(List<CategoryPropertyValue> listPropVal) {
        this.listPropVal = listPropVal;
    }

    @Override
    public String toString() {
        return "" + "id :" + id
                + "ori : " + originCategoryId
                + "dest :" + destCategoryId
                + "same : " + sameProperties.toString() + "- length : " + sameProperties.size()
                + "listprop : " + listPropertiesValues.toString() + "- length : " + listPropertiesValues.size()
                + "type : " + running
                + "user ID : " + userId
                + "create : " + createTime
                + "startTime : " + startTime
                + "entime : " + endTime
                + "avtive : " + active;
    }

}
