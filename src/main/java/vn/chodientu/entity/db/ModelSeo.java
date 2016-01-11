package vn.chodientu.entity.db;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ModelSeo implements Serializable {

    @Id
    private String modelId;
    @NotBlank(message = "Tiêu đề seo không được để trống !")
    private String title;
    @NotBlank(message = "Mô tả seo không được để trống !")
    private String description;
    private String content;
    private String contentProperties;
    private String note;
    @Indexed
    private String administrator;
    @Indexed
    private long createTime;
    @Indexed
    private long updateTime;
    @Indexed
    private long approvedTime;
    @Indexed
    private boolean active;
    @Transient
    private boolean propertiesFag;
    @Transient
    private String name;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentProperties() {
        return contentProperties;
    }

    public void setContentProperties(String contentProperties) {
        this.contentProperties = contentProperties;
    }

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(long approvedTime) {
        this.approvedTime = approvedTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPropertiesFag() {
        return propertiesFag;
    }

    public void setPropertiesFag(boolean propertiesFag) {
        this.propertiesFag = propertiesFag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
