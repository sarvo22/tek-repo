package com.tekfilo.admin.dropdown;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DropDownNumberDto {
    private Integer value;
    private String label;

    public DropDownNumberDto(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
