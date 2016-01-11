package vn.chodientu.entity.input;

import java.io.Serializable;

/**
 *
 * @author phuongdt
 */
public class ModelSeoSearch implements Serializable {

    private String modelId;
    private String administrator;
    private int status; // 1.active,approved -- 2.inactive -- 3.unapproved
    private int pageIndex;
    private int pageSize;
    private int orderBy;
    private long createTimeFrom;
    private long createTimeTo;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public long getCreateTimeFrom() {
        return createTimeFrom;
    }

    public void setCreateTimeFrom(long createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
    }

    public long getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(long createTimeTo) {
        this.createTimeTo = createTimeTo;
    }
    


}
