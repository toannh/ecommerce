/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.chodientu.entity.form;

import org.hibernate.validator.constraints.NotBlank;

public class ParameterKeyForm {
    @NotBlank(message = "Phải nhập key quy ước")
    private String keyConvention;
    private String key;
    @NotBlank(message = "Phải nhập value")
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
