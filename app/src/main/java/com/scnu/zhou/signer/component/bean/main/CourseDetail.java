package com.scnu.zhou.signer.component.bean.main;

import com.scnu.zhou.signer.component.bean.sign.Course;
import com.scnu.zhou.signer.component.bean.sign.Signer;

import java.util.List;

/**
 * Created by zhou on 16/11/1.
 */
public class CourseDetail {

    private Course course;
    private int signNum;
    private List<Signer> records;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getSignNum() {
        return signNum;
    }

    public void setSignNum(int signNum) {
        this.signNum = signNum;
    }

    public List<Signer> getRecords() {
        return records;
    }

    public void setRecords(List<Signer> records) {
        this.records = records;
    }
}
