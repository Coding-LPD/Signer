package com.scnu.zhou.signer.component.bean.login;

import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/22.
 */
public class LoginResult {

    private String id;
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
