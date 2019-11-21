package com.example.export.modu.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Table(name="basics_user")
public class User {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="card")
    private String card;

    @Column(name="birthday")
    private String birthday;

    @Column(name="sex")
    private String sex;

    @Column(name="address")
    private String address;

    @Column(name="nation")
    private String nation;

    @Column(name="occupation")
    private String occupation;

    @Column(name="interest")
    private String interest;

    @Column(name="email")
    private String email;
}
