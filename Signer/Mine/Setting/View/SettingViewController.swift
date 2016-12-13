//
//  SettingViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/10.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SettingViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        
        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
    }
    
    func clearCache()
    {
        view.makeToastActivity(.center)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) {
            self.view.hideToastActivity()
            self.view.makeToast("清除成功", duration: 0.3, position: .center)
        }
    }
}

extension SettingViewController: UITableViewDataSource, UITableViewDelegate
{
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return 4
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if section == 0 {
            return 1
        } else if section == 1 {
            return 2
        } else if section == 2 {
            return 1
        } else {
            return 1
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SettingCell", for: indexPath) as! SettingCell
        configureCell(cell, atIndexPath: indexPath)
        return cell
    }
    
    func configureCell(_ cell: SettingCell, atIndexPath indexPath: IndexPath)
    {
        if indexPath.section == 0 && indexPath.row == 0 {
            cell.configureCell(contentText: "修改密码", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 0 {
            cell.configureCell(contentText: "关于Signer", isLineHidden: false)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            cell.configureCell(contentText: "意见反馈", isLineHidden: true)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            cell.configureCell(contentText: "清理缓存", isLineHidden: true)
        } else if indexPath.section == 3 && indexPath.row == 0 {
            cell.configureCell(contentText: "退出登录", isLineHidden: true)
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    {
        return 16.0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if indexPath.section == 0 && indexPath.row == 0 {
            performSegue(withIdentifier: "modifyPassword", sender: nil)
        } else if indexPath.section == 1 && indexPath.row == 0 {
            performSegue(withIdentifier: "About", sender: nil)
        } else if indexPath.section == 1 && indexPath.row == 1 {
            performSegue(withIdentifier: "feedback", sender: nil)
        } else if indexPath.section == 2 && indexPath.row == 0 {
            clearCache()
        } else if indexPath.section == 3 && indexPath.row == 0 {
            let actionSheet = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
            let logOutAction = UIAlertAction(title: "退出登录", style: .destructive, handler: { (action) in
                let userDefaults = UserDefaults.standard
                userDefaults.set(false, forKey: "isLogged")
                userDefaults.synchronize()
                if let logInVC = self.storyboard?.instantiateViewController(withIdentifier: "LogInViewController") as? LogInViewController {
                    self.present(logInVC, animated: true, completion: nil)
                }
            })
            let cancelAction = UIAlertAction(title: "取消", style: .cancel, handler: nil)
            actionSheet.addAction(logOutAction)
            actionSheet.addAction(cancelAction)
            present(actionSheet, animated: true, completion: nil)
        }
    }
}
