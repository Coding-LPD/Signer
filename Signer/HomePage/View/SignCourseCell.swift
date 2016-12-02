//
//  SignCourseCell.swift
//  Signer
//
//  Created by Vernon on 2016/11/24.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SignCourseCell: UITableViewCell
{
    
    @IBOutlet weak var signCourseView: SignCourseView!
    
    static let heightForEmptyAvatar: CGFloat = 100
    static let heightForHaveAvatar: CGFloat = 138
}

extension SignCourseCell
{
    func configureWith(course: Course)
    {
        signCourseView.configureWith(course: course)
    }
}
