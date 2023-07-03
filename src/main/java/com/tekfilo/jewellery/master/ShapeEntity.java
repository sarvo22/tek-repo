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
@Table(name = "tbl_shape")
public class ShapeEntity {

    @Id
    @Column(name = "shape_id", updatable = false, insertable = false, nullable = false)
    private Integer id;

    @Column(name = "shape_name")
    private String shapeName;
}
