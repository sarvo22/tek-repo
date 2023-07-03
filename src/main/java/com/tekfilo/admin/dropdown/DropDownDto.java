package com.tekfilo.admin.dropdown;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class DropDownDto implements Serializable {

    private String value;
    private String label;

    public DropDownDto(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
