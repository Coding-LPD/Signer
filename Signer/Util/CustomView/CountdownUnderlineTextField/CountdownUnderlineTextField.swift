//
//  CountdownUnderlineTextField.swift
//  Signer
//
//  Created by Vernon on 16/9/6.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

protocol CountdownUnderlineTextFieldDelegate
{
    func countdownUnderlineTextFieldDidClickButton()
}

@IBDesignable class CountdownUnderlineTextField: UIView
{
    
    @IBOutlet weak var leftTextLabel: UILabel!
    @IBOutlet weak var underLine: UIView!
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var countdownButton: UIButton!

    var delegate: CountdownUnderlineTextFieldDelegate?
    
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
    
    @IBInspectable
    var buttonPlaceText: String? {
        didSet {
            countdownButton.setTitle(buttonPlaceText, for: .normal)
        }
    }
    
    @IBInspectable
    var countdownSeconds: Int? 
    
    var keyboardType: UIKeyboardType = .default {
        didSet {
            textField.keyboardType = keyboardType
        }
    }
    
    var text: String? {
        return textField.text
    }
    
    @IBAction func clickButtonAction()
    {
        startCountdown()
        delegate?.countdownUnderlineTextFieldDidClickButton()
    }
    
    func startCountdown()
    {
        countdownButton.start(withTime: countdownSeconds!, title: "重新发送", countDownTitle: "s")
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
    
    override func setValue(_ value: Any?, forUndefinedKey key: String) {
        if let value = value as? Int?, key == "countdownSeconds" {
            self.countdownSeconds = value
        }
    }
}

extension CountdownUnderlineTextField: UITextFieldDelegate
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


