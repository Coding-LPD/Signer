package com.scnu.zhou.signer.presenter.home;

/**
 * Created by zhou on 16/10/23.
 */
public interface IHomePresenter {

    void getRelatedCourses(String phone, int limit, int page);

    void searchRelatedCourses(String phone, int limit, int page, String keyword);
}
