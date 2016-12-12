//
//  AppDelegate.swift
//  Signer
//
//  Created by Vernon on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import IQKeyboardManager
import AlamofireNetworkActivityIndicator

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate
{
    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool
    {
        UITextField.appearance().tintColor = ThemeGreenColor
        UITextView.appearance().tintColor = ThemeGreenColor
        
        NetworkActivityIndicatorManager.shared.isEnabled = true

        IQKeyboardManager.shared().isEnableAutoToolbar = false
        IQKeyboardManager.shared().shouldResignOnTouchOutside = true
        
        if checkLoggedStatus() {
            showHomePage()
        }

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
}

