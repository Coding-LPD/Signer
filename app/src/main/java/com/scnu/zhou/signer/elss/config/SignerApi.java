package com.scnu.zhou.signer.elss.config;

import com.scnu.zhou.signer.elss.bean.http.ResultResponse;
import com.scnu.zhou.signer.elss.bean.user.Student;
import com.scnu.zhou.signer.elss.bean.user.User;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zhou on 2016/9/19.
 */
public interface SignerApi {

    @GET("/api/publickey")    // 获取公钥
    Observable<ResultResponse<String>> getPublicKey();



    @FormUrlEncoded
    @POST("/api/smsCode")    // 发送短信验证码
    Observable<ResultResponse<String>> sendSmsCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/api/smsCode/verification")    // 验证短信验证码
    Observable<ResultResponse<String>> verifySmsCode(@Field("phone") String phone, @Field("smsCode") String smsCode);



    @FormUrlEncoded
    @POST("/api/users")    // 用户注册
    Observable<ResultResponse<User>> regist(@Field("phone") String phone, @Field("password") String password, @Field("role") String role);

    @FormUrlEncoded
    @POST("/api/users/login")    // 用户登录
    Observable<ResultResponse<User>> login(@Field("phone") String phone, @Field("password") String password);



    @FormUrlEncoded
    @POST("/api/students/search")    // 根据手机获取学生用户信息
    Observable<ResultResponse<List<Student>>> getStudentInfoByPhone(@Field("phone") String phone);

    @FormUrlEncoded
    @PUT("/api/students/{userid}")    // 更新学生用户信息
    Observable<ResultResponse<Student>> updateStudentInfo(@Path("userid") String userid, @FieldMap Map<String,String> infos);



    @GET("/api/students/images/{pos}")    // 获取用户默认头像url
    Observable<ResultResponse<String>> getDefaultImageUrl(@Path("pos") int pos);

    @Multipart
    @POST("/api/students/images")    // 上传头像
    Observable<ResultResponse<String>> uploadUserImage(@Part("file\"; filename=\"image.png") RequestBody file);
}