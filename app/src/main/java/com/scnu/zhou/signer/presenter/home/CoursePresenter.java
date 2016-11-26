package com.scnu.zhou.signer.presenter.home;

import android.util.Log;

import com.scnu.zhou.signer.callback.home.CourseCallBack;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.main.SignBean;
import com.scnu.zhou.signer.model.home.CourseModel;
import com.scnu.zhou.signer.model.home.ICourseModel;
import com.scnu.zhou.signer.view.home.ICheckSignView;
import com.scnu.zhou.signer.view.home.ICourseView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 16/10/31.
 */
public class CoursePresenter implements ICoursePresenter, CourseCallBack {

    private ICourseView courseView;
    private ICheckSignView checkSignView;
    private ICourseModel courseModel;

    public CoursePresenter(ICourseView courseView){

        this.courseView = courseView;
        this.courseModel = new CourseModel();
    }

    public CoursePresenter(ICheckSignView checkSignView){

        this.checkSignView = checkSignView;
        this.courseModel = new CourseModel();
    }

    @Override
    public void getCourseDetail(String courseId) {

        courseModel.getCourseDetail(courseId, this);
    }

    @Override
    public void getSignDetail(String courseId, String studentId) {

        courseModel.getSignDetail(courseId, studentId, this);
    }


    /**
     * implementation for CourseCallBack
     * @param response
     */
    @Override
    public void onGetCourseDetailSuccess(ResultResponse<CourseDetail> response) {

        if (response.getCode().equals("200")) {
            CourseDetail result = response.getData();
            if (result != null) {

                //String time = "星期一 1节-3节,星期四 5节-8节";
                String time = result.getCourse().getTime();
                String[] sessions = time.split(",");   // 按逗号分开

                String week = "";
                String session = "";
                for (String se : sessions) {
                    String[] s = se.split("\\s+");    // 按空格分开
                    if (week.equals("")) week += s[0];
                    else week += ";" + s[0];
                    if (session.equals("")) session += s[1];
                    else session += ";" + s[1];
                }

                courseView.onGetCourseDetailSuccess(response.getData(), week, session);
            }
        }
        else{
            courseView.onGetCourseDetailError(response.getMsg());
        }
    }

    @Override
    public void onGetCourseDetailError(Throwable e) {

        courseView.onGetCourseDetailError(e);
    }

    @Override
    public void onGetSignDetailSuccess(ResultResponse<List<SignBean>> response) {

        if (response.getCode().equals("200")) {

            Map<String, Boolean> note01 = new HashMap<>();
            Map<String, Boolean> note02 = new HashMap<>();
            List<SignBean> result = response.getData();
            //Log.e("result", result.size() + "");
            for (SignBean bean: result){

                if (bean.isTag()){
                    Log.e("note01", bean.getTime());
                    note01.put(bean.getTime(), true);
                }
                else{
                    Log.e("note02", bean.getTime());
                    note02.put(bean.getTime(), true);
                }
            }
            checkSignView.onGetSignDetailSuccess(note01, note02);
        }
        else{
            checkSignView.onGetSignDetailError(response.getMsg());
        }
    }

    @Override
    public void onGetSignDetailError(Throwable e) {

        checkSignView.onGetSignDetailError(e);
    }
}
