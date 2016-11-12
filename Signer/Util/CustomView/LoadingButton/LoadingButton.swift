//
//  LoadingButton.swift
//  Signer
//
//  Created by Vernon on 2016/11/8.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

protocol LoadingButtonDelegate
{
    func didClickButton()
}

@IBDesignable class LoadingButton: UIView
{

    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    
    var delegate: LoadingButtonDelegate?
    
    @IBInspectable
    var buttonText: String? {
        didSet {
            button.setTitle(buttonText, for: .normal)
        }
    }

    var isEnabled = true {
        didSet {
            if isEnabled {
                button.alpha = 1.0
                button.isEnabled = true
            } else {
                button.alpha = 0.5
                button.isEnabled = false
            }
        }
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
        
        spinner.alpha = 0
    }
    
    @IBAction func clickButtonAction(_ sender: UIButton)
    {
        delegate?.didClickButton()
    }
    
    func startWaiting()
    {
        isEnabled = false
        
        spinner.alpha = 1.0
        spinner.startAnimating()
    }
    
    func stopWaiting()
    {
        isEnabled = true
        
        spinner.alpha = 0
        spinner.stopAnimating()
    }
    
}
