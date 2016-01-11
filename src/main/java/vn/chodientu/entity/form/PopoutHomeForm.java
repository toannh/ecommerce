package vn.chodientu.entity.form;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

public class PopoutHomeForm {
    
    @Id
    private String id;
    @NotBlank(message = "Tiêu đề tin không được để trống")
    private String title;
    @NotBlank(message = "Url không được để trống")
    private String url;
    private int type;
    private boolean active;
    private long time;
    private MultipartFile imagefile;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public MultipartFile getImagefile() {
        return imagefile;
    }

    public void setImagefile(MultipartFile imagefile) {
        this.imagefile = imagefile;
    }

}
