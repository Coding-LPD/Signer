package com.scnu.zhou.signer.activity.user.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.elss.cache.UserCache;
import com.scnu.zhou.signer.elss.config.SignerApi;
import com.scnu.zhou.signer.elss.bean.http.ResultResponse;
import com.scnu.zhou.signer.elss.bean.user.Student;
import com.scnu.zhou.signer.elss.util.http.RetrofitServer;
import com.scnu.zhou.signer.elss.util.image.FileUtils;
import com.scnu.zhou.signer.elss.util.image.ImagePicker;
import com.scnu.zhou.signer.elss.widget.image.CircleImageView;
import com.scnu.zhou.signer.elss.widget.toast.ToastView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 2016/9/10.
 */
public class EditHeaderActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    private File file;

    private String userid = "";
    private Uri headerUri;

    private Context context;

    @Bind(R.id.ll_info) LinearLayout ll_info;
    @Bind(R.id.ll_preview) LinearLayout ll_preview;
    @Bind(R.id.civ_preview_header) CircleImageView civ_preview_header;
    @Bind(R.id.tv_preview_name) TextView tv_preview_name;

    private int preview_pos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_header);

        ButterKnife.bind(this);
        context = this;
        initData();
        initView();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("更换头像");
    }

    @Override
    public void initData() {

        userid = getIntent().getStringExtra("userid");
    }

    @Override
    public void loadData() {

    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 选择默认头像1
    @OnClick(R.id.iv_header01)
    public void pickHeader01(){
        openPreview(1);
    }


    // 选择默认头像2
    @OnClick(R.id.iv_header02)
    public void pickHeader02(){
        openPreview(2);
    }


    // 选择默认头像3
    @OnClick(R.id.iv_header03)
    public void pickHeader03(){
        openPreview(3);
    }


    // 选择默认头像4
    @OnClick(R.id.iv_header04)
    public void pickHeader04(){
        openPreview(4);
    }


    // 从相册选择头像
    @OnClick(R.id.tc_picker)
    public void pickFromAblum(){
        ImagePicker.pickFromAblum(this);
    }


    // 取消预览
    @OnClick(R.id.tv_cancel)
    public void cancelPreview(){
        closePreview();
    }


    // 设置头像
    @OnClick(R.id.btn_set_header)
    public void setHeader(){
        uploadImage();
    }


    /**
     * 打开预览
     */
    private void openPreview(int pos){

        preview_pos = pos;

        ll_info.setVisibility(View.GONE);
        ll_preview.setVisibility(View.VISIBLE);
        tv_preview_name.setText(UserCache.getName(this));

        switch (pos){
            case 0:
                ImageLoader.getInstance().displayImage(headerUri.toString(), civ_preview_header);
                break;
            case 1:
                civ_preview_header.setImageResource(R.drawable.default_header_male);
                break;
            case 2:
                civ_preview_header.setImageResource(R.drawable.default_header_female);
                break;
            case 3:
                civ_preview_header.setImageResource(R.drawable.default_header_male02);
                break;
            case 4:
                civ_preview_header.setImageResource(R.drawable.default_header_female02);
                break;
        }
    }

    /**
     * 关闭预览
     */
    private void closePreview(){
        ll_info.setVisibility(View.VISIBLE);
        ll_preview.setVisibility(View.GONE);

    }


    /**
     * 上传图片
     */
    private void uploadImage(){

        switch (preview_pos){
            case 0:
                showLoadingDialog("提交中");
                file = new File(FileUtils.getFilePathFromUri(context, headerUri));
                uploadUserImage();
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                showLoadingDialog("提交中");
                getDefaultImageUrl();
                break;
            default:break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == ImagePicker.STATE_ABLUM){
            if (resultCode == RESULT_OK) {
                ImagePicker.cropPhoto(this, data.getData());// 裁剪图片
            }

        }
        else if (requestCode == ImagePicker.STATE_CROP){


            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap head = extras.getParcelable("data");
                if (head != null) {

                    //headerUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), head, null,null));
                    headerUri = ImagePicker.getPictureUri();
                    openPreview(0);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取用户默认头像url
     */
    private void getDefaultImageUrl(){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getDefaultImageUrl(preview_pos)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());

                        dismissLoadingDialog();
                        ToastView toastView = new ToastView(context, "设置头像失败");
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        dismissLoadingDialog();

                        if (response.getCode().equals("200")){

                            updateStudentAvatar(response.getData());
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

    /**
     * 更新学生头像
     */
    private void updateStudentAvatar(String avatar){

        Map<String, String> infos = new HashMap<>();
        infos.put("avatar", avatar);

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
                        ToastView toastView = new ToastView(context, "更新头像失败");
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


    /**
     * 上传用户头像
     */
    private void uploadUserImage(){

        Log.e("file", file.getName());

        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .uploadUserImage(requestFile)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());

                        dismissLoadingDialog();
                        ToastView toastView = new ToastView(context, "上传头像失败");
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        dismissLoadingDialog();

                        //Log.e("url", response.getData());
                        if (response.getCode().equals("200")){
                            updateStudentAvatar(response.getData());
                        }
                        else{
                            ToastView toastView = new ToastView(context, response.getMsg());
                            toastView.setGravity(Gravity.CENTER, 0, 0);
                            toastView.show();
                        }
                    }
                });
    }
}
