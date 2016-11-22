//
//  MineInfoCell.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

@IBDesignable class MineInfoCell: UIView
{

    @IBOutlet weak var leftLabel: UILabel!

    @IBOutlet weak var contentLabel: UILabel!
    
    @IBOutlet weak var seperateLine: UIView!
    
    @IBInspectable
    var leftText: String? {
        didSet {
            leftLabel.text = leftText
        }
    }
    
    @IBInspectable
    var contentText: String? {
        didSet {
            contentLabel.text = contentText
        }
    }
    
    @IBInspectable
    var isHideSeperateLine: Bool = false {
        didSet {
            seperateLine.isHidden = isHideSeperateLine
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
    }
    
}
