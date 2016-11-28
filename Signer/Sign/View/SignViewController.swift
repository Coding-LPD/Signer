//
//  SignViewController.swift
//  Signer
//
//  Created by FSQ on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

protocol dismissSignViewControllerDelegate
{
    func dismissSignVC()
}

class SignViewController: UIViewController, dismissSignViewControllerDelegate
{
    
    @IBOutlet weak var scanView: UIView!
    @IBOutlet weak var inputBarButton: UIBarButtonItem!
    
    lazy var scanner: MTBBarcodeScanner? = {
        let scanner = MTBBarcodeScanner(previewView: self.scanView)
        return scanner
    }()
    
    var dismissViewControllerBlock: (() -> ()) = {}

    private var courseQRCode: String?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        inputBarButton.setTitleTextAttributes([NSForegroundColorAttributeName: UIColor(netHex: 0x97cc00)], for: .normal)
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        MTBBarcodeScanner.requestCameraPermission { [weak self] (isSuccess) in
            if isSuccess {
                self?.scanner?.startScanning(resultBlock: { codes in
                    if let code = (codes as! [AVMetadataMachineReadableCodeObject]?)?.first {
                        self?.courseQRCode = code.stringValue
                        self?.performSegue(withIdentifier: "showCourseDetail", sender: nil)
                        self?.scanner?.stopScanning()
                    }
                }, error: nil)
            } else {
                let alert = UIAlertController(title: "没有相机权限", message: "前往设置打开应用相机权限或者点击右上角输入手动输入签到码", preferredStyle: .alert)
                let doneAction = UIAlertAction(title: "确定", style: .cancel, handler: nil)
                alert.addAction(doneAction)
                self?.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    override func viewWillDisappear(_ animated: Bool)
    {
        self.scanner?.stopScanning()
        super.viewWillDisappear(animated)
    }
    
    
    @IBAction func doneAction(_ sender: UIBarButtonItem)
    {
        dismissViewControllerBlock()
        dismiss(animated: true, completion: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "showCourseDetail" {
            if let desVC = segue.destination as? CourseDetailViewController {
                desVC.courseQRCode = courseQRCode
                desVC.dismissSignVCDelegate = self
                desVC.weakSignVC = self
            }
        } else if segue.identifier == "inputCode" {
            if let desVC = segue.destination as? InputCodeViewController {
                desVC.weakSignVC = self
                desVC.dismissSignVCDelegate = self
            }
        }
    }
    
    func dismissSignVC()
    {
        dismissViewControllerBlock()
        dismiss(animated: true, completion: nil)
    }

}
