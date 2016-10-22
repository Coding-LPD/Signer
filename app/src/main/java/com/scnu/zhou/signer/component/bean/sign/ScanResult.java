package com.scnu.zhou.signer.component.bean.sign;

import java.util.List;

/**
 * Created by zhou on 16/10/21.
 */
public class ScanResult {

    private Course course;
    private String signId;
    private List<Signer> records;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public List<Signer> getRecords() {
        return records;
    }

    public void setRecords(List<Signer> records) {
        this.records = records;
    }
}
