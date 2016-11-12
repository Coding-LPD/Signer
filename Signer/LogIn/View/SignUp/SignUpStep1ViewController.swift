//
//  SignUpStep1ViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
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
        
        SignUpService.requestVerifyCode(parameter: ["phone": phoneNumber], successHandler: { [weak self] (json) -> () in
            DispatchQueue.main.async {
                self?.nextStepButton.stopWaiting()
                self?.performSegue(withIdentifier: "Step2", sender: nil)
            }
            }, failureHandler: nil)
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
