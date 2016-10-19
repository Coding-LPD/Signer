package com.scnu.zhou.signer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.user.info.UserInfoActivity;
import com.scnu.zhou.signer.activity.user.settings.SettingActivity;
import com.scnu.zhou.signer.cache.UserCache;
import com.scnu.zhou.signer.config.SignerApi;
import com.scnu.zhou.signer.bean.http.ResultResponse;
import com.scnu.zhou.signer.bean.user.Student;
import com.scnu.zhou.signer.util.http.RetrofitServer;
import com.scnu.zhou.signer.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.widget.image.CircleImageView;
import com.scnu.zhou.signer.widget.toast.ToastView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/9/6.
 */
public class MineFragment extends BaseFragment{

    private Activity context;

    @Bind(R.id.tv_user_name) TextView tv_user_name;
    @Bind(R.id.tv_user_id) TextView tv_user_id;
    @Bind(R.id.civ_user_header) CircleImageView civ_user_header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mine, container,
                false);// 关联布局文件
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        initView();
        initData();
    }

    @Override
    public void initView() {
        tv_user_name.setText(UserCache.getName(context));
        tv_user_id.setText("ID:" + UserCache.getPhone(context));
        ImageLoaderUtil.getInstance().displayHeaderImage(civ_user_header, UserCache.getAvatar(context));
    }

    @Override
    public void initData() {

    }

    @Override
    public void loadData() {

        getStudentInfo();
        //new GetStudentInfoTask().execute();
    }


    // 查看个人信息
    @OnClick(R.id.rl_profile)
    public void checkProfile(){
        Intent intent = new Intent(context, UserInfoActivity.class);
        startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 进入设置
    @OnClick(R.id.tc_settings)
    public void setting(){
        Intent intent = new Intent(context, SettingActivity.class);
        startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    /**
     * 获取学生信息
     */
    private void getStudentInfo(){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getStudentInfoByPhone(UserCache.getPhone(context))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<List<Student>>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<List<Student>> response) {

                        if (response.getCode().equals("200")){

                            List<Student> list = response.getData();
                            tv_user_name.setText(list.get(0).getName());

                            String avatar = list.get(0).getAvatar();
                            if (!avatar.equals("")){
                                ImageLoaderUtil.getInstance().displayHeaderImage(civ_user_header, avatar);
                            }
                            else{
                                if (list.get(0).getGender().equals("女")){
                                    civ_user_header.setImageResource(R.drawable.default_header_female);
                                }
                                else{
                                    civ_user_header.setImageResource(R.drawable.default_header_male);
                                }
                            }


                            // 本地缓存
                            UserCache.setName(context, list.get(0).getName());
                            UserCache.setAvatar(context, list.get(0).getAvatar());
                        }
                        else{
                            String data = response.getMsg();
                            ToastView toastView = new ToastView(context, data);
                            toastView.setGravity(Gravity.CENTER, 0, 0);
                            toastView.show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              loadData();
            }
        }, 200);
    }
}
