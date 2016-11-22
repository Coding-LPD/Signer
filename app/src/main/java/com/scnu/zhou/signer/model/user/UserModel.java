package com.scnu.zhou.signer.model.user;

import android.util.Log;

import com.scnu.zhou.signer.callback.user.UserInfoCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.user.User;
import com.scnu.zhou.signer.component.config.SignerApi;
import com.scnu.zhou.signer.component.util.http.RetrofitServer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/10/20.
 */
public class UserModel implements IUserModel {

    @Override
    public void getPublicKey(final UserInfoCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getPublicKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetPublicKeyError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callBack.onGetPublicKeySuccess(response);
                    }
                });
    }

    @Override
    public void getStudentInfo(String phone, final UserInfoCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getStudentInfoByPhone(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<Student>>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<List<Student>> response) {

                        callBack.onGetSuccess(response);
                    }
                });
    }


    @Override
    public void updateStudentInfo(String userid, String key, String value, final UserInfoCallBack callBack) {

        Map<String, String> infos = new HashMap<>();
        infos.put(key, value);

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .updateStudentInfo(userid, infos)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<Student>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onUpdateError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<Student> response) {

                        callBack.onUpdateSuccess(response);
                    }
                });
    }


    @Override
    public void getDefaultImageUrl(int pos, final UserInfoCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getDefaultImageUrl(pos)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onGetImageError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callBack.onGetImageSuccess(response);
                    }
                });
    }

    @Override
    public void uploadStudentAvatar(File file, final UserInfoCallBack callBack) {

        Log.e("file", file.getName());

        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .uploadUserImage(requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onUploadImageError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        callBack.onUploadImageSuccess(response);
                    }
                });
    }


    @Override
    public void updateUserPassword(String phone, final UserInfoCallBack callBack) {

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .updateUserPassword(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<User>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        callBack.onUpdatePasswordError(e);
                    }

                    @Override
                    public void onNext(ResultResponse<User> response) {

                        callBack.onUpdatePasswordSuccess(response);
                    }
                });
    }

}
