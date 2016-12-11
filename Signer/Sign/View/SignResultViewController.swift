//
//  SignResultViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/26.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SignResultViewController: UIViewController
{
    var distance: Int?
    var battery: Int?
    
    var dismissSignVCDelegate: dismissSignViewControllerDelegate?
    
    @IBOutlet weak var distanceLabel: UILabel!
    @IBOutlet weak var batteryLabel: UILabel!

    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let distance = distance, let battery = battery else {
            fatalError("SignResultViewController没有初始值")
        }
        
        distanceLabel.text = "\(distance)m"
        batteryLabel.text = "\(battery)%"
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        dismissSignVCDelegate?.dismissSignVC()
    }
}
