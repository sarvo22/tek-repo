package com.tekfilo.account.base;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class FilterClause {

    private String key;
    private String value;
    private String name;
    private String operator;
    private String type;
}
