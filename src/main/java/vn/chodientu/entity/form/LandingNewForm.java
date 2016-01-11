package vn.chodientu.entity.form;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

/**
 * @since Jul 17, 2014
 * @author phuongdt
 */
@Document
public class LandingNewForm implements Serializable {

    @Id
    private String id;
    @NotEmpty(message = "Tên landing new không được để trống")
    private String name;
    private String color;
    @NotEmpty(message = "Description không được để trống")
    private String description;
    private boolean active;
    private MultipartFile bannerCenter;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getBannerCenter() {
        return bannerCenter;
    }

    public void setBannerCenter(MultipartFile bannerCenter) {
        this.bannerCenter = bannerCenter;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
