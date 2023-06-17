package org.itstep.studentservice.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @JsonProperty("count")
    @Column(length = 50, nullable = false, name = "first_name")
    @Size(min = 3, max = 50)
    private String firstName;
    @Column(length = 50, nullable = false, name = "last_name")
    @Size(min = 3, max = 50)
    private String lastName;
    @Column(name = "birthday")
    @Past
    private Date birthday;

    @Column(name = "phone")
//  +38 095 000 00 01
//  +3809500000 01
//  095 000 00 00
//  +38 095000 00 00
    @Pattern(regexp = "(\\+\\d{2})? *\\d{3} *\\d{3} *\\d{2} *\\d{2}$", message = "Wrong format of phone number")
    private String phone;
    @Column(name = "email")
//    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Wrong format of email")
    @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Wrong format of email")
    private String email;
}

