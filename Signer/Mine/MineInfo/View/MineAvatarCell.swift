//
//  MineAvatarCell.swift
//  Signer
//
//  Created by Vernon on 2016/11/21.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

@IBDesignable class MineAvatarCell: UIView
{
    
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    
    @IBInspectable
    var avatarImage: UIImage? {
        didSet {
            avatarImageView.image = avatarImage
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
