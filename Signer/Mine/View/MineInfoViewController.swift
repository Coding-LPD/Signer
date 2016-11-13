//
//  MineInfoViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class MineInfoViewController: UIViewController
{
    
    @IBOutlet weak var tableView: UITableView!

    let student = Student.currentStudent()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
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
            cell.configureCell(leftText: "学号", contentText: student.number ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 0 && indexPath.row == 1 {
            cell.configureCell(leftText: "姓名", contentText: student.name ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 0 && indexPath.row == 2 {
            cell.configureCell(leftText: "性别", contentText: student.gender ?? "未填写", isLineHidden: true)
        } else if indexPath.section == 1 && indexPath.row == 0 {
            cell.configureCell(leftText: "学校", contentText: student.school ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            cell.configureCell(leftText: "学院", contentText: student.academy ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 2 {
            cell.configureCell(leftText: "专业", contentText: student.major ?? "未填写", isLineHidden: true)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            cell.configureCell(leftText: "年级", contentText: student.grade ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 2 && indexPath.row == 1 {
            cell.configureCell(leftText: "班级", contentText: student.classroom ?? "未填写", isLineHidden: false)
        } else if indexPath.section == 2 && indexPath.row == 2 {
            cell.configureCell(leftText: "邮箱", contentText: student.mail ?? "未填写", isLineHidden: true)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        return 16.0
    }
    
}
