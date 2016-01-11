package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.chodientu.entity.enu.SellerPostStatus;

@Document
public class SellerPostFacebookItem implements Serializable {

    @Id
    private String id;
    @Indexed
    private String postFacebookId;
    @Indexed
    private String description;
    @Indexed
    private SellerPostStatus status;
    @Indexed
    private long createTime;
    @Indexed
    private long endTime;
    @Indexed
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    public SellerPostStatus getStatus() {
        return status;
    }

    public void setStatus(SellerPostStatus status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPostFacebookId() {
        return postFacebookId;
    }

    public void setPostFacebookId(String postFacebookId) {
        this.postFacebookId = postFacebookId;
    }


}
