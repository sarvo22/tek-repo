package com.tekfilo.admin.master;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_commodity_group")
public class CommodityGroupEntity {

    @Id
    @Column(name = "commodity_group_id", insertable = false, updatable = false)
    private Integer id;


    @Column(name = "commodity_group_name", insertable = false, updatable = false)
    private String groupName;


    @Column(name = "commodity_group_type", insertable = false, updatable = false)
    private String groupType;

}
