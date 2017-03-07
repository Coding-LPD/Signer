//
//  MineInfoViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SDWebImage
import SwiftyJSON
import Toast_Swift

class MineInfoViewController: UITableViewController
{
    @IBOutlet weak var mineAvatarView: MineAvatarCell!
    @IBOutlet weak var numberCell: MineInfoCell!
    @IBOutlet weak var nameCell: MineInfoCell!
    @IBOutlet weak var genderCell: MineInfoCell!
    @IBOutlet weak var schoolCell: MineInfoCell!
    @IBOutlet weak var academyCell: MineInfoCell!
    @IBOutlet weak var majorCell: MineInfoCell!
    @IBOutlet weak var gradeCell: MineInfoCell!
    @IBOutlet weak var classCell: MineInfoCell!
    @IBOutlet weak var mailCell: MineInfoCell!
    
    let student = Student()
    
    private var json: JSON?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        initUI()
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        Alamofire
            .request(StudentRouter.queryStudent(id: student.id))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("查询学生成功: \(json)")
                    DispatchQueue.main.async {
                        self.configureUIWith(json: json["data"][0])
                        self.json = json["data"][0]
                        self.view.hideToastActivity()
                    }
                case .failure:
                    DispatchQueue.main.async {
                        self.view.makeToast("获取学生信息失败，检查网络连接", duration: 1.0, position: .center)
                        self.view.hideToastActivity()
                    }
                }
        }
    }
    
    @IBAction func unwindToThisViewController(segue: UIStoryboardSegue) {}
    
    func initUI()
    {
        tableView.tableHeaderView = UIView(frame: CGRect(x: 0, y: 0, width: tableView.bounds.size.width, height: 0.01))
        
        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)

        view.makeToastActivity(.center)
        
        setMsgAndSignCountDescription()
    }
    
    // 从服务器中获取学生的签到数signCount和发言数msgCount，设置字符串形如"\(signCount)签到/\(msgCount)发言"到描述信息上
    func setMsgAndSignCountDescription()
    {
        Alamofire
            .request(StudentRouter.requestSignAndMsgCount(studentId: Student().id))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("获取学生签到数和发言数: \(json)")
                    if json["code"] == "200" {
                        let signCount = json["data"]["signCount"].intValue
                        let msgCount = json["data"]["msgCount"].intValue
                        self.mineAvatarView.descriptionText = "\(signCount)签到/\(msgCount)发言"
                    }
                case .failure:
                    break
                }
            }
    }
    
    func configureUIWith(json: JSON)
    {
        LogInViewController.writeLogInStatus(isLogged: true, isStudent: true, id: student.id, phone: student.phone, name: json["name"].stringValue, avatarUrl: json["avatar"].stringValue)
        
        mineAvatarView.avatarImageView.sd_setImage(with: URL(string: json["avatar"].stringValue))
        mineAvatarView.nameText = json["name"].stringValue
    
        numberCell.contentText = json["number"].stringValue.length > 0 ? json["number"].stringValue : "未填写"
        nameCell.contentText = json["name"].stringValue.length > 0 ? json["name"].stringValue : "未填写"
        genderCell.contentText = json["gender"].stringValue.length > 0 ? json["gender"].stringValue : "未填写"
        schoolCell.contentText = json["school"].stringValue.length > 0 ? json["school"].stringValue : "未填写"
        academyCell.contentText = json["academy"].stringValue.length > 0 ? json["academy"].stringValue : "未填写"
        majorCell.contentText = json["major"].stringValue.length > 0 ? json["major"].stringValue : "未填写"
        gradeCell.contentText = json["grade"].stringValue.length > 0 ? json["grade"].stringValue : "未填写"
        classCell.contentText = json["_class"].stringValue.length > 0 ? json["_class"].stringValue : "未填写"
        mailCell.contentText = json["mail"].stringValue.length > 0 ? json["mail"].stringValue : "未填写"
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if indexPath.section == 0 {
            
        } else if indexPath.section == 1 && indexPath.row == 2 {
            ActionSheetStringPicker.show(withTitle: "性别", rows: [["男", "女", "保密"]], initialSelection: [0], doneBlock: {(_, _, selectedGender) in
                    self.modifyStudentWith(studentID: self.student.id, parameters: ["gender": selectedGender?.first as! String])
                }, cancel: nil, origin: self.view)
        } else if indexPath.section == 3 && indexPath.row == 0 {
            let currentYear = getYear()
            ActionSheetStringPicker.show(withTitle: "年级", rows: [["\(currentYear)级", "\(currentYear-1)级", "\(currentYear-2)级", "\(currentYear-3)级"]], initialSelection: [0], doneBlock: {(_, _, selectedGrade) in
                self.modifyStudentWith(studentID: self.student.id, parameters: ["grade": selectedGrade?.first as! String])
            }, cancel: nil, origin: self.view)
        } else if indexPath.section == 3 && indexPath.row == 1 {
            ActionSheetStringPicker.show(withTitle: "班级", rows: [["1班", "2班", "3班", "4班", "5班", "6班", "7班", "8班"]], initialSelection: [0], doneBlock: {(_, _, selectedClass) in
                self.modifyStudentWith(studentID: self.student.id, parameters: ["_class": selectedClass?.first as! String])
            }, cancel: nil, origin: self.view)
        }
    }
    
    func modifyStudentWith(studentID: String, parameters: Parameters)
    {
        Alamofire
            .request(StudentRouter.modifyStudent(id: studentID, parameters: parameters))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print(json)
                    if json["code"] == "200" {
                        DispatchQueue.main.async {
                            self.configureUIWith(json: json["data"])
                        }
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
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if let desVC = segue.destination as? ModifyInformationViewController {
            guard let json = json else {
                fatalError("prepareSegue json error!!!")
            }
            
            desVC.studentID = student.id
            if segue.identifier == "number" {
                desVC.modifyType = .number(oldNumber: json["number"].stringValue)
            } else if segue.identifier == "name" {
                desVC.modifyType = .name(oldName: json["name"].stringValue)
            } else if segue.identifier == "school" {
                desVC.modifyType = .school(oldSchool: json["school"].stringValue)
            } else if segue.identifier == "academy" {
                desVC.modifyType = .academy(oldAcademy: json["academy"].stringValue)
            } else if segue.identifier == "major" {
                desVC.modifyType = .major(oldMajor: json["major"].stringValue)
            } else if segue.identifier == "mail" {
                desVC.modifyType = .mail(oldMail: json["mail"].stringValue)
            }
        }
    }

    // MARK: - private 
    
    func getYear() -> Int
    {
        let year = Calendar.current.component(Calendar.Component.year, from: Date())
        return year
    }
    
}

