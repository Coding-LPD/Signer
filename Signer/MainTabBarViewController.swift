//
//  MainTabBarViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class MainTabBarViewController: UITabBarController
{
    let centerButtonIndex = 2
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tabBar.tintColor = ThemeGreenColor
        
        addAllChildViewController()
        
        NotificationCenter.default.addObserver(self, selector: #selector(receiveNewNotice), name: NSNotification.Name(rawValue: Constant.receiveNewNoticeNotification), object: nil)
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    func addAllChildViewController()
    {
        // 首页
        addChildVC("HomePageViewController", title: "签到", image: "tab_homePage")
        // 聊天室
        addChildVC("ChatRoomNavigation", title: "聊天室", image: "tab_chatRoom")
        // 签到
        addCenterButton(imageName: "tab_sign")
        // 通知
        addChildVC("NoticeNavigation", title: "通知", image: "tab_notice")
        // 我的
        addChildVC("MineNavigation", title: "我的", image: "tab_mine")
    }
    
    func addChildVC(_ identifier: String, title: String?, image: String)
    {
        if let childVC = storyboard?.instantiateViewController(withIdentifier: identifier) {
            childVC.tabBarItem.title = title
            childVC.tabBarItem.image = UIImage(named: image)
            addChildViewController(childVC)
        }
    }
    
    func addCenterButton(imageName: String)
    {
        let placeHolderVC = UIViewController()
        placeHolderVC.view.backgroundColor = UIColor.white
        let buttonImage = UIImage(named: imageName)?.withRenderingMode(.alwaysOriginal)
        
        placeHolderVC.tabBarItem.image = buttonImage
        placeHolderVC.tabBarItem.tag = centerButtonIndex
        placeHolderVC.tabBarItem.imageInsets = UIEdgeInsetsMake(5, 0, -5, 0)
        
        addChildViewController(placeHolderVC)
    }
    
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem)
    {
        if item.tag == centerButtonIndex {
            signInAction()
        }
    }
    
    // 签到
    func signInAction()
    {
        guard let signNavigation = storyboard?.instantiateViewController(withIdentifier: "SignNavigation") as? UINavigationController,
            let signViewController = signNavigation.viewControllers[0] as? SignViewController else {
                return
        }
        
        let currentSelectedIndex = selectedIndex
        signViewController.dismissViewControllerBlock = { [weak self] in
            self?.selectedIndex = currentSelectedIndex
        }
        
        present(signNavigation, animated: true, completion: nil)
    }
    
    func receiveNewNotice()
    {
        if let unreadNoticeString = self.tabBar.items?[3].badgeValue, let unreadNoticeCount = Int(unreadNoticeString) {
            self.tabBar.items?[3].badgeValue = "\(unreadNoticeCount + 1)"
        } else {
            self.tabBar.items?[3].badgeValue = "1"
        }
    }

}
