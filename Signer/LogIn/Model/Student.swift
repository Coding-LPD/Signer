//
//  Student.swift
//  Signer
//
//  Created by Vernon on 2016/11/10.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Foundation

struct Student
{
    // ------------------ 必填 ---------------------
    /** 唯一标识*/
    var id: String
    /** 手机号*/
    var phone: String
    /** 姓名*/
    var name: String
    /** 头像*/
    var avatarUrl: String
    
    // ------------------ 选填 ---------------------
    /** 专业*/
    var major: String?
    /** 学校*/
    var school: String?
    /** 班级*/
    var classroom: String?
    /** 学院*/
    var academy: String?
    /** 学号*/
    var number: String?
    /** 年级*/
    var grade: String?
    /** 性别*/
    var gender: String?
    /** 邮箱*/
    var mail: String?

    init()
    {
        let userDefaults = UserDefaults.standard
        guard let id = userDefaults.object(forKey: "id") as? String, let phone = userDefaults.object(forKey: "phone") as? String, let name = userDefaults.object(forKey: "name") as? String, let avatarUrl = userDefaults.object(forKey: "avatarUrl") as? String else {
            fatalError("初始化学生对象失败！")
        }
        
        self.id = id
        self.phone = phone
        self.name = name
        self.avatarUrl = avatarUrl
    }
}
