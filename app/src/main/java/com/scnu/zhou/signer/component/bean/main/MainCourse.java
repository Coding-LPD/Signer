package com.scnu.zhou.signer.component.bean.main;

import java.util.List;

/**
 * Created by zhou on 16/10/22.
 */
public class MainCourse {

    private String name;
    private int number;
    private List<String> avatars;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<String> avatars) {
        this.avatars = avatars;
    }
}
