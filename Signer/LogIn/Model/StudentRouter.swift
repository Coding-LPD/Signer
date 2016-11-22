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
    static let baseURLString = "http://120.25.65.207:3000/api/students"

    case queryStudent(id: String)       // 查询指定id的学生信息
    case modifyStudent(id: String, parameters: Parameters)     // 修改指定id的学生信息
    
    var method: HTTPMethod {
        switch self {
        case .queryStudent:
            return .post
        case .modifyStudent:
            return .put
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .queryStudent(id):
                return ("/search", ["_id": id])
            case let .modifyStudent(id, parameters):
                return ("/\(id)", parameters)
            }
        }()
        
        let url = try StudentRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
