package vn.chodientu.entity.data;

import java.io.Serializable;

/**
 * @since Jun 10, 2014
 * @author Phu
 */
public class GroupFacebook implements Serializable{

    private String name;
    private String id;

    public GroupFacebook(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    
}
