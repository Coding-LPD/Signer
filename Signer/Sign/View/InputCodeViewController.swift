//
//  InputCodeViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/26.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class InputCodeViewController: UIViewController
{
    
    @IBOutlet weak var sixCodeStackView: UIStackView!

    var dismissSignVCDelegate: dismissSignViewControllerDelegate?
    
    var sixNumbers = ""
    
    weak var weakSignVC: SignViewController?

    @IBAction func inputNumberAction(_ button: UIButton)
    {
        if button.tag == 10 {
            if sixNumbers.length > 0 {
                sixNumbers.remove(at: sixNumbers.index(before: sixNumbers.endIndex))
            }
        } else {
            sixNumbers = sixNumbers.appending("\(button.tag)")
        }
        
        for index in 0..<sixNumbers.length {
            let label = sixCodeStackView.arrangedSubviews[index] as! UILabel
            label.text = sixNumbers[index]
            label.textColor = UIColor(netHex: 0x97cc00)
        }
        for index in sixNumbers.length..<6 {
            let label = sixCodeStackView.arrangedSubviews[index] as! UILabel
            label.text = "⦁"
            label.textColor = UIColor(netHex: 0xD8D8D8)
        }
        
        if sixNumbers.length == 6 {
            performSegue(withIdentifier: "showCourseDetail", sender: nil)
        }
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        dismissSignVCDelegate?.dismissSignVC()
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "showCourseDetail" {
            if let desVC = segue.destination as? CourseDetailViewController {
                desVC.courseQRCode = sixNumbers
                desVC.weakSignVC = weakSignVC
                desVC.dismissSignVCDelegate = weakSignVC
            }
        }
    }
}
