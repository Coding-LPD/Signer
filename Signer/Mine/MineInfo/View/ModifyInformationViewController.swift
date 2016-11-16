//
//  ModifyInformationViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class ModifyInformationViewController: UIViewController
{
    var caption: String?            // 标题
    var content: String?            // 内容
    var studentID: String?          // 学生id
    var parameterName: String?      // 修改的参数
    
    @IBOutlet weak var textField: UITextField!

    override func viewDidLoad()
    {
        super.viewDidLoad()

        title = caption
        textField.text = content
        
        print("studentID: \(studentID)")
    }
    
    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        _ = navigationController?.popViewController(animated: true)
    }
    
    @IBAction func saveAction(_ sender: UIBarButtonItem)
    {
        guard let parameterName = parameterName, let studentID = studentID else {
            fatalError("参数名还未初始化")
        }
        
        Alamofire
            .request(StudentRouter.modifyStudent(id: studentID, parameters: [parameterName: textField.text ?? ""]))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("修改学生信息: \(json)")
                    if json["code"] == "200" {
                        _ = self.navigationController?.popViewController(animated: true)
                    } else {
                        self.view.makeToast("修改信息失败，检查网络连接", duration: 1.0, position: .center)
                    }
                case .failure(let error):
                    fatalError("修改学生信息失败: \(error.localizedDescription)")
                }
            }
    }

}
