package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import vn.chodientu.entity.enu.SellerPostStatus;

@Document
public class SellerPostFacebook implements Serializable {

    @Id
    private String id;
    @Indexed
    private String sellerId;
    @Indexed
    private long totalMoney;
    @Indexed
    private String facebookId;
    @Indexed
    private long totalGroup;
    @Indexed
    private long sucessGroup;
    @Indexed
    private SellerPostStatus status;
    @Indexed
    private long createTime;
    @Indexed
    private long endTime;
    @Indexed
    private String note;
    @Indexed
    private String timeFinish;
    @Indexed
    private String productId;
    @Indexed
    private String message;
    @Indexed
    private String resultLink;
    @Transient
    private String image;
    @Transient
    private String name;
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

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public long getTotalGroup() {
        return totalGroup;
    }

    public void setTotalGroup(long totalGroup) {
        this.totalGroup = totalGroup;
    }

    public String getTimeFinish() {
        return timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getResultLink() {
        return resultLink;
    }

    public void setResultLink(String resultLink) {
        this.resultLink = resultLink;
    }

    public long getSucessGroup() {
        return sucessGroup;
    }

    public void setSucessGroup(long sucessGroup) {
        this.sucessGroup = sucessGroup;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
