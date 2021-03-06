package vn.chodientu.entity.input;

import java.io.Serializable;

/**
 *
 * @author thanhvv
 */
public class EmailOutboxSearch implements Serializable {

    private String from;
    private String to;
    private int sent;
    private int success;
    private long timeFrom;
    private long timeTo;
    private long sentTimeFrom;
    private long sentTimeTo;
    private int type;
    private int pageIndex;
    private int pageSize;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }

    public long getSentTimeFrom() {
        return sentTimeFrom;
    }

    public void setSentTimeFrom(long sentTimeFrom) {
        this.sentTimeFrom = sentTimeFrom;
    }

    public long getSentTimeTo() {
        return sentTimeTo;
    }

    public void setSentTimeTo(long sentTimeTo) {
        this.sentTimeTo = sentTimeTo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
