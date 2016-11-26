package com.scnu.zhou.signer.ui.activity.sign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.gridview.SignerAdapter;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;
import com.scnu.zhou.signer.component.bean.sign.Signer;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.location.BaiduLocationClient;
import com.scnu.zhou.signer.component.util.permission.UserPermission;
import com.scnu.zhou.signer.presenter.sign.ISignPresenter;
import com.scnu.zhou.signer.presenter.sign.SignPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.user.info.UserInfoActivity;
import com.scnu.zhou.signer.ui.widget.dialog.AlertDialog;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.sign.ISignView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/22.
 */
public class ConfirmSignActivity extends BaseSlideActivity implements ISignView{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_week) TextView tv_week;
    @Bind(R.id.tv_session) TextView tv_session;
    @Bind(R.id.tv_location) TextView tv_location;
    @Bind(R.id.tv_teacher) TextView tv_teacher;

    @Bind(R.id.gv_signer) GridView gv_signer;

    private List<Signer> signers;
    private SignerAdapter adapter;

    private String code;
    private ISignPresenter signPresenter;

    private String signId;
    private int type = 0;
    private int battery = 0;

    private BaiduLocationClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_sign);

        ButterKnife.bind(this);
        initView();
        initData();

        client = new BaiduLocationClient(this);
    }


    @Override
    public void initView() {

        tv_title.setText("课程详情");
        ll_return.setVisibility(View.VISIBLE);

        showLoadingDialog("加载中");
    }


    @Override
    public void initData() {

        code = getIntent().getStringExtra("code");
        signers = new ArrayList<>();

        signPresenter = new SignPresenter(this);
        signPresenter.getScanResult(code);
    }


    /**
     * 获取扫描结果成功
     * @param result
     */
    @Override
    public void onGetScanResultSuccess(ScanResult result, String week, String session) {

        dismissLoadingDialog();

        signId = result.getSignId();

        tv_title.setText(result.getCourse().getName());
        tv_name.setText(result.getCourse().getName());
        tv_location.setText(result.getCourse().getLocation());
        tv_teacher.setText(result.getCourse().getTeacherName());

        tv_week.setText(week);
        tv_session.setText(session);

        signers = result.getRecords();
        adapter = new SignerAdapter(this, signers);
        gv_signer.setAdapter(adapter);
    }


    /**
     * 签到成功
     * @param response
     */
    @Override
    public void onPostSignSuccess(SignRecord response) {

        dismissLoadingDialog();

        Intent intent = new Intent(this, SignSuccessActivity.class);
        intent.putExtra("distance", response.getDistance());
        intent.putExtra("battery", response.getBattery());
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        finish();
    }


    /**
     * 显示错误信息
     * @param e
     */
    @Override
    public void onShowError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    /**
     * 无效签到码
     */
    @Override
    public void showUnavailCode() {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "签到码无效");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();

        finish();
    }


    @Override
    public void onShowError(String msg) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, msg);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }



    // 课前签到
    @OnClick(R.id.btn_sign_before)
    public void signBefore(){

        if (TextUtils.isEmpty(UserCache.getInstance().getNumber(this))) {   // 有学号信息
            showNoNumberDialog();
        }
        else if (!UserPermission.getInstance().isLocatePermitted(this) ||
                !UserPermission.getInstance().isPhoneStatePermitted(this)){
            showPermissionDialog();
        }
        else{
            type = 0;
            showLoadingDialog("发送请求中");
            getPhoneBattery();
        }

    }

    // 课后签到
    @OnClick(R.id.btn_sign_after)
    public void signAfter(){

        if (TextUtils.isEmpty(UserCache.getInstance().getNumber(this))) {   // 有学号信息
            showNoNumberDialog();
        }
        else if (!UserPermission.getInstance().isLocatePermitted(this) ||
                !UserPermission.getInstance().isPhoneStatePermitted(this)){
            showPermissionDialog();
        }
        else{
            type = 1;
            showLoadingDialog("发送请求中");
            getPhoneBattery();
        }
    }


    /**
     * 签到第一步： 注册广播获取手机电量
     */
    public void getPhoneBattery(){
        IntentFilter intentFilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //注册接收器以获取电量信息
        registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 检查手机电量
     */
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(this);
            int rawlevel = intent.getIntExtra("level", -1);//获得当前电量
            int scale = intent.getIntExtra("scale", -1);
            //获得总电量
            int level = -1;
            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;
            }
            battery = level;

            getPhoneLocation();
        }
    };


    /**
     * 签到第二步： 获取手机经纬度信息
     */
    public void getPhoneLocation(){

        client.setOnGetLocationListener(new BaiduLocationClient.OnGetLocationListener() {
            @Override
            public void onGetLocation(double latitude, double longitude) {
                sign(latitude, longitude);
            }
        });
        client.start();
    }


    /**
     * 第三步： 执行签到动作
     */
    private void sign(double latitude, double longitude){

        Map<String,String> strinfos = new HashMap<>();
        Map<String,Integer> numinfos = new HashMap<>();
        Map<String,Double> doubleinfos = new HashMap<>();

        strinfos.put("signId", signId);
        strinfos.put("phoneId", ((TelephonyManager)getSystemService(TELEPHONY_SERVICE)).getDeviceId());
        strinfos.put("studentId", UserCache.getInstance().getId(this));

        numinfos.put("type", type);
        numinfos.put("battery", battery);

        doubleinfos.put("latitude", latitude);
        doubleinfos.put("longitude", longitude);

        signPresenter.postSign(strinfos, numinfos, doubleinfos);
    }


    /**
     * 没有学号
     */
    @Override
    public void showNoNumberDialog(){

        dismissLoadingDialog();

        final AlertDialog dialog = new AlertDialog(this);
        dialog.setTitle("友情提示");
        dialog.setMessage("请先完善您的学生信息\n再进行签到");
        dialog.setNegativeButton("知道了", AlertDialog.BUTTON_LEFT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("现在就去", AlertDialog.BUTTON_RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfirmSignActivity.this, UserInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        //dialog.show();
    }


    /**
     * 不属于该课程学生
     */
    @Override
    public void showNoAdmittedDialog(){

        dismissLoadingDialog();

        final AlertDialog dialog = new AlertDialog(this);
        dialog.setTitle("签到失败");
        dialog.setMessage("很抱歉，您不是该课程的学生");
        dialog.setButton("知道了", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //dialog.show();
    }


    /**
     * 无权限
     */
    public void showPermissionDialog(){

        final AlertDialog dialog = new AlertDialog(this);
        dialog.setTitle("友情提示");
        dialog.setMessage("请先设置App的应用权限");
        dialog.setNegativeButton("知道了", AlertDialog.BUTTON_LEFT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("现在就去", AlertDialog.BUTTON_RIGHT, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        //dialog.show();
    }
}
