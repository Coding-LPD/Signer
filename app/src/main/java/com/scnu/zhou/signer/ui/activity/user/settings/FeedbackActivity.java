package com.scnu.zhou.signer.ui.activity.user.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.feedback.FeedbackPresenter;
import com.scnu.zhou.signer.presenter.feedback.IFeedbackPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.dialog.AlertDialog;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.feedback.IFeedbackView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/12.
 */
public class FeedbackActivity extends BaseSlideActivity implements IFeedbackView{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_right) TextView tv_right;

    @Bind(R.id.et_feedback) EditText et_feedback;

    private IFeedbackPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("意见反馈");
        tv_right.setText("发送");
    }

    @Override
    public void initData() {

        presenter = new FeedbackPresenter(this);
    }

    @Override
    public void onFeedbackSuccess(ResultResponse<Feedback> response) {

        dismissLoadingDialog();

        if (response.getCode().equals("200")){

            final AlertDialog dialog = new AlertDialog(this);
            dialog.setTitle("反馈提示");
            dialog.setMessage("反馈成功, 非常感谢您的支持");
            dialog.setButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(FeedbackActivity.this, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onFeedbackError(Throwable e) {

        Log.e("send feedback error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(FeedbackActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 提交反馈
    @OnClick(R.id.tv_right)
    public void postFeedBack(){

        if (!TextUtils.isEmpty(et_feedback.getText().toString())) {

            showLoadingDialog("提交中");
            Feedback feedback = new Feedback();
            feedback.setStudentId(UserCache.getInstance().getId(this));
            feedback.setPhone(UserCache.getInstance().getPhone(this));
            feedback.setName(UserCache.getInstance().getName(this));
            feedback.setContent(et_feedback.getText().toString());

            presenter.sendFeedback(feedback);
        }
        else{
            ToastView toastView = new ToastView(this, "提交内容不能为空哦");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }
}
