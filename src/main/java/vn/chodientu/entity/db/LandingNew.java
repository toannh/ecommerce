package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @since Jul 17, 2014
 * @author phuongdt
 */
@Document
public class LandingNew implements Serializable {
    @Id
    private String id;
    private String name;
    private String color;
    private String description;
    @Transient
    private String bannerCenter;
    private long time;
    private boolean active;
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

    public String getBannerCenter() {
        return bannerCenter;
    }

    public void setBannerCenter(String bannerCenter) {
        this.bannerCenter = bannerCenter;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
