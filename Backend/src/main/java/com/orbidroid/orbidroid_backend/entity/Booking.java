package com.orbidroid.orbidroid_backend.entity;

import  javax.persistence.*;

@Entity
@Table(name = "Bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id = 1;

    @Column(name = "booking_time", nullable = false)
    private String time;

    @Column(name = "booking_duration", nullable = false)
    private int duration;

    @Column(name = "booking_type", nullable = false)
    private int type;

    @Column(name = "booking_doc_num", nullable = false)
    private Integer docNum;

    @Column(name = "booking_stu_num", nullable = false)
    private Integer stuNum;

    public Booking() {

    }

    public Booking(String time, int duration, int type, Integer docNum, Integer stuNum) {
        this.time = time;
        this.duration = duration;
        this.type = type;
        this.docNum = docNum;
        this.stuNum = stuNum;
    }

    public Integer getNum() {
        return this.id;
    }

    public String getTime() {
        return this.time;
    }

    public int getType() {
        return this.type;
    }

    public int getDuration() {
        return this.duration;
    }

    public Integer getDocNum() {
        return this.docNum;
    }

    public Integer getStuNum() {
        return this.stuNum;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDocNum(Integer docNum) {
        this.docNum = docNum;
    }

    public void setStuNum(Integer stuNum) {
        this.stuNum = stuNum;
    }

    @Override
    public String toString() {
        return String.format("{BookingNum=%d, BookingTime=%s, BookingDuration=%d, BookingType=%d, BookingStuNum=%s," +
                        "BookingDocNum=%s}",
                id, time, duration, type, stuNum, docNum);
    }
}
