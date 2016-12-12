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
    case searchCourse(phone: String, courseName: String)        // 根据指定课程名查询课程
    case requestNotice(phone: String, type: Int, page: Int)     // 获取学生相关通知信息
    case requestSignAndMsgCount(studentId: String)              // 获取学生的签到数和发言数
    case requestSignDatesInMonth(id: String, date: String)      // 获取指定id学生的指定月份的完成签到的日期，date格式为2016-10（必须指定）
    case requestSignInDate(id: String, date: String)            // 获取指定id学生的某天完成的签到，date格式为2016-10-22（必须指定）
    case requestChatDatesInMonth(id: String, date: String)      // 获取指定id学生的指定月份的完成签到的日期，date格式为2016-10（必须指定）
    case requestChatInDate(id: String, date: String)            // 获取指定id学生的某天的发言，date格式为2016-10-22（必须指定）
    
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
        case .searchCourse:
            return .get
        case .requestNotice:
            return .get
        case .requestSignAndMsgCount:
            return .get
        case .requestSignDatesInMonth:
            return .get
        case .requestSignInDate:
            return .get
        case .requestChatDatesInMonth:
            return .get
        case .requestChatInDate:
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
            case let .searchCourse(phone, courseName):
                return ("/students/\(phone)/relatedCourses", ["limit": 1000, "page": 0, "keyword": courseName])
            case let .requestNotice(phone, type, page):
                return ("/students/\(phone)/notice", ["type": type, "page": page, "limit": 10])
            case let .requestSignAndMsgCount(studentId):
                return ("/students/\(studentId)/activeInfo", [:])
            case let .requestSignDatesInMonth(studentId, dateString):
                return ("/students/\(studentId)/signInDays", ["date": dateString])
            case let .requestSignInDate(studentId, dateString):
                return ("/students/\(studentId)/signInDays/detail", ["date": dateString])
            case let .requestChatDatesInMonth(studentId, dateString):
                return ("/students/\(studentId)/chatDays", ["date": dateString])
            case let .requestChatInDate(studentId, dateString):
                return ("/students/\(studentId)/chatDays/detail", ["date": dateString])
            }
        }()
        
        let url = try SignUpRouter.baseAPIURL.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
