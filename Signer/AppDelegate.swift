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
        
        let isLogIn = UserDefaults.standard.bool(forKey: "isLogged")
        let isStudent = UserDefaults.standard.bool(forKey: "isStudent")
        
        if isLogIn && isStudent {
            showStudentHomePage()
        } else if isLogIn && !isStudent {
            showTeacherHomePage()
        }

        return true
    }

    // 显示学生主界面
    func showStudentHomePage()
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let mainTabBarVC = storyboard.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            window?.rootViewController = mainTabBarVC
        }
    }
    
    // 显示教师主界面
    func showTeacherHomePage()
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        if let mainTabBarVC = storyboard.instantiateViewController(withIdentifier: "MainTabBarViewController") as? MainTabBarViewController {
            window?.rootViewController = mainTabBarVC
        }
    }
}

