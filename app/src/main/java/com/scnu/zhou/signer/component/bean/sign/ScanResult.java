package com.scnu.zhou.signer.component.bean.sign;

import java.util.List;

/**
 * Created by zhou on 16/10/21.
 */
public class ScanResult {

    private Course course;
    private List<Signer> records;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Signer> getRecords() {
        return records;
    }

    public void setRecords(List<Signer> records) {
        this.records = records;
    }
}
