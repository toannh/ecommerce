package vn.chodientu.entity.form;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Anhpp
 */
public class BigLandingForm {

    private String id;
    @NotEmpty(message = "Tên chương trình không được để trống")
    private String name;
    private String description;
    private String descriptionOrder;
    private String landingTemplate;
    private boolean centerBannerActive;
    private long startTime;
    private long endTime;
    private long startTimeSeller;
    private long endTimeSeller;
    private boolean active;

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

    public String getLandingTemplate() {
        return landingTemplate;
    }

    public void setLandingTemplate(String landingTemplate) {
        this.landingTemplate = landingTemplate;
    }

    public boolean isCenterBannerActive() {
        return centerBannerActive;
    }

    public void setCenterBannerActive(boolean centerBannerActive) {
        this.centerBannerActive = centerBannerActive;
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

}
