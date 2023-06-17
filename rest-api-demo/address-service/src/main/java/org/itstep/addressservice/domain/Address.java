package org.itstep.addressservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //@JsonProperty("count")
    private String country;
    private String city;
    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_line2")
    private String addressLine2;
}
