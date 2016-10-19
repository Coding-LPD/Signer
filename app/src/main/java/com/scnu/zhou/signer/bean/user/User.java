package com.scnu.zhou.signer.bean.user;

/**
 * Created by zhou on 2016/9/19.
 */
public class User {

    private String _id;
    private String phone;
    private String password;
    private String role;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
