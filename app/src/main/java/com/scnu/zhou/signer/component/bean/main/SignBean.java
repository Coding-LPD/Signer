package com.scnu.zhou.signer.component.bean.main;

/**
 * Created by zhou on 16/11/3.
 */
public class SignBean {

    private String signId;
    private String time;
    private boolean tag;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
