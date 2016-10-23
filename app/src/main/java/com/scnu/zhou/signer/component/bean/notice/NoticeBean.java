package com.scnu.zhou.signer.component.bean.notice;

/**
 * Created by zhou on 16/10/23.
 */
public class NoticeBean {

    private String courseName;
    private int signState;
    private int signDistance;
    private int signNumber;
    private String signAt;
    private String createdAt;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    public int getSignDistance() {
        return signDistance;
    }

    public void setSignDistance(int signDistance) {
        this.signDistance = signDistance;
    }

    public int getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(int signNumber) {
        this.signNumber = signNumber;
    }

    public String getSignAt() {
        return signAt;
    }

    public void setSignAt(String signAt) {
        this.signAt = signAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
