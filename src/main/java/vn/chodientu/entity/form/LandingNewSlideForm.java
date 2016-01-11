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
public class LandingNewSlideForm implements Serializable {

    @Id
    private String id;
    @NotEmpty(message = "Bạn phải nhập name")
    private String name;
    private String landingNewId;
    private int position;
    @NotEmpty(message = "Bạn phải nhập url")
    private String url;
    private boolean active;
    private MultipartFile banner;

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

    public String getLandingNewId() {
        return landingNewId;
    }

    public void setLandingNewId(String landingNewId) {
        this.landingNewId = landingNewId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public MultipartFile getBanner() {
        return banner;
    }

    public void setBanner(MultipartFile banner) {
        this.banner = banner;
    }

   

}
