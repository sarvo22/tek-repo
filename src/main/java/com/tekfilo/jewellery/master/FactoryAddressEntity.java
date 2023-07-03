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
@Table(name = "tbl_factory_address")
public class FactoryAddressEntity {
    @Id
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "factory_id")
    private Integer factoryId;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "street")
    private String street;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "default_address")
    private Integer defaultAddress;


}
