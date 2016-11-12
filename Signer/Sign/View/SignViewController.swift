//
//  SignViewController.swift
//  Signer
//
//  Created by FSQ on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SignViewController: UIViewController
{
    
    var dismissViewControllerBlock: (() -> ()) = {}

    override func viewDidLoad()
    {
        super.viewDidLoad()
    }
    
    
    @IBAction func doneAction(_ sender: UIBarButtonItem)
    {
        dismissViewControllerBlock()
        dismiss(animated: true, completion: nil)
    }

}
