
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
                            self.saveInformationWith(json: json["data"], phoneNumber: phoneNumber)
                        } else {
                                self.view.makeToast("登录失败，手机号与密码不匹配", duration: 1.0, position: .center)
                        }
                    }
                case .failure:
                    self.view.makeToast("登录失败，检查网络连接", duration: 0.3, position: .center)
            }
        }
    }
    
    func saveInformationWith(json: JSON, phoneNumber: String)
    {
        let role = json["user"]["role"].intValue
        let id = json["person"]["_id"].stringValue
        let name = json["person"]["name"].stringValue
        let tempUrl = json["person"]["avatar"].stringValue
        let avatarUrl = tempUrl.length > 0 ? tempUrl : Constant.defaultAvatar1Url
        
        if role == 0 {
            // 学生
            LogInViewController.writeLogInStatus(isLogged: true, isStudent: true, id: id, phone: phoneNumber, name: name, avatarUrl: avatarUrl)
            showStudentHomePage()
        } else if role == 1 {
            // 老师
            LogInViewController.writeLogInStatus(isLogged: true, isStudent: false, id: id, phone: phoneNumber, name: name, avatarUrl: avatarUrl)
            showTeacherHomePage()
        }
    }
    
    func showStudentHomePage()
    {
        if let mainTabBarVC = storyboard?.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            present(mainTabBarVC, animated: true, completion: nil)
        }
    }
    
    func showTeacherHomePage()
    {
        let tabBarVC = UITabBarController()
        tabBarVC.tabBar.tintColor = ThemeGreenColor
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        
        let chatRoomVC = storyboard.instantiateViewController(withIdentifier: "ChatRoomNavigation")
        chatRoomVC.tabBarItem.title = "聊天室"
        chatRoomVC.tabBarItem.image = UIImage(named: "tab_chatRoom")
        tabBarVC.addChildViewController(chatRoomVC)
        
        let settingVC = storyboard.instantiateViewController(withIdentifier: "SettingViewController")
        settingVC.tabBarItem.title = "设置"
        settingVC.tabBarItem.image = UIImage(named: "tab_mine")
        let navigationVC = UINavigationController(rootViewController: settingVC)
        navigationVC.navigationBar.tintColor = UIColor(netHex: 0x666666)
        tabBarVC.addChildViewController(navigationVC)
        
        present(tabBarVC, animated: true, completion: nil)
    }
    
    static func writeLogInStatus(isLogged: Bool, isStudent: Bool, id: String, phone: String, name: String, avatarUrl: String)
    {
        let userDefaults = UserDefaults.standard
        userDefaults.set(isLogged, forKey: "isLogged")
        userDefaults.set(isStudent, forKey: "isStudent")
        userDefaults.set(phone, forKey: "phone")
        userDefaults.set(id, forKey: "id")
        userDefaults.set(name, forKey: "name")
        userDefaults.set(avatarUrl, forKey: "avatarUrl")
        userDefaults.synchronize()
    }
    

}
