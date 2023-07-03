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
@Table(name = "tbl_cut")
public class CutEntity {

    @Id
    @Column(name = "cut_id", updatable = false, insertable = false, nullable = false)
    private Integer id;

    @Column(name = "cut_name")
    private String cutName;
}
