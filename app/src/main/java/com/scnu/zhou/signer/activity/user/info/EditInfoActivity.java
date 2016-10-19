package com.scnu.zhou.signer.activity.user.info;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.elss.config.SignerApi;
import com.scnu.zhou.signer.elss.bean.http.ResultResponse;
import com.scnu.zhou.signer.elss.bean.user.Student;
import com.scnu.zhou.signer.elss.util.http.RetrofitServer;
import com.scnu.zhou.signer.elss.widget.dialog.LoadingDialog;
import com.scnu.zhou.signer.elss.widget.edit.WhiteClearableEditText;
import com.scnu.zhou.signer.elss.widget.toast.ToastView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/9/7.
 */
public class EditInfoActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_right) TextView tv_right;

    private String userid, title, text;

    @Bind(R.id.et_info) WhiteClearableEditText et_info;

    private Context context;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_info);

        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText(title);

        tv_right.setText("保存");

        et_info = (WhiteClearableEditText) findViewById(R.id.et_info);
        et_info.setText(text);
        et_info.setHint("请输入" + title);
    }

    @Override
    public void initData() {

        userid = getIntent().getStringExtra("userid");
        title = getIntent().getStringExtra("title");
        text = getIntent().getStringExtra("text");

        dialog = new LoadingDialog(context);
        dialog.setTitle("提交中");
    }

    @Override
    public void loadData() {

    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 保存动作
    @OnClick(R.id.tv_right)
    public void saveInfo(){
        dialog.show();
        updateStudentInfo(getVariableName(title), et_info.getText());
    }


    /**
     * 根据中文描述获取对应变量名
     */
    private String getVariableName(String title){

        switch (title){
            case "学号": return "number";
            case "姓名": return "name";
            case "学校": return "school";
            case "学院": return "academy";
            case "专业": return "major";
            case "邮箱": return "mail";
            default: return "";
        }
    }


    /**
     * 更新学生信息
     */
    private void updateStudentInfo(String key, String value){

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

                        Log.e("error", e.toString());

                        dismissLoadingDialog();
                        ToastView toastView = new ToastView(context, "修改失败");
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }

                    @Override
                    public void onNext(ResultResponse<Student> response) {

                        dismissLoadingDialog();

                        if (response.getCode().equals("200")){

                            ToastView toastView = new ToastView(context, "修改成功");
                            toastView.setGravity(Gravity.CENTER, 0, 0);
                            toastView.show();
                            finish();
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
}
