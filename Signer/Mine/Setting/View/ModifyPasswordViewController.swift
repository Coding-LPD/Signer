//
//  ModifyPasswordViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/4.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import Alamofire
import SwiftyJSON

class ModifyPasswordViewController: UIViewController, LoadingButtonDelegate
{
    @IBOutlet weak var oldPasswordTextField: UnderlineTextField!
    @IBOutlet weak var newPasswordTextField: UnderlineTextField!
    @IBOutlet weak var confirmPasswordTextField: UnderlineTextField!
    @IBOutlet weak var modifyButton: LoadingButton!
    
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        initUI()
    }
    
    func initUI()
    {
        
        
        oldPasswordTextField.isSecureTextEntry = true
        newPasswordTextField.isSecureTextEntry = true
        confirmPasswordTextField.isSecureTextEntry = true
        
        Observable
            .combineLatest(oldPasswordTextField.textField.rx.text.asObservable(), newPasswordTextField.textField.rx.text.asObservable(), confirmPasswordTextField.textField.rx.text.asObservable()) { (oldPassword, newPassword, confirmPassword) -> Bool in
                return oldPassword!.length > 0 && newPassword!.length > 0 && confirmPassword!.length > 0
            }
            .subscribe(onNext: { (isEnabled) in
                self.modifyButton.isEnabled = isEnabled
            }, onError: nil, onCompleted: nil, onDisposed: nil)
            .addDisposableTo(disposeBag)
        
        modifyButton.delegate = self
    }

    // 修改密码
    func didClickButton()
    {
        guard let oldPassword = oldPasswordTextField.text, let newPassword = newPasswordTextField.text,
            let confirmPassword = confirmPasswordTextField.text else {
                return
        }
        
        guard newPassword == confirmPassword else {
            view.makeToast("新密码和确认密码不一致", duration: 1.0, position: .center)
            return
        }
        
        guard let encryptedOldPassword = oldPassword.encrypt(withPublicKey: SignUpRouter.publicKey),
            let encryptedNewPassword = newPassword.encrypt(withPublicKey: SignUpRouter.publicKey) else {
            fatalError("密码RSA加密失败")
        }
        
        modifyButton.startWaiting()
        
        Alamofire
            .request(SignUpRouter.logInStudent(phone: Student().phone, encryptedPassword: encryptedOldPassword))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    self.modifyButton.stopWaiting()
                    let json = JSON(value)
//                    print("修改密码时登录: \(json)")
                    if json["code"] == "200" {
                        
                        self.modifyButton.startWaiting()
                        Alamofire
                            .request(SignUpRouter.modifyPassword(phone: Student().phone, encryptedPassword: encryptedNewPassword))
                            .responseJSON(completionHandler: { (response) in
                                self.modifyButton.stopWaiting()
                                switch response.result {
                                case .success(let value):
                                    let json = JSON(value)
//                                    print("修改密码: \(json)")
                                    if json["code"] == "200" {
                                        DispatchQueue.main.async {
                                            self.oldPasswordTextField.text = nil
                                            self.newPasswordTextField.text = nil
                                            self.confirmPasswordTextField.text = nil
                                            self.view.makeToast("修改密码成功", duration: 1.0, position: .center)
                                        }
                                    } else {
                                        fatalError("修改密码失败")
                                    }
                                case .failure:
                                    self.view.makeToast("修改密码失败，检查网络连接", duration: 1.0, position: .center)
                                }
                            })
                        
                    } else {
                        self.view.makeToast("修改密码失败，原密码不正确", duration: 1.0, position: .center)
                    }
                case .failure:
                    
                    self.view.makeToast("修改密码失败，检查网络连接", duration: 1.0, position: .center)
                }
        }
    }
    

}
