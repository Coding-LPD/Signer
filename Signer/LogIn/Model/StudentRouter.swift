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
    case queryStudent(id: String)                               // 查询指定id的学生信息
    case uploadAvatar                                           // 上传头像
    case modifyStudent(id: String, parameters: Parameters)      // 修改指定id的学生信息
    case requestSignedCourses(phone: String)                    // 学生的所有签到的课程
    
    var method: HTTPMethod {
        switch self {
        case .queryStudent:
            return .post
        case .uploadAvatar:
            return .post
        case .modifyStudent:
            return .put
        case .requestSignedCourses:
            return .get
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
            case let .requestSignedCourses(phone):
                return ("/students/\(phone)/relatedCourses", ["limit": 1000, "page": 0])
                
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
