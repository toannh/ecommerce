package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BigLanding implements Serializable {

    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String description;
    @Indexed
    private String descriptionOrder;
    @Indexed
    private String landingTemplate;
    @Indexed
    private long startTime;
    @Indexed
    private long endTime;
    @Indexed
    private long startTimeSeller;
    @Indexed
    private long endTimeSeller;
    @Indexed
    private boolean active;
    @Indexed
    private boolean centerBannerActive;
    @Indexed
    private String background;
    @Transient
    private String heartBanner;
    @Transient
    private String centerBanner;
    @Transient
    private String logoBanner;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeartBanner() {
        return heartBanner;
    }

    public void setHeartBanner(String heartBanner) {
        this.heartBanner = heartBanner;
    }

    public String getCenterBanner() {
        return centerBanner;
    }

    public void setCenterBanner(String centerBanner) {
        this.centerBanner = centerBanner;
    }

    public String getLogoBanner() {
        return logoBanner;
    }

    public void setLogoBanner(String logoBanner) {
        this.logoBanner = logoBanner;
    }

    public boolean isCenterBannerActive() {
        return centerBannerActive;
    }

    public void setCenterBannerActive(boolean centerBannerActive) {
        this.centerBannerActive = centerBannerActive;
    }

    public String getLandingTemplate() {
        return landingTemplate;
    }

    public void setLandingTemplate(String landingTemplate) {
        this.landingTemplate = landingTemplate;
    }

    public String getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(String descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }

    public long getStartTimeSeller() {
        return startTimeSeller;
    }

    public void setStartTimeSeller(long startTimeSeller) {
        this.startTimeSeller = startTimeSeller;
    }

    public long getEndTimeSeller() {
        return endTimeSeller;
    }

    public void setEndTimeSeller(long endTimeSeller) {
        this.endTimeSeller = endTimeSeller;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

}
