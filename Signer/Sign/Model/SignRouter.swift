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
    case scanQRCode(code: String)       // 查询指定id的学生信息
    
    var method: HTTPMethod {
        switch self {
        case .scanQRCode:
            return .get
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .scanQRCode(code):
                return ("/signs/scanning/\(code)", [:])
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
