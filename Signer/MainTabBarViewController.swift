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
    }
    
    func addAllChildViewController()
    {
        // 首页
        addChildVC("HomePageViewController", title: "首页", image: "tab_homePage")
        // 聊天室
        addChildVC("ChatRoomViewController", title: "聊天室", image: "tab_chatRoom")
        // 签到
        addCenterButton(imageName: "tab_sign")
        // 通知
        addChildVC("NoticeViewController", title: "通知", image: "tab_notice")
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
            signButtonClicked()
        }
    }
    
    // 点击签到按钮
    func signButtonClicked()
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
    
}
