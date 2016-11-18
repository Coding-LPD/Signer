package com.scnu.zhou.signer.component.bean.chat;

/**
 * Created by zhou on 16/11/18.
 */
public class Emotion {

    private String code = null;

    private String name = null;

    public Emotion() {}

    public Emotion(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}