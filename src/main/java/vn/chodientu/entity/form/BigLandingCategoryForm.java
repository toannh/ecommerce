package vn.chodientu.entity.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Anhpp
 */
public class BigLandingCategoryForm {

    private String id;
    @NotEmpty(message = "Tên danh mục không được để trống")
    private String name;
    private String parentId;
    private String bigLandingId;
    private boolean active;
    private int position;

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

    public String getBigLandingId() {
        return bigLandingId;
    }

    public void setBigLandingId(String bigLandingId) {
        this.bigLandingId = bigLandingId;
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

}
