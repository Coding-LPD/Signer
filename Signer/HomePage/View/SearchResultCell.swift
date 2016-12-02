//
//  SearchResultCell.swift
//  Signer
//
//  Created by Vernon on 2016/12/1.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SearchResultCell: UITableViewCell
{
    
    @IBOutlet weak var courseView: SignCourseView!

    static let heightForEmptyAvatar: CGFloat = 100
    static let heightForHaveAvatar: CGFloat = 138
}

extension SearchResultCell
{
    func configureWith(course: Course)
    {
        courseView.configureWith(course: course)
    }
}
