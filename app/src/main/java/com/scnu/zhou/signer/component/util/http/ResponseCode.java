package com.scnu.zhou.signer.component.util.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhou on 16/9/4.
 */
public class ResponseCode {

    private Map<String, String> codes;

    private ResponseCode(){

        codes = new HashMap<>();

        codes.put("1000", "手机号码不能为空");
        codes.put("1001", "密码不能为空");
        codes.put("1002", "角色不能为空");
        codes.put("1003", "用户角色的值只能为\"0\"或\"1\"");
        codes.put("1004", "手机号码无效");
        codes.put("1005", "密码解密失败");
        codes.put("1006", "学号不能为空");
        codes.put("1007", "课程名称不能为空");
        codes.put("1008", "查询条件不能为空");
        codes.put("1009", "签到类型的值只能为0、1、2");
        codes.put("1015", "服务器没接收到上传的文件");
        codes.put("1016", "不存在该默认图片");
        codes.put("1017", "签到记录的状态的值只能为0、1、2");
        codes.put("1018", "签到记录的类型的值只能为0、1");
        codes.put("2000", "手机号码或密码不能为空");
        codes.put("2001", "手机号码或密码错误");
        codes.put("2002", "该手机号码已被注册");
        codes.put("2003", "用户不存在");
        codes.put("3000", "验证码错误或者已被验证");
        codes.put("3001", "该验证码尚未被验证");
        codes.put("3002", "该号码已达到短信验证码发送上限");
        codes.put("3004", "无效的smsid");
        codes.put("4000", "签到失败，该签到不存在");
        codes.put("4001", "签到失败，该签到没有对应某个课程");
        codes.put("4002", "IP地址不能为空");
        codes.put("4003", "定位失败");
        codes.put("401", "认证信息有误");
        codes.put("402", "认证信息已失效");
        codes.put("403", "无权限访问该资源");
        codes.put("500", "内部服务器错误");
        codes.put("200", "操作成功");
    }

    private static class ResponseCodeHolder{

        private static final ResponseCode instance = new ResponseCode();
    }

    public static ResponseCode getInstance(){

        return ResponseCodeHolder.instance;
    }

    public String getMessage(String code){

        if (codes.get(code) != null){
            return codes.get(code);
        }
        else{
            return "";
        }
    }
}
