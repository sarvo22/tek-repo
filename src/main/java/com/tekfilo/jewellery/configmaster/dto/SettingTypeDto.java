package com.tekfilo.jewellery.configmaster.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SettingTypeDto {

    private Integer id;
    private String settingTypeName;
    private Integer sequence;
    private Integer isLocked;
    private Integer isDeleted;
}
