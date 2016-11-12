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
    
    var isPhoneNumber: Bool {
        let phoneNumberRegex = "[1][358]\\d{9}"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", phoneNumberRegex)
        let result =  phoneTest.evaluate(with: self)
        return result
    }
    
    var isVerifyCode: Bool {
        let verifyCodeRegex = "\\d{6}"
        let verifyCodeTest = NSPredicate(format: "SELF MATCHES %@", verifyCodeRegex)
        let result = verifyCodeTest.evaluate(with: self)
        return result
    }
}
