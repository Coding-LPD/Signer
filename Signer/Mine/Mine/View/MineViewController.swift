//
//  MineViewController.swift
//  Signer
//
//  Created by FSQ on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class MineViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!

    override func viewDidLoad()
    {
        super.viewDidLoad()

        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.tableFooterView = UIView()
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        
        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
    }
    
    override func viewWillAppear(_ animated: Bool)
    {
        super.viewWillAppear(animated)
        tableView.reloadSections([0], with: .automatic)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        if let desVC = segue.destination as? RecordViewController {
            if segue.identifier == "SignRecord" {
                desVC.recordType = .sign
            } else if segue.identifier == "ChatRecord" {
                desVC.recordType = .chat
            }
        }
    }

}

extension MineViewController: UITableViewDataSource, UITableViewDelegate
{
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if section == 0 {
            return 1
        } else if section == 1 {
            return 2
        } else {
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "LeftAvatarCell", for: indexPath) as! LeftAvatarCell
            let student = Student()
            cell.configureCell(withAvatarUrl: student.avatarUrl, userName: student.name, id: "ID: \(student.phone)")
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MineLeftImageCell", for: indexPath) as! MineLeftImageCell
            configureSettingCell(cell, atIndexPath: indexPath)
            return cell
        }
    }
    
    func configureSettingCell(_ cell: MineLeftImageCell, atIndexPath indexPath: IndexPath)
    {
        if indexPath.section == 1 && indexPath.row == 0 {
            cell.configureCell(imageName: "sign", text: "我的签到", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            cell.configureCell(imageName: "speak", text: "我的发言", isLineHidden: false)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            cell.configureCell(imageName: "setting", text: "设置", isLineHidden: true)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if indexPath.section == 0 {
            return LeftAvatarCell.cellHeight
        } else {
            return MineLeftImageCell.cellHeight
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        return 16.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if indexPath.section == 1 && indexPath.row == 0 {
            performSegue(withIdentifier: "SignRecord", sender: nil)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            performSegue(withIdentifier: "ChatRecord", sender: nil)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            performSegue(withIdentifier: "Setting", sender: nil)
        }
    }
    
}
