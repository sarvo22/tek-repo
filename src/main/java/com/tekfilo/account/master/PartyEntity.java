package com.tekfilo.account.master;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Table(name = "v_party")
public class PartyEntity implements Serializable {

    @Id
    @Column(name = "party_type_id")
    private String partyTypeId;

    @Column(name = "party_type")
    private String partyType;

    @Column(name = "party_id")
    private Integer partyId;

    @Column(name = "party_name")
    private String partyName;

}
