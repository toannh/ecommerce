package vn.chodientu.entity.db;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ParameterKey implements Serializable {

    @Id
    private String keyConvention;
    @Indexed
    private String key;
    @Indexed
    private String value;

    public String getKeyConvention() {
        return keyConvention;
    }

    public void setKeyConvention(String keyConvention) {
        this.keyConvention = keyConvention;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
