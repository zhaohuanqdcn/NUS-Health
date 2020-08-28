package com.orbidroid.orbidroid_backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "Students")
public class Student {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id = 1;

    @Column(name="stu_name", nullable = false)
    private String name;

    @Column(name = "stu_gender", nullable = false)
    private String gender;

    @Column(name = "stu_contact", unique = true, nullable = false)
    private String contact;

    @Column(name = "stu_email", unique = true, nullable = false)
    private String email;

    @Column(name = "stu_pwd", nullable = false)
    private String pwd;

    public Student() {

    }

    public Student (String name, String gender, String contact, String email, String pwd) {
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.email = email;
        this.pwd = pwd;
    }

    public Integer getNum() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public String getContact() {
        return this.contact;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPwd() {
        return this.pwd;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return String.format("{StuNum=%d, StuName=%s, StuGender=%s, StuContact=%s, StuEmail=%s}",
                id, name, gender, contact, email);
    }
}