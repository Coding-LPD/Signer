//
//  NoticeCell.swift
//  Signer
//
//  Created by Vernon on 2016/12/2.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class NoticeCell: UITableViewCell
{
    @IBOutlet weak var typeImageView: UIImageView!

    @IBOutlet weak var courseNameLabel: UILabel!

    @IBOutlet weak var distanceLabel: UILabel!
    
    @IBOutlet weak var numberOfPersonLabel: UILabel!
    
    @IBOutlet weak var dateLabel: UILabel!
    
    @IBOutlet weak var isConfirmLabel: UILabel!
    
    @IBOutlet weak var timeIntervalLabel: UILabel!
    
    static let cellHeight: CGFloat = 110
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        return dateFormatter
    }()
}

extension NoticeCell
{
    func configureWith(notice: Notice)
    {
        typeImageView.image = notice.signState == .confirm ? UIImage(named: "confirm") : UIImage(named: "reject")
        courseNameLabel.text = notice.courseName
        distanceLabel.text = "\(notice.distance)m"
        numberOfPersonLabel.text = "\(notice.signNumber)人"
        isConfirmLabel.text = notice.signState == .confirm ? "同意签到" : "拒绝签到"
        dateLabel.text = dateFormatter.string(from: notice.signDate)
        timeIntervalLabel.text = Date().getTimeIntervalDescriptionTo(date: notice.confirmDate)
    }
}
