package com.tekfilo.sso.country;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class DropDownDto {
    private String value;
    private String label;

    public DropDownDto(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
