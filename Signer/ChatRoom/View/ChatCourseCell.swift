//
//  ChatCourseCell.swift
//  Signer
//
//  Created by Vernon on 2016/12/5.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class ChatCourseCell: UITableViewCell
{
    
    @IBOutlet weak var avatarImageView: UIImageView!
    
    @IBOutlet weak var nameLabel: UILabel!
    
    @IBOutlet weak var countLabel: UILabel!
    
    @IBOutlet weak var newestMsgLabel: UILabel!

    @IBOutlet weak var timeIntervalLabel: UILabel!
    
    @IBOutlet weak var hasUpdateLabel: UILabel!
    
    static let cellHeight: CGFloat = 85.0

}

extension ChatCourseCell
{
    func configureWith(chatRoom: ChatRoom)
    {
        if let avatarUrl = chatRoom.avatarUrl {
            if avatarUrl.length > 0 {
                avatarImageView.sd_setImage(with: URL(string: avatarUrl)!)
            }
        }
        nameLabel.text = chatRoom.name
        countLabel.text = "\(chatRoom.count)"
        newestMsgLabel.text = chatRoom.newestMsg
        timeIntervalLabel.text = Date().getTimeIntervalDescriptionTo(date: chatRoom.newestMsgDate)
    }
}

