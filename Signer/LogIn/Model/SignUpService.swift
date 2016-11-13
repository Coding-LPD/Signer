//
//  SignUpService.swift
//  Signer
//
//  Created by Vernon on 2016/11/7.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire
import SwiftyJSON

enum SignUpRouter: URLRequestConvertible
{
    static let baseURLString = "http://120.25.65.207:3000/api"
    static let publicKey = "-----BEGIN PUBLIC KEY-----\nMFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIeiKZeAhYhsNgMrCNEonJA+YdbWsAC9\nDGz0WsU/zKSPVqggI9G+P//Ip4W9U9zZNiwL22E+ZK5Py/fCxzbvk6sCAwEAAQ==\n-----END PUBLIC KEY-----"

    case requestVerifyCode(String)                                  // 发送验证码到指定手机号码
    case validatePhoneAndCode(String, String)                       // 验证手机号码和验证码的一致性
    case signUpStudent(String, String)                              // 注册学生
    case logInStudent(phone: String, encryptedPassword: String)     // 学生登录
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .requestVerifyCode(phone):
                return ("/smsCode", ["phone": phone])
            case let .validatePhoneAndCode(phone, smsCode):
                return ("/smsCode/verification", ["phone": phone, "smsCode": smsCode])
            case let .signUpStudent(phone, encryptedPassword):
                return ("/users", ["phone": phone, "password": encryptedPassword, "role": "0"])
            case let .logInStudent(phone, encryptedPassword):
                return ("/users/login", ["phone": phone, "password": encryptedPassword])
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = HTTPMethod.post.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}

class SignUpService
{
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
