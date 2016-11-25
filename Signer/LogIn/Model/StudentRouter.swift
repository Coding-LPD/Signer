//
//  StudentRouter.swift
//  Signer
//
//  Created by Vernon on 2016/11/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire

enum StudentRouter: URLRequestConvertible
{
    case queryStudent(id: String)       // 查询指定id的学生信息
    case uploadAvatar
    case modifyStudent(id: String, parameters: Parameters)     // 修改指定id的学生信息
    
    var method: HTTPMethod {
        switch self {
        case .queryStudent:
            return .post
        case .uploadAvatar:
            return .post
        case .modifyStudent:
            return .put
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .queryStudent(id):
                return ("/students/search", ["_id": id])
            case .uploadAvatar:
                return ("/students", [:])
            case let .modifyStudent(id, parameters):
                return ("/students/\(id)", parameters)
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
