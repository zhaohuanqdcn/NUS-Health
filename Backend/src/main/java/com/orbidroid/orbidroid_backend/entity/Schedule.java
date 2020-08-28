package com.orbidroid.orbidroid_backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "Schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id = 1;

    @Column(name="schedule_doc_num", nullable = false)
    private Integer docNum;

    @Column(name = "schedule_work_start", nullable = false)
    private String workStart;

    @Column(name = "schedule_work_end", nullable = false)
    private String workEnd;

    public Schedule() {

    }

    public Schedule(Integer docNum, String workStart, String workEnd) {
        this.docNum = docNum;
        this.workStart = workStart;
        this.workEnd = workEnd;
    }

    public Integer getNum() {
        return this.id;
    }

    public Integer getDocNum() {
        return this.docNum;
    }

    public String getWorkStart() {
        return this.workStart;
    }

    public String getWorkEnd() {
        return this.workEnd;
    }

    public void setDocNum(Integer docNum) {
        this.docNum = docNum;
    }

    public void setWorkStart(String workStart) {
        this.workStart = workStart;
    }

    public void setWorkEnd(String workEnd) {
        this.workEnd = workEnd;
    }

    @Override
    public String toString() {
        return String.format("{\"docNum\":%d, \"workStart\":\"%s\", \"workEnd\":\"%s\", \"num\":%d}",
                docNum, workStart, workEnd, id);
    }
}
