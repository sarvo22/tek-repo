package com.tekfilo.account.costcategory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.sql.Timestamp;

@Data
@Getter
@Setter
public class CostCategoryDto {
    private Integer id;
    private String costCategoryName;
    private String costCategoryGroup;
    private String description;
    private Integer companyId;
    private Integer sequence;
    private Integer isLocked;


}
