package com.scnu.zhou.signer.view.user;

import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;

/**
 * Created by zhou on 16/10/20.
 */
public interface IUserHeaderView {

    void initView();
    void initData();

    void onGetDefaultImageSuccess(ResultResponse<String> response);
    void onGetDefaultImageError(Throwable e);

    void onUpdateAvatarSuccess(ResultResponse<Student> response);
    void onUpdateAvatarError(Throwable e);

    void onUploadAvatarSuccess(ResultResponse<String> response);
    void onUploadAvatarError(Throwable e);
}
