package com.scnu.zhou.signer.presenter.user;

import java.io.File;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserPresenter {

    void getPublicKey();

    void getStudentInfo(String phone);   // 获取用户学生信息
    void getActiveInfo(String studentId);    // 获取学生签到次数与发言次数

    void updateStudentInfo(String userid, String key, String value);  // 更新用户学生信息

    void getDefaultImageUrl(int pos);   // 获取默认头像url
    void uploadStudentAvatar(File file);    // 上传头像

    void updateUserPassword(String phone, String password);   // 更改密码
}
