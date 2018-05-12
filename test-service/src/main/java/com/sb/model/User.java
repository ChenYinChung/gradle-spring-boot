package com.sb.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="USER")
public class User implements Serializable {
    private static final long serialVersionUID = -5652649716039775051L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME",nullable = false,length = 30, unique = true)
    private String name;

    @Column(name = "AGE",nullable = false)
    private Integer age;

    public User (){

    }

    public User (String name, int age){
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}