//
//  AppDelegate.swift
//  Signer
//
//  Created by Vernon on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO
import IQKeyboardManager
import AlamofireNetworkActivityIndicator

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate
{
    var window: UIWindow?
    
    lazy var mainStoryboard: UIStoryboard = {
        return UIStoryboard(name: "Main", bundle: nil)
    }()
    
    var isLogged: Bool {
        get {
            return UserDefaults.standard.bool(forKey: "isLogged")
        }
    }
    
    var isStudent: Bool {
        get {
            return UserDefaults.standard.bool(forKey: "isStudent")
        }
    }

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool
    {
        UITextField.appearance().tintColor = ThemeGreenColor
        UITextView.appearance().tintColor = ThemeGreenColor
        
        NetworkActivityIndicatorManager.shared.isEnabled = true

        IQKeyboardManager.shared().isEnableAutoToolbar = false
        IQKeyboardManager.shared().shouldResignOnTouchOutside = true
        
        if isLogged && isStudent {
            showStudentHomePage()
        } else if isLogged && !isStudent {
            showTeacherHomePage()
        } else {
            showLogInViewController()
        }

        return true
    }
    
    func applicationDidEnterBackground(_ application: UIApplication)
    {
        if isStudent {
            stopMonitorNewNotice()
        }
    }
    
    func applicationWillEnterForeground(_ application: UIApplication)
    {
        if isStudent {
            monitorNewNotice()
        }
    }
    
    func applicationWillTerminate(_ application: UIApplication)
    {
        stopMonitorNewNotice()
    }

    // 显示学生主界面
    func showStudentHomePage()
    {
        if let mainTabBarVC = mainStoryboard.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            monitorNewNotice()
            window?.rootViewController = mainTabBarVC
        }
    }
    
    lazy var socket: SocketIOClient = {
        let socket = SocketIOClient(socketURL: URL(string: SignUpRouter.baseSocketUrl)!, config: [.log(false), .nsp("/sign"), .forceNew(true), .reconnects(true)])
        return socket
    }()
    
    /// 学生监听着新的签到通知
    func monitorNewNotice()
    {
        socket.on("connect") {data, ack in
            self.socket.emit("student-in", Student().id)        // 触发此事件才能接收到通知
        }
        
        socket.on("notice") {data, ack in
            NotificationCenter.default.post(name: NSNotification.Name(rawValue: Constant.receiveNewNoticeNotification), object: nil)
        }
        
        socket.connect()
    }
    
    /// 学生不再监听着新的签到通知
    func stopMonitorNewNotice()
    {
        socket.removeAllHandlers()
        socket.disconnect()
    }
    
    // 显示教师主界面
    func showTeacherHomePage()
    {
        let tabBarVC = UITabBarController()
        tabBarVC.tabBar.tintColor = ThemeGreenColor
        
        let chatRoomVC = mainStoryboard.instantiateViewController(withIdentifier: "ChatRoomNavigation")
        chatRoomVC.tabBarItem.title = "聊天室"
        chatRoomVC.tabBarItem.image = UIImage(named: "tab_chatRoom")
        tabBarVC.addChildViewController(chatRoomVC)
        
        let settingVC = mainStoryboard.instantiateViewController(withIdentifier: "SettingViewController")
        settingVC.tabBarItem.title = "设置"
        settingVC.tabBarItem.image = UIImage(named: "tab_mine")
        let navigationVC = UINavigationController(rootViewController: settingVC)
        navigationVC.navigationBar.tintColor = UIColor(netHex: 0x666666)
        navigationVC.view.backgroundColor = UIColor.white
        tabBarVC.addChildViewController(navigationVC)
        
        window?.rootViewController = tabBarVC
    }
    
    // 显示登录界面
    func showLogInViewController()
    {
        if let logInVC = mainStoryboard.instantiateViewController(withIdentifier: "LogInViewController") as? LogInViewController {
            window?.rootViewController = logInVC
        }
    }
}

