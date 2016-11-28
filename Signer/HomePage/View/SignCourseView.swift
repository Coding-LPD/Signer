//
//  SignCourseView.swift
//  Signer
//
//  Created by Vernon on 2016/11/24.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

@IBDesignable class SignCourseView: UIView
{
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var numberLabel: UILabel!
    @IBOutlet weak var avatarStackView: UIStackView!
    
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

extension SignCourseView
{
    func configureWith(course: Course)
    {
        
        nameLabel.text = course.name
        numberLabel.text = "\(course.signedNumber)人参与签到"
        for index in 0..<course.avatarUrls.count {
            let imageView = avatarStackView.arrangedSubviews[index] as! UIImageView
            imageView.sd_setImage(with: URL(string: course.avatarUrls[index])!)
        }
        for index in course.avatarUrls.count..<6 {
            avatarStackView.arrangedSubviews[index].isHidden = true
        }
    }
}
