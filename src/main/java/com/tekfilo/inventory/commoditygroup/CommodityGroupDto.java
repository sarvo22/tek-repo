package com.tekfilo.inventory.commoditygroup;

import com.tekfilo.inventory.base.BaseDto;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CommodityGroupDto extends BaseDto implements Serializable {
    private Integer id;
    private String groupName;
    private String description;
    private String groupType;
}
