//
//  SignUpStep2ViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import Alamofire
import SwiftyJSON

class SignUpStep2ViewController: UIViewController, CountdownUnderlineTextFieldDelegate, LoadingButtonDelegate
{
    var phoneNumber: String?
    
    @IBOutlet weak var verifyCodeTextField: CountdownUnderlineTextField!
    @IBOutlet weak var nextStepButton: LoadingButton!
    
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        initUI()
    }
    
    func initUI()
    {
        verifyCodeTextField.delegate = self
        verifyCodeTextField.keyboardType = .numberPad
        verifyCodeTextField.startCountdown()
        verifyCodeTextField.textField.rx.text
            .map { (verifyCode) -> Bool in
                return verifyCode!.isVerifyCode
            }
            .subscribe(onNext: { (isEnabled) in
                self.nextStepButton.isEnabled = isEnabled
            }, onError: nil, onCompleted: nil, onDisposed: nil)
            .addDisposableTo(disposeBag)
        
        nextStepButton.delegate = self
    }

    
    func didClickButton()
    {
        guard let verifyCode = verifyCodeTextField.text, let phoneNumber = phoneNumber else {
            return
        }
    
        nextStepButton.startWaiting()
        
        Alamofire
            .request(SignUpRouter.validatePhoneAndCode(phoneNumber, verifyCode))
            .responseJSON { (response) in
                self.nextStepButton.stopWaiting()
                
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("验证手机号和验证码: \(json)")
                    DispatchQueue.main.async {
                        if(json["code"] == "200") {
                            self.performSegue(withIdentifier: "Step3", sender: nil)
                        } else {
                            self.view.makeToast("验证码错误，请重新输入", duration: 1.0, position: .center)
                        }
                    }
                case .failure:
                    self.view.makeToast("检查网络连接", duration: 1.0, position: .center)
                }
            }
    }

    
    // 重新发送验证码
    func countdownUnderlineTextFieldDidClickButton()
    {
        sendVerifycode()
    }
    
    func sendVerifycode()
    {
        guard let phoneNumber = phoneNumber else {
            return
        }
        
        Alamofire.request(SignUpRouter.requestVerifyCode(phoneNumber)).responseJSON { (response) in
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                print("发送验证码: \(json)")
                DispatchQueue.main.async {
                    if json["code"] == "200" {
                        self.view.makeToast("发送验证码成功", duration: 1.0, position: .center)
                    }
                }
            case .failure(let error):
                fatalError("发送验证码失败: \(error.localizedDescription)")
            }
        }
    }
    
    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        _ = navigationController?.popViewController(animated: true)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "Step3" {
            if let desVC = segue.destination as? SignUpStep3ViewController {
                desVC.phoneNumber = phoneNumber
            }
        }
    }
    
    
}

