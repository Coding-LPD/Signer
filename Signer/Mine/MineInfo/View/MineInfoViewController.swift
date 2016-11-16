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

class MineInfoViewController: UIViewController
{
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!

    let student = Student.sharedStudent
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        initUI()
    }
    
    func initUI()
    {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        
        avatarImageView.sd_setImage(with: URL(string: student.avatarUrl))
        nameLabel.text = student.name.isEmpty ? "用户名" : student.name
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "ModifyInformation" {
            if let desVC = segue.destination as? ModifyInformationViewController,
                let cell = tableView.cellForRow(at: tableView.indexPathForSelectedRow!) as? MineInfoCell {
                desVC.caption = cell.leftText
                desVC.content = cell.contentText
                desVC.studentID = student.id
                desVC.parameterName = cell.parameterName
                
            }
        }
    }
}

extension MineInfoViewController: UITableViewDataSource, UITableViewDelegate
{
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if section == 0 {
            return 3
        } else if section == 1 {
            return 3
        } else {
            return 3
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MineInfoCell", for: indexPath) as! MineInfoCell
        configureCell(cell, atIndexPath: indexPath)
        return cell
    }
    
    func configureCell(_ cell: MineInfoCell, atIndexPath indexPath: IndexPath)
    {
        if indexPath.section == 0 && indexPath.row == 0 {
            cell.configureCell(leftText: "学号", parameterName: "number", contentText: student.number, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 0 && indexPath.row == 1 {
            cell.configureCell(leftText: "姓名", parameterName: "name", contentText: student.name, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 0 && indexPath.row == 2 {
            cell.configureCell(leftText: "性别", parameterName: "gender", contentText: student.gender, placeText: "未填写", isLineHidden: true)
        } else if indexPath.section == 1 && indexPath.row == 0 {
            cell.configureCell(leftText: "学校", parameterName: "school", contentText: student.school, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            cell.configureCell(leftText: "学院", parameterName: "academy", contentText: student.academy, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 2 {
            cell.configureCell(leftText: "专业", parameterName: "major", contentText: student.major, placeText: "未填写", isLineHidden: true)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            cell.configureCell(leftText: "年级", parameterName: "grade", contentText: student.grade, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 2 && indexPath.row == 1 {
            cell.configureCell(leftText: "班级", parameterName: "class", contentText: student.classroom, placeText: "未填写", isLineHidden: false)
        } else if indexPath.section == 2 && indexPath.row == 2 {
            cell.configureCell(leftText: "邮箱", parameterName: "mail", contentText: student.mail, placeText: "未填写", isLineHidden: true)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        return 16.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if (indexPath.section == 0 && (indexPath.row == 0 || indexPath.row == 1)) ||
            (indexPath.section == 1) ||
            (indexPath.section == 2 && indexPath.row == 2) {
            performSegue(withIdentifier: "ModifyInformation", sender: nil)
        } else {
            
        }
    }
    
}
