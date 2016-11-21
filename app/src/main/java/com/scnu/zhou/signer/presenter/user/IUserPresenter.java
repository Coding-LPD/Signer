package com.scnu.zhou.signer.presenter.user;

import java.io.File;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserPresenter {

    void getPublicKey();

    void getStudentInfo(String phone);   // 获取用户学生信息
    void updateStudentInfo(String userid, String key, String value);  // 更新用户学生信息

    void getDefaultImageUrl(int pos);   // 获取默认头像url
    void uploadStudentAvatar(File file);    // 上传头像
}
