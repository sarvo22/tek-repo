package com.tekfilo.jewellery.master;

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
@Table(name = "tbl_factory")
public class FactoryEntity {

    @Id
    @Column(name = "factory_id", updatable = false, insertable = false, nullable = false)
    private Integer id;

    @Column(name = "factory_code")
    private String factoryCode;

    @Column(name = "factory_name")
    private String factoryName;

    @Column(name = "factory_alias_name")
    private String factoryAliasName;
}
