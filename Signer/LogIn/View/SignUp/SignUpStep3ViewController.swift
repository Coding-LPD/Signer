//
//  SignUpStep3ViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift

class SignUpStep3ViewController: UIViewController, LoadingButtonDelegate
{
    var phoneNumber: String?// = "15603005850"

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
        guard let password = passwordTextField.text, let validation = validationTextField.text, let phoneNumber = phoneNumber else {
            return
        }
        
        guard password == validation else {
            view.makeToast("密码不一致", duration: 1.0, position: .center)
            return
        }
        
        doneButton.startWaiting()

        SignUpService.signUpStudent(withPhoneNumber: phoneNumber, password: password, successHandler: { [weak self] (json) -> () in
            print("注册学生成功: \(json)")
            DispatchQueue.main.async {
                self?.doneButton.stopWaiting()
                if json["code"] == "200" {
                    SignUpService.writeLogInStatus(isLogged: true, json: json)
                    self?.showHomePage()
                } else {
                    self?.view.makeToast("注册失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
        }, failureHandler: nil)
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        _ = navigationController?.popViewController(animated: true)
    }

    func showHomePage()
    {
        if let mainTabBarVC = storyboard?.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            present(mainTabBarVC, animated: true, completion: nil)
        }
    }
}
