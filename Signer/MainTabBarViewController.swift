//
//  MainTabBarViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO

class MainTabBarViewController: UITabBarController
{
    let centerButtonIndex = 2

    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tabBar.tintColor = ThemeGreenColor
        addAllChildViewController()
        
        connectToServer()
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        NotificationCenter.default.addObserver(self, selector: #selector(clearNoticeCountHandler), name: NSNotification.Name(rawValue: "ClearNoticeCountNotification"), object: nil)
    }
    
    override func viewDidDisappear(_ animated: Bool)
    {
        super.viewDidDisappear(animated)
        
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(rawValue: "ClearNoticeCountNotification"), object: nil)
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

    lazy var socket: SocketIOClient = {
        let socket = SocketIOClient(socketURL: URL(string: SignUpRouter.socketBaseUrl)!, config: [.log(false), .nsp("/sign"), .forceNew(true), .reconnects(true)])
        return socket
    }()

    var newNoticeCount = 0 {
        didSet {
            self.tabBar.items?[3].badgeValue = newNoticeCount == 0 ? nil : "\(newNoticeCount)"
        }
    }
    
    func clearNoticeCountHandler()
    {
        newNoticeCount = 0
    }
    
    func connectToServer()
    {
        socket.on("connect") {data, ack in
            self.socket.emit("student-in", Student().id)        // 触发此事件才能接收到通知
        }
        
        socket.on("notice") {data, ack in
            print("------------notice")
            self.newNoticeCount += 1
            
        }

        socket.connect()
    }
    
}
