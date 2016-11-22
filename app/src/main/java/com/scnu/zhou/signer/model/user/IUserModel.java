package com.scnu.zhou.signer.model.user;

import com.scnu.zhou.signer.callback.user.UserInfoCallBack;

import java.io.File;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserModel {

    void getPublicKey(UserInfoCallBack callBack);

    void getStudentInfo(String phone, UserInfoCallBack callBack);   // 获取用户学生信息
    void getStudentActiveInfo(String studentId, UserInfoCallBack callBack); // 获取学生签到次数与发言次数

    void updateStudentInfo(String userid, String key, String value, UserInfoCallBack callBack);  // 更新用户学生信息

    void getDefaultImageUrl(int pos, UserInfoCallBack callBack);   // 获取默认头像url
    void uploadStudentAvatar(File file, UserInfoCallBack callBack);    // 上传头像

    void updateUserPassword(String phone, String password, UserInfoCallBack callBack);    // 更改密码
}
