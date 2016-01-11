package vn.chodientu.entity.db;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @since Jul 14, 2014
 * @author phuongdt
 */
@Document
public class LandingNewItem implements Serializable {
    @Id
    private String id;
    @Indexed
    @NotEmpty(message = "Mã sản phẩm không được để trống")
    private String itemId;
    @Indexed
    @NotEmpty(message = "Mã danh mục landing không được để trống")
    private String landingNewId;
    private String name;
    @Transient
    private Item item;
    @Transient
    private String image;
    @Indexed
    private int position;
    @Indexed
    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLandingNewId() {
        return landingNewId;
    }

    public void setLandingNewId(String landingNewId) {
        this.landingNewId = landingNewId;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}
