package com.tekfilo.sso.state;

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
@Table(name = "tbl_state")
public class StateEntity {

    @Id
    @Column(name = "state_code")
    private String stateCode;
    @Column(name = "state_name")
    private String stateName;
    @Column(name = "sort_seq")
    private Integer sequence;
    @Column(name = "country_id")
    private Integer countryId;
    @Column(name = "is_deleted")
    private Integer isDeleted;

}
