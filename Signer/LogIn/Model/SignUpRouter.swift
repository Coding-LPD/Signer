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

    static let baseSocketUrl = Constant.baseSocketUrl
    static let baseAPIURL = Constant.baseAPIUrl
    static let publicKey = "-----BEGIN PUBLIC KEY-----\nMFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIeiKZeAhYhsNgMrCNEonJA+YdbWsAC9\nDGz0WsU/zKSPVqggI9G+P//Ip4W9U9zZNiwL22E+ZK5Py/fCxzbvk6sCAwEAAQ==\n-----END PUBLIC KEY-----"

    case requestVerifyCode(String)                                  // 发送验证码到指定手机号码
    case validatePhoneAndCode(String, String)                       // 验证手机号码和验证码的一致性
    case signUpStudent(String, String)                              // 注册学生
    case logInStudent(phone: String, encryptedPassword: String)     // 学生登录
    case modifyPassword(phone: String, encryptedPassword: String)   // 修改密码
    
    var method: HTTPMethod {
        switch self {
        case .requestVerifyCode, .validatePhoneAndCode, .signUpStudent, .logInStudent:
            return .post
        case .modifyPassword:
            return .put
        }
    }
    
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
            case let .modifyPassword(phone, encryptedPassword):
                return ("/users/\(phone)", ["password": encryptedPassword])
            }
        }()
        
        let url = try SignUpRouter.baseAPIURL.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
