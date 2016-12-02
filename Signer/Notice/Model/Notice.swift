//
//  Notice.swift
//  Signer
//
//  Created by Vernon on 2016/12/2.
//  Copyright © 2016年 Vernon. All rights reserved.
//

enum SignState: Int
{
    case confirm = 1
    case reject = 2
    
    init?(num: Int)
    {
        if num == 1 || num == 2 {
            self.init(rawValue: num)
        } else {
            return nil
        }
    }
}

struct Notice
{
    var courseName: String
    var signNumber: Int
    var signState: SignState
    var distance: Int
    var signDate: Date
    var confirmDate: Date
}
