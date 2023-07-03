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
@Table(name = "tbl_color")
public class ColorEntity {

    @Id
    @Column(name = "color_id", updatable = false, insertable = false, nullable = false)
    private Integer id;

    @Column(name = "color_name")
    private String colorName;
}
