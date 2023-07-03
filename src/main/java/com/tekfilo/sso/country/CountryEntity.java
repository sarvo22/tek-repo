package com.tekfilo.sso.country;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity
@Table(name = "tbl_country")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_generator")
    @SequenceGenerator(name = "country_generator", sequenceName = "tbl_country_seq", allocationSize = 1)
    @Column(name = "country_id", updatable = false, insertable = true, nullable = false)
    private Integer id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "mobile_code")
    private String mobileCode;

    @Column(name = "date_format")
    private String dateFormat;

    @Column(name = "ui_date_format")
    private String uiDateFormat;

    @Column(name = "default_currency")
    private String defaultCurrency;

    @Column(name = "is_deleted")
    private Integer isDeleted;

}
