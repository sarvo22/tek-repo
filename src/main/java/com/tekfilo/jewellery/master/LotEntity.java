package com.tekfilo.jewellery.master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "tbl_product")
public class LotEntity {

    @Id
    @Column(name = "product_id", updatable = false, insertable = false, nullable = false)
    private Integer id;

    @Column(name = "product_no")
    private String lotNo;
}
