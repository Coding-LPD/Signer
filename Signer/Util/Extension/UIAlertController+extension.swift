//
//  UIAlertController+extension.swift
//  Signer
//
//  Created by Vernon on 2016/11/29.
//  Copyright © 2016年 Vernon. All rights reserved.
//

extension UIAlertController
{
    class func showOnlySureAlertController(title: String?, message: String?, sureButtonTitle: String, sureHandler: @escaping ()->()) -> UIAlertController
    {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let doneAction = UIAlertAction(title: sureButtonTitle, style: .cancel, handler: { (_) in
            sureHandler()
        })
        alert.addAction(doneAction)
        
        
        return alert
    }
}
