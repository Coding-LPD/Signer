//
//  AppDelegate.swift
//  Signer
//
//  Created by Vernon on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO
import CocoaAsyncSocket
import AlamofireNetworkActivityIndicator

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate
{
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool
    {
        UITextField.appearance().tintColor = ThemeGreenColor
        
        NetworkActivityIndicatorManager.shared.isEnabled = true
        
        if checkLoggedStatus() {
            showHomePage()
        }
        
        connectToServer()
        
        return true
    }

    // 检查是否已经登录
    func checkLoggedStatus() -> Bool
    {
        let userDefaults = UserDefaults.standard
        return userDefaults.bool(forKey: "isLogged")
    }
    
    // 显示主界面
    func showHomePage()
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let mainTabBarVC = storyboard.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            window?.rootViewController = mainTabBarVC
        }
    }
    
    let socket = SocketIOClient(socketURL: URL(string: "http://120.25.65.207:3000/sign:3000")!, config: [.log(false), .forceNew(true), .reconnects(true), .forcePolling(true)])
    
    func connectToServer()
    {
        socket.on("connect") {data, ack in
            print("------------socket connected")
            
            self.socket.emit("student-in", Student().id)
        }
        
        socket.on("notice") {data, ack in
            print("------------notice")
        }
        
        socket.connect()
        
    }

}

