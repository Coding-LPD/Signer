//
//  SignRouter.swift
//  Signer
//
//  Created by Vernon on 2016/11/25.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire

enum SignRouter: URLRequestConvertible
{
    case scanQRCode(code: String)       // 将6位签到码发送到服务器，服务器返回对应课程的相关信息
    case sign(signId: String, phoneId: String, studentId: String, type: Int, battery: Float, longitude: Double, latitude: Double)   // 学生进行签到
    
    var method: HTTPMethod {
        switch self {
        case .scanQRCode:
            return .get
        case .sign:
            return .post
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .scanQRCode(code):
                return ("/signs/scanning/\(code)", [:])
            case let .sign(signId, phoneId, studentId, type, battery, longitude, latitude):
                return ("/signRecords", ["signId": signId, "phoneId": phoneId, "studentId": studentId, "type": type, "battery": battery, "longitude": longitude, "latitude": latitude])
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
