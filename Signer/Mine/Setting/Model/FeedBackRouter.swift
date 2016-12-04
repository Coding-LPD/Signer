//
//  FeedBackRouter.swift
//  Signer
//
//  Created by Vernon on 2016/12/4.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Alamofire

enum FeedBackRouter: URLRequestConvertible
{
    case feedback(studentId: String, name: String, phone: String, content: String)
    
    var method: HTTPMethod {
        switch self {
        case .feedback:
            return .post
        }
    }
    
    func asURLRequest() throws -> URLRequest {
        let result: (path: String, parameters: Parameters) = {
            switch self {
            case let .feedback(studentId, name, phone, content):
                return ("/feedbacks", ["studentId": studentId, "name": name, "phone": phone, "content": content])
            }
        }()
        
        let url = try SignUpRouter.baseURLString.asURL()
        var urlRequest = URLRequest(url: url.appendingPathComponent(result.path))
        urlRequest.httpMethod = method.rawValue
        
        return try URLEncoding.default.encode(urlRequest, with: result.parameters)
    }
    
}
