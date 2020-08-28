package com.orbidroid.orbidroid_backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "Doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id = 1;

    @Column(name="doc_name", nullable = false)
    private String name;

    @Column(name = "doc_gender", nullable = false)
    private String gender;

    @Column(name = "doc_contact", unique = true, nullable = false)
    private String contact;

    @Column(name = "doc_pos", nullable = false)
    private String pos;

    @Column(name = "doc_email", unique = true, nullable = false)
    private String email;

    @Column(name = "doc_pwd")
    private String pwd;

    public Doctor() {

    }

    public Doctor (String name, String gender, String contact, String pos, String email, String pwd) {
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.pos = pos;
        this.email = email;
        this.pwd = pwd;
    }

    public Doctor (String name, String gender, String contact, String pos, String email) {
        this.name = name;
        this.gender = gender;
        this.contact = contact;
        this.pos = pos;
        this.email = email;
        this.pwd = "";
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

    public String getPos() {
        return this.pos;
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

    public void setPos(String pos) {
        this.pos = pos;
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
        return String.format("{\"num\":%s, \"name\"=%s, \"gender\"=%s, \"contact\"=%s, \"pos\"=%s, \"email\"=%s}",
                id, name, gender, contact, pos, email);
    }
}