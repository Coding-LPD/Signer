//
//  LogInViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/3.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import RxCocoa
import Alamofire
import SwiftyJSON

class LogInViewController: UIViewController, LoadingButtonDelegate
{
    
    @IBOutlet weak var phoneNumberTextField: UnderlineTextField!
    @IBOutlet weak var passwordTextField: UnderlineTextField!
    @IBOutlet weak var logInButton: LoadingButton!

    fileprivate let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        initUI()
    }
    
    func initUI()
    {
        phoneNumberTextField.keyboardType = .numberPad
        passwordTextField.isSecureTextEntry = true
        
        Observable.combineLatest(phoneNumberTextField.textField.rx.text.asObservable(), passwordTextField.textField.rx.text.asObservable()) { (phoneNumber, password) -> Bool in
            return phoneNumber!.isPhoneNumber && password!.length > 0
        }
        .subscribe(onNext: { (isEnabled) in
            self.logInButton.isEnabled = isEnabled
            }, onError: nil, onCompleted: nil, onDisposed: nil)
        .addDisposableTo(disposeBag)
        logInButton.delegate = self
    }
    
    func didClickButton()
    {
        guard let phoneNumber = phoneNumberTextField.text, let password = passwordTextField.text,
            let encryptedPassword = password.encrypt(withPublicKey: SignUpRouter.publicKey) else {
            return
        }
        
        logInButton.startWaiting()
        
        Alamofire
            .request(SignUpRouter.logInStudent(phone: phoneNumber, encryptedPassword: encryptedPassword))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("登录: \(json)")
                    DispatchQueue.main.async {
                        self.logInButton.stopWaiting()
                        if json["code"] == "200" {
                            LogInViewController.writeLogInStatus(isLogged: true, id: json["data"]["person"]["_id"].stringValue, phone: phoneNumber, name: json["data"]["person"]["name"].stringValue, avatarUrl: json["data"]["person"]["avatar"].stringValue)
                                self.showHomePage()
                            } else {
                                self.view.makeToast("登录失败，手机号与密码不匹配", duration: 1.0, position: .center)
                            }
                    }
                case .failure(let error):
                    fatalError("登录失败: \(error.localizedDescription)")
            }
        }
    }
    
    func showHomePage()
    {
        if let mainTabBarVC = storyboard?.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            present(mainTabBarVC, animated: true, completion: nil)
        }
    }
    
    static func writeLogInStatus(isLogged: Bool,id: String, phone: String, name: String, avatarUrl: String)
    {
        let userDefaults = UserDefaults.standard
        userDefaults.set(isLogged, forKey: "isLogged")
        userDefaults.set(phone, forKey: "phone")
        userDefaults.set(id, forKey: "id")
        userDefaults.set(name, forKey: "name")
        userDefaults.set(avatarUrl, forKey: "avatarUrl")
        userDefaults.synchronize()
    }
    

}
