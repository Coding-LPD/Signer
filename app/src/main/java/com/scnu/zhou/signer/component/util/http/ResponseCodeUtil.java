package com.scnu.zhou.signer.component.util.http;

/**
 * Created by zhou on 16/9/4.
 */
public class ResponseCodeUtil {

    public static String getMessage(String code){

        if (code.equals("1000")){
            return "手机号码不能为空";
        }
        else if (code.equals("1001")){
            return "密码不能为空";
        }
        else if (code.equals("1002")){
            return "角色不能为空";
        }
        else if (code.equals("1003")){
            return "用户角色的值只能为\"0\"或\"1\"";
        }
        else if (code.equals("1004")){
            return "手机号码无效";
        }
        else if (code.equals("1005")){
            return "密码解密失败";
        }
        else if (code.equals("1006")){
            return "学号不能为空";
        }
        else if (code.equals("1007")){
            return "课程名称不能为空";
        }
        else if (code.equals("1008")){
            return "查询条件不能为空";
        }
        else if (code.equals("2000")){
            return "手机号码或密码不能为空";
        }
        else if (code.equals("2001")){
            return "手机号码或密码错误";
        }
        else if (code.equals("2002")){
            return "该手机号码已被注册";
        }
        else if (code.equals("2003")){
            return "用户不存在";
        }
        else if (code.equals("3000")){
            return "验证码错误或者已被验证";
        }
        else if (code.equals("3001")){
            return "该验证码尚未被验证";
        }
        else if (code.equals("3002")){
            return "该号码已达到短信验证码发送上限";
        }
        else if (code.equals("3004")){
            return "无效的smsid";
        }
        else if (code.equals("4000")){
            return "未找到相应签到信息";
        }
        else if (code.equals("401")){
            return "认证信息有误";
        }
        else if (code.equals("402")){
            return "认证信息已失效";
        }
        else if (code.equals("403")){
            return "无权限访问该资源";
        }
        else if (code.equals("500")){
            return "内部服务器错误";
        }
        else if (code.equals("200")){
            return "操作成功";
        }
        else{
            return "";
        }
    }
}
