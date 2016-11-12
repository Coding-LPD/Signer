//
//  UnderlineTextField.swift
//  Signer
//
//  Created by Vernon on 16/9/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

@IBDesignable class UnderlineTextField: UIView
{

    @IBOutlet weak var leftTextLabel: UILabel!
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var underLine: UIView!
        
    @IBInspectable
    var leftText: String? {
        didSet {
            leftTextLabel.text = leftText
        }
    }
    
    @IBInspectable
    var placeHolder: String? {
        didSet {
            textField.placeholder = placeHolder
        }
    }
    
    /// 键盘类型
    var keyboardType: UIKeyboardType = .default {
        didSet {
            textField.keyboardType = keyboardType
        }
    }
    
    /// 是否是密码输入框
    var isSecureTextEntry = false {
        didSet {
            textField.isSecureTextEntry = isSecureTextEntry
        }
    }
    
    var text: String? {
        return textField.text
    }
    
    override init(frame: CGRect)
    {
        super.init(frame: frame)
        
        loadViewFromNib()
    }
    
    required init?(coder aDecoder: NSCoder)
    {
        super.init(coder: aDecoder)
        
        loadViewFromNib()
    }
    
    func loadViewFromNib()
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: String(describing: type(of: self)), bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        view.frame = bounds
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(view)
        
        textField.delegate = self
    }
    
}

extension UnderlineTextField: UITextFieldDelegate
{
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        underLine.backgroundColor = ThemeGreenColor
    }
    
    func textFieldDidEndEditing(_ textField: UITextField)
    {
        underLine.backgroundColor = UIColor(netHex: 0xdddddd)
    }
}
