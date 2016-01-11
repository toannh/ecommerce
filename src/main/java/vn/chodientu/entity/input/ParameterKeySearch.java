/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.input;

import java.io.Serializable;

/**
 *
 * @author taind
 */
public class ParameterKeySearch implements Serializable{

    private String keyConvention;
    private String key;
    private String value;
    private int orderBy;
    private int pageIndex;
    private int pageSize;

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getKeyConvention() {
        return keyConvention;
    }

    public void setKeyConvention(String keyConvention) {
        this.keyConvention = keyConvention;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
