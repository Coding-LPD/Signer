//
//  SignUpStep1ViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import Alamofire
import SwiftyJSON
import Toast_Swift

class SignUpStep1ViewController: UIViewController, LoadingButtonDelegate
{
    
    @IBOutlet weak var phoneNumberTextField: UnderlineTextField!
    @IBOutlet weak var nextStepButton: LoadingButton!
    
    fileprivate let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        initUI()
    }
    
    func initUI()
    {
        phoneNumberTextField.keyboardType = .numberPad
        phoneNumberTextField.textField.rx.text.map { (phoneNumber) -> Bool in
            return phoneNumber!.isPhoneNumber
            }
            .subscribe(onNext: { (isEnabled) in
                self.nextStepButton.isEnabled = isEnabled
            }, onError: nil, onCompleted: nil, onDisposed: nil)
            .addDisposableTo(disposeBag)
        
        nextStepButton.delegate = self
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        dismiss(animated: true, completion: nil)
    }
    
    // 发送验证码到指定手机
    func didClickButton()
    {
        guard let phoneNumber = phoneNumberTextField.text else {
            return
        }

        nextStepButton.startWaiting()
        
        Alamofire
            .request(SignUpRouter.requestVerifyCode(phoneNumber))
            .responseJSON { (response) in
                self.nextStepButton.stopWaiting()
                
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    DispatchQueue.main.async {
                        if json["code"] == "200" {
                            self.performSegue(withIdentifier: "Step2", sender: nil)
                        } else {
                            fatalError("发送验证码失败")
                        }
                    }
                case .failure:
                    self.view.makeToast("检查网络连接", duration: 1.0, position: .center)
                }
        }
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "Step2" {
            if let desVC = segue.destination as? SignUpStep2ViewController {
                desVC.phoneNumber = phoneNumberTextField.text
            }
        }
    }
    
    

}
