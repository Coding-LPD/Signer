//
//  SignUpStep3ViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import Alamofire
import SwiftyJSON

class SignUpStep3ViewController: UIViewController, LoadingButtonDelegate
{
    var phoneNumber: String?

    @IBOutlet weak var passwordTextField: UnderlineTextField!
    @IBOutlet weak var validationTextField: UnderlineTextField!
    @IBOutlet weak var doneButton: LoadingButton!
    
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        initUI()
    }
    
    func initUI()
    {
        passwordTextField.isSecureTextEntry = true
        validationTextField.isSecureTextEntry = true
        
        Observable
            .combineLatest(passwordTextField.textField.rx.text.asObservable(), validationTextField.textField.rx.text.asObservable()) { (password, validation) -> Bool in
                return password!.length > 0 && validation!.length > 0
            }
            .subscribe(onNext: { (isEnabled) in
                self.doneButton.isEnabled = isEnabled
            }, onError: nil, onCompleted: nil, onDisposed: nil)
            .addDisposableTo(disposeBag)
        
        doneButton.delegate = self
    }
    
    // 学生注册
    func didClickButton()
    {
        guard let password = passwordTextField.text, let validation = validationTextField.text,
            let phoneNumber = phoneNumber, let encryptedPassword = password.encrypt(withPublicKey: SignUpRouter.publicKey) else {
            return
        }
        
        guard password == validation else {
            view.makeToast("密码不一致", duration: 1.0, position: .center)
            return
        }
        
        doneButton.startWaiting()

        Alamofire
            .request(SignUpRouter.signUpStudent(phoneNumber, encryptedPassword))
            .responseJSON { (response) in
                self.doneButton.stopWaiting()
                
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("学生注册: \(json)")
                    DispatchQueue.main.async {
                        if json["code"] == "200" {
                            LogInViewController.writeLogInStatus(isLogged: true, isStudent: true, id: json["data"]["person"]["_id"].stringValue, phone: phoneNumber, name: json["data"]["person"]["name"].stringValue, avatarUrl: json["data"]["person"]["avatar"].stringValue)
                            self.showHomePage()
                        } else {
                            fatalError("注册失败")
                        }
                    }
                case .failure:
                    self.view.makeToast("注册失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        let alert = UIAlertController(title: nil, message: "退出注册？", preferredStyle: .alert)
        let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
        let doneAction = UIAlertAction(title: "确定", style: .destructive, handler: { (action) in
            self.dismiss(animated: true, completion: nil)
        })
        alert.addAction(cancelAction)
        alert.addAction(doneAction)
        present(alert, animated: true, completion: nil)
    }

    private func showHomePage()
    {
        if let mainTabBarVC = storyboard?.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            let appDelegate = UIApplication.shared.delegate as! AppDelegate
            appDelegate.monitorNewNotice()          // 学生监听新的签到通知
            present(mainTabBarVC, animated: true, completion: nil)
        }
    }
 
}
