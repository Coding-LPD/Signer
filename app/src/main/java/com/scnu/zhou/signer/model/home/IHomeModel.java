package com.scnu.zhou.signer.model.home;

import com.scnu.zhou.signer.callback.home.HomeCallBack;

/**
 * Created by zhou on 16/10/23.
 */
public interface IHomeModel {

    void getRelatedCourses(String phone, int limit, int page, String keyword, HomeCallBack callBack);
}
