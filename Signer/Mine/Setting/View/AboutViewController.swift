//
//  AboutViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class AboutViewController: UIViewController
{

    @IBOutlet weak var versionLabel: UILabel!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        if let version = Bundle.main.infoDictionary?["CFBundleVersion"] as? String {
            versionLabel.text = "版本号(\(version))"
        }
    }

    @IBAction func detectUpdateAction(_ sender: UIButton)
    {
    }
    
    @IBAction func websiteAction(_ sender: UIButton) {
    }
    

}
