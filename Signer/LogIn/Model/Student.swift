//
//  Student.swift
//  Signer
//
//  Created by Vernon on 2016/11/10.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Foundation

class Student: NSObject, NSCoding
{
    /** 姓名*/
    var name: String?
    /** 专业*/
    var major: String?
    /** 手机号*/
    var phone: String?
    /** 学校*/
    var school: String?
    /** 班级*/
    var classroom: String?
    /** 唯一标识*/
    var id: String?
    /** 学院*/
    var academy: String?
    /** 头像*/
    var avatarUrl: String?
    /** 学号*/
    var number: String?
    /** 年级*/
    var grade: String?
    /** 性别*/
    var gender: String?
    /** 邮箱*/
    var mail: String?
    
    
    class func currentStudent() -> Student
    {
        let userDefaults = UserDefaults.standard
        let decodeStudent = userDefaults.object(forKey: "Student") as? Data
        let student = NSKeyedUnarchiver.unarchiveObject(with: decodeStudent!) as! Student
        return student
    }
    
    init(name: String?, major: String?, phone: String?, school: String?,
         classroom: String?, id: String?, academy: String?, avatarUrl: String?,
         number: String?, grade: String?, gender: String?, mail: String?)
    {
        self.name = name
        self.major = major
        self.phone = phone
        self.school = school
        self.classroom = classroom
        self.id = id
        self.academy = academy
        self.avatarUrl = avatarUrl
        self.number = number
        self.grade = grade
        self.gender = gender
        self.mail = mail
        
        super.init()
    }
    
    override var description: String {
        return "学生信息:\n姓名: \(name)\n专业: \(major)\n电话: \(phone)\n学校: \(school)\n班级: \(classroom)\nid: \(id)\n学院: \(academy)\n头像地址: \(avatarUrl)\n学号: \(number)\n年级: \(grade)\n性别: \(gender)\n邮箱: \(mail)"
    }
    
    public func encode(with aCoder: NSCoder)
    {
        aCoder.encode(name, forKey: "name")
        aCoder.encode(major, forKey: "major")
        aCoder.encode(phone, forKey: "phone")
        aCoder.encode(school, forKey: "school")
        aCoder.encode(classroom, forKey: "classroom")
        aCoder.encode(id, forKey: "id")
        aCoder.encode(academy, forKey: "academy")
        aCoder.encode(avatarUrl, forKey: "avatarUrl")
        aCoder.encode(number, forKey: "number")
        aCoder.encode(grade, forKey: "grade")
        aCoder.encode(gender, forKey: "gender")
        aCoder.encode(mail, forKey: "mail")
    }
    
    required convenience init?(coder aDecoder: NSCoder)
    {
        let unarchivedName = aDecoder.decodeObject(forKey: "name") as? String
        let unarchivedMajor = aDecoder.decodeObject(forKey: "major") as? String
        let unarchivedPhone = aDecoder.decodeObject(forKey: "phone") as? String
        let unarchivedSchool = aDecoder.decodeObject(forKey: "school") as? String
        let unarchivedClassroom = aDecoder.decodeObject(forKey: "classroom") as? String
        let unarchivedId = aDecoder.decodeObject(forKey: "id") as? String
        let unarchivedAcademy = aDecoder.decodeObject(forKey: "academy") as? String
        let unarchivedAvatarUrl = aDecoder.decodeObject(forKey: "avatarUrl") as? String
        let unarchivedNumber = aDecoder.decodeObject(forKey: "number") as? String
        let unarchivedGrade = aDecoder.decodeObject(forKey: "grade") as? String
        let unarchivedGender = aDecoder.decodeObject(forKey: "gender") as? String
        let unarchivedMail = aDecoder.decodeObject(forKey: "mail") as? String

        
        self.init(name: unarchivedName, major: unarchivedMajor, phone: unarchivedPhone, school: unarchivedSchool, classroom: unarchivedClassroom, id: unarchivedId,
                  academy: unarchivedAcademy, avatarUrl: unarchivedAvatarUrl, number: unarchivedNumber, grade: unarchivedGrade, gender: unarchivedGender, mail: unarchivedMail)
    }

}
