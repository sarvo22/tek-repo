package com.tekfilo.sso.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterClause {
    private String key;
    private String value;
    private String name;
    private String operator;
    private String type;
}
