package com.scnu.zhou.signer.component.config;

import com.scnu.zhou.signer.component.bean.feedback.Feedback;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.bean.main.SignBean;
import com.scnu.zhou.signer.component.bean.notice.NoticeBean;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.SignRecord;
import com.scnu.zhou.signer.component.bean.user.Student;
import com.scnu.zhou.signer.component.bean.user.User;

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
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zhou on 2016/9/19.
 */
public interface SignerApi {

    @GET("/api/publickey")    // 获取公钥
    Observable<ResultResponse<String>> getPublicKey();


    /**
     * Login and Regist
     */
    @FormUrlEncoded
    @POST("/api/smsCode")    // 发送短信验证码
    Observable<ResultResponse<String>> sendSmsCode(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("/api/smsCode/verification")    // 验证短信验证码
    Observable<ResultResponse<String>> verifySmsCode(@Field("phone") String phone, @Field("smsCode") String smsCode);



    @FormUrlEncoded
    @POST("/api/users")    // 用户注册
    Observable<ResultResponse<LoginResult>> regist(@Field("phone") String phone, @Field("password") String password, @Field("role") String role);

    @FormUrlEncoded
    @POST("/api/users/login")    // 用户登录
    Observable<ResultResponse<LoginResult>> login(@Field("phone") String phone, @Field("password") String password);


    /**
     * User Info Get and Update
     */
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

    @FormUrlEncoded
    @PUT("/api/users/{phone}")    // 更新用户密码
    Observable<ResultResponse<User>> updateUserPassword(@Path("phone") String phone, @FieldMap Map<String,String> infos);


    /**
     * Scan and Sign
     */
    @GET("/api/signs/scanning/{code}")    // 根据签到码获取课程和签到信息
    Observable<ResultResponse<ScanResult>> getScanResult(@Path("code") String code);

    @FormUrlEncoded
    @POST("/api/signRecords")    // 签到动作
    Observable<ResultResponse<SignRecord>> sign(@FieldMap Map<String,String> strinfos, @FieldMap Map<String,Integer> numinfos,
                                                @FieldMap Map<String,Double> doubleinfos);


    /**
     * Home Page
     */
    @GET("/api/students/{phone}/relatedCourses")     // 获取最近签到相关课程信息
    Observable<ResultResponse<List<MainCourse>>> getRelatedCourses(@Path("phone") String phone, @Query("limit") int limit,
                                                             @Query("page") int page, @Query("keyword") String keyword);

    @GET("/api/courses/{id}/latestSignRecords")     // 获取相关课程信息详情
    Observable<ResultResponse<CourseDetail>> getCourseDetail(@Path("id") String courseId);

    @GET("/api/courses/{id}/students/{studentId}/signRecords")   // 获取相关签到记录
    Observable<ResultResponse<List<SignBean>>> getSignDetail(@Path("id") String courseId, @Path("studentId") String studentId);


    /**
     * Notice Page
     */
    @GET("/api/students/{phone}/notice")     // 获取最近通知
    Observable<ResultResponse<List<NoticeBean>>> getNotices(@Path("phone") String phone, @Query("type") int type,
                                                            @Query("limit") int limit, @Query("page") int page);


    /**
     * Send Feedback
     */
    @FormUrlEncoded
    @POST("/api/feedbacks")    // 发送反馈
    Observable<ResultResponse<Feedback>> sendFeedback(@FieldMap Map<String,String> infos);
}