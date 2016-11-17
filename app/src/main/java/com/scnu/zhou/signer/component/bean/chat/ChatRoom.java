package com.scnu.zhou.signer.component.bean.chat;

/**
 * Created by zhou on 16/11/17.
 */
public class ChatRoom {

    private String courseId;
    private String name;
    private String avatar;
    private int count;
    private ChatMessage msg;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ChatMessage getMsg() {
        return msg;
    }

    public void setMsg(ChatMessage msg) {
        this.msg = msg;
    }
}
