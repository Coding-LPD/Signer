//
//  SignUpService.swift
//  Signer
//
//  Created by Vernon on 2016/11/7.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire
import SwiftyJSON

class SignUpService
{
    typealias successClosure = (_ json: JSON) -> ()
    typealias failureClosure = (_ error: Error) -> ()
    
    /// 请求公钥
    private class func requestPublicKey(successHandler: successClosure?, failureHandler: failureClosure?)
    {
        Alamofire.request("http://linkdust.xicp.net:50843/api/publickey", method: .get).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                successHandler?(JSON(value))
            case .failure(let error):
                failureHandler?(error)
            }
        }
    }
    
    /// 请求发送短信验证码到指定手机号
    class func requestVerifyCode(parameter: Parameters?, successHandler: successClosure?, failureHandler: failureClosure?)
    {
        Alamofire.request("http://linkdust.xicp.net:50843/api/smsCode", method: .post, parameters: parameter).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                successHandler?(JSON(value))
            case .failure(let error):
                failureHandler?(error)
            }
        }
    }
   
    /// 验证手机号和验证码是否匹配
    class func validateCode(parameter: Parameters?, successHandler: successClosure?, failureHandler: failureClosure?)
    {
        Alamofire.request("http://linkdust.xicp.net:50843/api/smsCode/verification", method: .post, parameters: parameter).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                successHandler?(json)
            case .failure(let error):
                failureHandler?(error)
            }
        }
    }
    
    /// 学生注册
    class func signUpStudent(withPhoneNumber phoneNumber: String, password: String, successHandler: successClosure?, failureHandler: failureClosure?)
    {
        // 先请求公钥
        SignUpService.requestPublicKey(successHandler: { (json)-> () in
            if json["code"] == "200" {
                print("获取公钥成功\(json)")
                guard let publicKey = json["data"].string, let encryptedPassword = RSA.encryptString(password, publicKey: publicKey) else {
                    print("注册时密码加密失败")
                    return
                }
                
                // 请求公钥成功则进行注册操作
                let parameters = ["phone": phoneNumber, "password": encryptedPassword, "role": "0"]
                Alamofire.request("http://linkdust.xicp.net:50843/api/users", method: .post, parameters: parameters).responseJSON { (response) in
                    switch response.result {
                    case .success(let value):
                        successHandler?(JSON(value))
                    case .failure(let error):
                        failureHandler?(error)
                    }
                }
            } else {
                print("获取公钥失败\(json)")
            }
        }, failureHandler: nil)
    }
    
    /// 学生登录
    class func logIn(withPhoneNumber phoneNumber: String, password: String, successHandler: successClosure?, failureHandler: failureClosure?)
    {
        // 先请求公钥
        SignUpService.requestPublicKey(successHandler: { (json)-> () in
            if json["code"] == "200" {
                print("获取公钥成功\(json)")
                guard let publicKey = json["data"].string, let encryptedPassword = RSA.encryptString(password, publicKey: publicKey) else {
                    print("登录时密码加密失败")
                    return
                }
                
                // 请求公钥成功则进行登录操作
                let parameters = ["phone": phoneNumber, "password": encryptedPassword]
                Alamofire.request("http://linkdust.xicp.net:50843/api/users/login", method: .post, parameters: parameters).responseJSON { (response) in
                    switch response.result {
                    case .success(let value):
                        successHandler?(JSON(value))
                    case .failure(let error):
                        failureHandler?(error)
                    }
                }
            } else {
                print("获取公钥失败\(json)")
            }
        }, failureHandler: nil)
    }
    
    /// 向数据库写入登录状态
    class func writeLogInStatus(isLogged: Bool, json: JSON?)
    {
        let userDefaults = UserDefaults.standard
        
        userDefaults.set(isLogged, forKey: "isLogged")
        if isLogged {
            guard let json = json else {
                return
            }

            let name = json["data"]["person"]["name"].string
            let major = json["data"]["person"]["major"].string
            let phone = json["data"]["person"]["phone"].string
            let school = json["data"]["person"]["school"].string
            let classroom = json["data"]["person"]["_class"].string
            let id = json["data"]["person"]["_id"].string
            let academy = json["data"]["person"]["academy"].string
            let avatarUrl = json["data"]["person"]["avatar"].string
            let number = json["data"]["person"]["number"].string
            let grade = json["data"]["person"]["grade"].string
            let gender = json["data"]["person"]["gender"].string
            let mail = json["data"]["person"]["mail"].string
            let student = Student(name: name, major: major, phone: phone, school: school, classroom: classroom, id: id,
                                  academy: academy, avatarUrl: avatarUrl, number: number, grade: grade, gender: gender, mail: mail)
            print("写入Student到数据库: \n\(student)")
            userDefaults.set(NSKeyedArchiver.archivedData(withRootObject: student), forKey: "Student")
        } else {
            userDefaults.removeObject(forKey: "Student")
        }
        userDefaults.synchronize()
    }
    
}
