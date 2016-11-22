//
//  UIView+extension.swift
//  Signer
//
//  Created by Vernon on 2016/11/22.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

extension UIView
{
    var shadowUIColor: UIColor {
        get {
            return UIColor(cgColor: layer.shadowColor!)
        }
        set {
            layer.shadowColor = newValue.cgColor
        }
    }

}
