package com.tekfilo.admin.base;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BaseAdvanceSearch {
    private String searchColumn;
    private String searchValue;
}
