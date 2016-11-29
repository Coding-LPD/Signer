//
//  CourseRouter.swift
//  Signer
//
//  Created by Vernon on 2016/11/29.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire

enum CourseRouter: URLRequestConvertible
{
    case requestCourseDetail(courseId: String)       // 查询指定id的课程的最近签到记录，返回课程信息和最多10个最近签到者头像
    
    var method: HTTPMethod {
        switch self {
        case .requestCourseDetail:
            return .get
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .requestCourseDetail(courseId):
                return ("/courses/\(courseId)/latestSignRecords", [:])
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
}
