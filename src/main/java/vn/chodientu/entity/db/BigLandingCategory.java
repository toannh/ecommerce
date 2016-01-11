package vn.chodientu.entity.db;

import java.io.Serializable;
import java.util.List;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BigLandingCategory implements Serializable {

    @Id
    private String id;
    @Indexed
    @NotBlank(message = "Bạn chưa nhập tên danh mục")
    private String name;
    @Indexed
    private String parentId;
    @Indexed
    private String bigLandingId;
    @Indexed
    private boolean active;
    @Indexed
    private int position;
    @Indexed
    private String template;
    @Transient
    private List<BigLandingCategory> categorySubs;
    @Transient
    private List<BigLandingItem> bigLandingItem;
    @Transient
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<BigLandingCategory> getCategorySubs() {
        return categorySubs;
    }

    public void setCategorySubs(List<BigLandingCategory> categorySubs) {
        this.categorySubs = categorySubs;
    }

    public List<BigLandingItem> getBigLandingItem() {
        return bigLandingItem;
    }

    public void setBigLandingItem(List<BigLandingItem> bigLandingItem) {
        this.bigLandingItem = bigLandingItem;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBigLandingId() {
        return bigLandingId;
    }

    public void setBigLandingId(String bigLandingId) {
        this.bigLandingId = bigLandingId;
    }

}
