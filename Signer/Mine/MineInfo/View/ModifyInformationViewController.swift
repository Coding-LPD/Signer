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

enum ModifyType
{
    case number(oldNumber: String), name(oldName: String), school(oldSchool: String), academy(oldAcademy: String), major(oldMajor: String), mail(oldMail: String)
    
    var parameterName: String {
        switch self {
        case .number:
            return "number"
        case .name:
            return "name"
        case .school:
            return "school"
        case .academy:
            return "academy"
        case .major:
            return "major"
        case .mail:
            return "mail"
        }
    }
    
    var oldValue: String {
        switch self {
        case .number(let oldNumber):
            return oldNumber
        case .name(let oldName):
            return oldName
        case .school(let oldSchool):
            return oldSchool
        case .academy(let oldAcademy):
            return oldAcademy
        case .major(let oldMajor):
            return oldMajor
        case .mail(let oldMail):
            return oldMail
        }
    }
    
    var title: String {
        switch self {
        case .number:
            return "学号"
        case .name:
            return "姓名"
        case .school:
            return "学校"
        case .academy:
            return "学院"
        case .major:
            return "专业"
        case .mail:
            return "邮箱"
        }
    }
}

class ModifyInformationViewController: UIViewController
{
    var studentID: String?          // 学生id
    var modifyType: ModifyType?
    
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var saveBarButton: UIBarButtonItem!

    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let type = modifyType else {
            fatalError("ModifyInformationViewController没有传入初始值")
        }
        
        title = type.title
        textField.text = type.oldValue
        
        saveBarButton.setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(netHex: 0x97cc00)], for: .normal)
    }

    @IBAction func saveAction(_ sender: UIBarButtonItem)
    {
        guard let type = modifyType, let id = studentID else {
            fatalError("ModifyInformationViewController没有传入初始值")
        }
        
        Alamofire
            .request(StudentRouter.modifyStudent(id: id, parameters: [type.parameterName: textField.text ?? ""]))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("修改学生信息: \(json)")
                    if json["code"] == "200" {
                        _ = self.navigationController?.popViewController(animated: true)
                    } else {
                        fatalError("修改学生信息失败")
                    }
                case .failure(_):
                    DispatchQueue.main.async {
                        self.view.makeToast("修改信息失败，检查网络连接", duration: 1.0, position: .center)
                    }
                }
            }
    }

}
