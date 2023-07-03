package com.tekfilo.sps.company;

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
@Table(name = "tbl_company")
public class CompanyEntity {

    @Id
    @Column(name = "company_id")
    private Integer id;

    @Column(name = "default_currency")
    private String baseCurrency;
}
