package com.scnu.zhou.signer.ui.activity.user.info;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.user.IUserPresenter;
import com.scnu.zhou.signer.presenter.user.UserPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.edit.WhiteClearableEditText;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.user.IUserUpdateView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/9/7.
 */
public class EditInfoActivity extends BaseSlideActivity implements IUserUpdateView{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_right) TextView tv_right;

    private String userid, title, text;

    @Bind(R.id.et_info) WhiteClearableEditText et_info;

    private Context context;

    private IUserPresenter userPresenter;

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

        userPresenter = new UserPresenter(this);
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 保存动作
    @OnClick(R.id.tv_right)
    public void saveInfo(){

        if (!TextUtils.isEmpty(et_info.getText().toString())) {

            showLoadingDialog("提交中");
            userPresenter.updateStudentInfo(userid, getVariableName(title), et_info.getText());
        }
        else{

            ToastView toastView = new ToastView(this, "信息不能为空哦");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
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


    // 更新信息成功
    @Override
    public void onUpdateStudentInfoSuccess(ResultResponse<Student> response) {

        dismissLoadingDialog();

        if (response.getCode().equals("200")){

            if (getVariableName(title).equals("number")){   // 是学号就要保存
                UserCache.getInstance().setNumber(this, et_info.getText().toString());
            }

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


    // 更新信息失败
    @Override
    public void onUpdateStudentInfoError(Throwable e) {

        Log.e("update info error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(context, "修改失败");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }
}
