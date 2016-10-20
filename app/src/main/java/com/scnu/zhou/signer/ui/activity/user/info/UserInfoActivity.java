package com.scnu.zhou.signer.ui.activity.user.info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.UserInfoCellAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.view.CellBean;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.presenter.user.IUserPresenter;
import com.scnu.zhou.signer.presenter.user.UserPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.image.CircleImageView;
import com.scnu.zhou.signer.ui.widget.picker.MenuPicker;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.user.IUserInfoView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by zhou on 16/9/6.
 */
public class UserInfoActivity extends BaseSlideActivity implements IUserInfoView{

    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.lv_cell) ListView lv_cell;
    private UserInfoCellAdapter adapter;
    private List<CellBean> cellBeen;
    private String[] cellTitles = {"学号","姓名","性别","学校","学院","专业","年级","班级","邮箱"};
    private String[] cellTexts;


    @Bind(R.id.civ_user_header) CircleImageView civ_user_header;
    @Bind(R.id.tv_user_name) TextView tv_user_name;

    private Context context;
    private String userid = "";
    private String avatar;

    @Bind(R.id.iv_loading) ImageView iv_loading;

    private IUserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userinfo);

        ButterKnife.bind(this);
        context = this;
        initView();
        initData();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);

        ImageLoaderUtil.getInstance().displayHeaderImage(civ_user_header, UserCache.getInstance().getAvatar(context));
        tv_user_name.setText(UserCache.getInstance().getName(context));

        RotateAnimation mAnim = new RotateAnimation(0, 360, Animation.RESTART, 0.5f,
                Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);

        iv_loading.startAnimation(mAnim);
    }

    private void initCell(){

        cellBeen = new ArrayList<>();

        for (int i=0; i<cellTitles.length; i++){

            CellBean model = new CellBean();
            model.setTitle(cellTitles[i]);
            model.setText(cellTexts[i]);
            cellBeen.add(model);
        }

        adapter = new UserInfoCellAdapter(this, cellBeen);
        lv_cell.setAdapter(adapter);
    }

    @Override
    public void initData() {

        userPresenter = new UserPresenter(this);
    }

    @Override
    public void loadData() {

        userPresenter.getStudentInfo(UserCache.getInstance().getPhone(this));
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 点击用户头像
    @OnClick(R.id.civ_user_header)
    public void clickUserHeader(){
        Intent intent = new Intent(context, EditHeaderActivity.class);
        intent.putExtra("userid", userid);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // ItemClick监听
    @OnItemClick(R.id.lv_cell)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (cellBeen.get(position).getTitle().equals("性别")) {

            List<String> genders = new ArrayList<>();
            genders.add("男");
            genders.add("女");
            genders.add("保密");
            new GenderPicker().show(this, genders);
        }
        else if (cellBeen.get(position).getTitle().equals("年级")) {

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            List<String> grades = new ArrayList<>();
            grades.add(year + "级");
            grades.add(year - 1 + "级");
            grades.add(year - 2 + "级");
            grades.add(year - 3 + "级");
            new GradePicker().show(this, grades);
        }
        else if (cellBeen.get(position).getTitle().equals("班级")) {

            List<String> classes = new ArrayList<>();
            classes.add("1班");
            classes.add("2班");
            classes.add("3班");
            classes.add("4班");
            classes.add("5班");
            classes.add("6班");
            classes.add("7班");
            classes.add("8班");
            new ClassPicker().show(this, classes);
        }
        else {
            Intent intent = new Intent(this, EditInfoActivity.class);
            intent.putExtra("userid", userid);
            intent.putExtra("title", cellBeen.get(position).getTitle());
            intent.putExtra("text", cellBeen.get(position).getText());
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }

    /**
     * 性别选择picker
     */
    private class GenderPicker extends MenuPicker{

        @Override
        public void execute() {

            this.dismiss();

            showLoadingDialog("提交中");
            userPresenter.updateStudentInfo(userid, "gender", this.getSelect());
        }
    }

    /**
     * 年级选择picker
     */
    private class GradePicker extends MenuPicker{

        @Override
        public void execute() {

            this.dismiss();

            showLoadingDialog("提交中");
            userPresenter.updateStudentInfo(userid, "grade", this.getSelect());
        }
    }

    /**
     * 班级选择picker
     */
    private class ClassPicker extends MenuPicker{

        @Override
        public void execute() {

            this.dismiss();

            showLoadingDialog("提交中");
            userPresenter.updateStudentInfo(userid, "_class", this.getSelect());
        }
    }


    // 获取用户学生信息成功
    @Override
    public void onGetStudentInfoSuccess(ResultResponse<List<Student>> response) {

        if (response.getCode().equals("200")){

            userid = response.getData().get(0).get_id();

            tv_user_name.setText(response.getData().get(0).getName());

            avatar = response.getData().get(0).getAvatar();
            if (!avatar.equals("")){
                ImageLoaderUtil.getInstance().displayHeaderImage(civ_user_header, avatar);
            }
            else{
                if (response.getData().get(0).getGender().equals("女")){
                    civ_user_header.setImageResource(R.drawable.default_header_female);
                }
                else{
                    civ_user_header.setImageResource(R.drawable.default_header_male);
                }
            }

            cellTexts = new String[9];
            cellTexts[0] = response.getData().get(0).getNumber();
            cellTexts[1] = response.getData().get(0).getName();
            cellTexts[2] = response.getData().get(0).getGender();
            cellTexts[3] = response.getData().get(0).getSchool();
            cellTexts[4] = response.getData().get(0).getAcademy();
            cellTexts[5] = response.getData().get(0).getMajor();
            cellTexts[6] = response.getData().get(0).getGrade();
            cellTexts[7] = response.getData().get(0).get_class();
            cellTexts[8] = response.getData().get(0).getMail();

            iv_loading.setVisibility(View.GONE);

            initCell();
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(context, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 获取用户学生信息失败
    @Override
    public void onGetStudentInfoError(Throwable e) {

        Log.e("get info error", e.toString());

        ToastView toastView = new ToastView(UserInfoActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 更新用户学生信息成功
    @Override
    public void onUpdateStudentInfoSuccess(ResultResponse<Student> response) {

        dismissLoadingDialog();

        if (response.getCode().equals("200")){

            ToastView toastView = new ToastView(context, "修改成功");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            onResume();
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(context, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 更新用户学生信息失败
    @Override
    public void onUpdateStudentInfoError(Throwable e) {

        Log.e("update info error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(context, "修改失败");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }
}
