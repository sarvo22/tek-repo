package com.tekfilo.inventory.master;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Immutable
@Table(name = "tbl_payment_type")
public class PaymentTerm {

    @Id
    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "payment_type_name")
    private String paymentTypeName;
}
