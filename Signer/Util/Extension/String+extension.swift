//
//  String+extension.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import Foundation

extension String
{
    var length: Int {
        return self.characters.count
    }

    subscript (r: Range<Int>) -> String
    {
        let range = Range(uncheckedBounds: (lower: max(0, min(length, r.lowerBound)),
                                            upper: min(length, max(0, r.upperBound))))
        let start = index(startIndex, offsetBy: range.lowerBound)
        let end = index(start, offsetBy: range.upperBound - range.lowerBound)
        return self[Range(start ..< end)]
    }
    
    subscript (i: Int) -> String
    {
        return self[Range(i ..< i + 1)]
    }
    
    func substring(from: Int) -> String
    {
        return self[Range(min(from, length) ..< length)]
    }
    
    func substring(to: Int) -> String
    {
        return self[Range(0 ..< max(0, to))]
    }
    
    /// 检查是否是电话号码
    var isPhoneNumber: Bool {
        let phoneNumberRegex = "[1][358]\\d{9}"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", phoneNumberRegex)
        let result =  phoneTest.evaluate(with: self)
        return result
    }
    
    /// 检查是否是验证码(6位数字)
    var isVerifyCode: Bool {
        let verifyCodeRegex = "\\d{6}"
        let verifyCodeTest = NSPredicate(format: "SELF MATCHES %@", verifyCodeRegex)
        let result = verifyCodeTest.evaluate(with: self)
        return result
    }
    
    // 将字符串stringToBeEncrypted用公钥publicKey加密
    func encrypt(withPublicKey publicKey: String) -> String?
    {
        if let encryptString = RSA.encryptString(self, publicKey: publicKey) {
            return encryptString
        }
        return nil
    }
}
