package com.tekfilo.sso.parameter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ParameterDto {
    private Integer id;
    private String parameterCode;
    private String parameterName;
    private String parameterGroup;
    private Integer sequence;
    private Integer isDeleted;
    private Integer isLocked;
}
