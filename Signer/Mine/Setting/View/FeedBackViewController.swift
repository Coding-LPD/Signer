//
//  FeedBackViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/4.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift
import Alamofire
import SwiftyJSON

class FeedBackViewController: UIViewController
{
    @IBOutlet weak var sendBarButton: UIBarButtonItem!
    @IBOutlet weak var contentTextView: UITextView!

    let disposeBag = DisposeBag()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        sendBarButton.setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(netHex: 0x97cc00)], for: .normal)
        sendBarButton.setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(netHex: 0x999999).withAlphaComponent(0.5)], for: .disabled)
        
        contentTextView.rx.text.subscribe(onNext: { content in
            self.sendBarButton.isEnabled = content!.length > 0
        }).addDisposableTo(disposeBag)
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        contentTextView.becomeFirstResponder()
    }
    
    @IBAction func sendAction(_ sender: UIBarButtonItem)
    {
        view.makeToastActivity(.center)
        
        let student = Student()
        Alamofire
            .request(FeedBackRouter.feedback(studentId: student.id, name: student.name, phone: student.phone, content: contentTextView.text))
            .responseJSON { (response) in
                self.view.hideToastActivity()
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("发送反馈: \(json)")
                    if json["code"] == "200" {
                        DispatchQueue.main.async {
                            self.view.makeToast("发送反馈成功", duration: 1.0, position: .center)
                            self.contentTextView.text = ""
                        }
                    } else {
                        fatalError("发送反馈失败")
                    }
                case .failure(_):
                    self.view.makeToast("发送反馈失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
    }

}
