package vn.chodientu.entity.db;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BigLandingSeller implements Serializable {

    @Id
    private String id;
    @Indexed
    private String sellerId;
    @Indexed
    private String promotionId;
    @Indexed
    private String promotionName;
    @Indexed
    private String sellerName;
    @Indexed
    private String biglandingId;
    @Indexed
    private int position;
    @Indexed
    private boolean active;
    @Transient
    private String logoShop;
    @Indexed
    private String alias;
    @Transient
    private List<Item> items;

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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBiglandingId(String biglandingId) {
        this.biglandingId = biglandingId;
    }

    public String getBiglandingId() {
        return biglandingId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public String getLogoShop() {
        return logoShop;
    }

    public void setLogoShop(String logoShop) {
        this.logoShop = logoShop;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
