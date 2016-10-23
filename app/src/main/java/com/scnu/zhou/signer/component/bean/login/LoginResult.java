package com.scnu.zhou.signer.component.bean.login;

import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.user.User;

/**
 * Created by zhou on 16/10/22.
 */
public class LoginResult {

    private User user;
    private Student person;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Student getPerson() {
        return person;
    }

    public void setPerson(Student person) {
        this.person = person;
    }
}
