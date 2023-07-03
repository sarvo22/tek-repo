package com.tekfilo.jewellery.master;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_supplier")
public class SupplierEntity implements Serializable {

    @Id
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;
}
