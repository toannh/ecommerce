package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BigLandingItem implements Serializable {

    @Id
    private String id;
    @Indexed
    private String itemId;
    @Transient
    private Item item;
    @Indexed
    private String image;
    @Indexed
    private String name;
    @Indexed
    private String bigLandingCategoryId;
    @Indexed
    private int position;
    @Indexed
    private String promition;
    @Indexed
    private boolean active;
    @Indexed
    private boolean featured;
    @Indexed
    private String sellerName;
    @Indexed
    private String sellerImage;

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBigLandingCategoryId() {
        return bigLandingCategoryId;
    }

    public void setBigLandingCategoryId(String bigLandingCategoryId) {
        this.bigLandingCategoryId = bigLandingCategoryId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPromition() {
        return promition;
    }

    public void setPromition(String promition) {
        this.promition = promition;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

}
