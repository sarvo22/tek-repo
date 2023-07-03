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
@Table(name = "tbl_process")
public class ProcessEntity {

    @Id
    @Column(name = "process_id")
    private Integer processId;

    @Column(name = "process_name")
    private String processName;
}
