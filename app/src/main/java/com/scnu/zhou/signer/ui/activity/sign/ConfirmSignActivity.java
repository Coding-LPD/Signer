package com.scnu.zhou.signer.ui.activity.sign;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.gridview.SignerAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;
import com.scnu.zhou.signer.component.bean.sign.Signer;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.location.BaiduLocationClient;
import com.scnu.zhou.signer.component.util.http.ResponseCodeUtil;
import com.scnu.zhou.signer.presenter.sign.ISignPresenter;
import com.scnu.zhou.signer.presenter.sign.SignPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
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
     * @param response
     */
    @Override
    public void onGetScanResultSuccess(ResultResponse<ScanResult> response) {

        dismissLoadingDialog();
        if (response.getCode().equals("200")) {

            ScanResult result = response.getData();

            signId = result.getSignId();

            if (result != null) {

                tv_title.setText(result.getCourse().getName());
                tv_name.setText(result.getCourse().getName());
                tv_location.setText(result.getCourse().getLocation());
                tv_teacher.setText(result.getCourse().getTeacherName());

                //Log.e("data", result.getCourse().getTime());
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

                tv_week.setText(week);
                tv_session.setText(session);

                signers = result.getRecords();
                adapter = new SignerAdapter(this, signers);
                gv_signer.setAdapter(adapter);
            }
        }
        else{

            ToastView toastView = new ToastView(ConfirmSignActivity.this,
                    ResponseCodeUtil.getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            if (response.getCode().equals("4000")){
                finish();
            }
        }
    }


    /**
     * 获取扫描结果失败
     * @param e
     */
    @Override
    public void onGetScanResultError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    /**
     * 签到成功
     * @param response
     */
    @Override
    public void onPostSignSuccess(ResultResponse<SignRecord> response) {

        dismissLoadingDialog();

        Intent intent = new Intent(this, SignSuccessActivity.class);
        intent.putExtra("distance", response.getData().getDistance());
        intent.putExtra("battery", response.getData().getBattery());
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

        finish();

        /*
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "签到成功");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();*/
    }


    /**
     * 签到失败
     * @param e
     */
    @Override
    public void onPostSignError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "签到失败");
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

        type = 0;
        showLoadingDialog("发送请求中");
        getPhoneBattery();

    }

    // 课后签到
    @OnClick(R.id.btn_sign_after)
    public void signAfter(){

        type = 1;
        showLoadingDialog("发送请求中");
        getPhoneBattery();
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
}
