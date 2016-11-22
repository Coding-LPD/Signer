package com.scnu.zhou.signer.callback.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.user.User;

import java.util.List;

/**
 * Created by zhou on 16/10/20.
 */
public interface UserInfoCallBack {

    void onGetPublicKeySuccess(ResultResponse<String> response);
    void onGetPublicKeyError(Throwable e);

    void onGetSuccess(ResultResponse<List<Student>> response);
    void onGetError(Throwable e);

    void onUpdateSuccess(ResultResponse<Student> response);
    void onUpdateError(Throwable e);

    void onGetImageSuccess(ResultResponse<String> response);
    void onGetImageError(Throwable e);

    void onUploadImageSuccess(ResultResponse<String> response);
    void onUploadImageError(Throwable e);

    void onUpdatePasswordSuccess(ResultResponse<User> response);
    void onUpdatePasswordError(Throwable e);
}
