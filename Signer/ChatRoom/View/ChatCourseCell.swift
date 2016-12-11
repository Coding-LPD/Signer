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
        newestMsgLabel.text = chatRoom.newestMsg != nil ? chatRoom.newestMsg : "没有人发言"
        newestMsgLabel.textColor = chatRoom.newestMsg != nil ? UIColor(netHex: 0x666666) : UIColor(netHex: 0x999999)
        timeIntervalLabel.text = Date().getTimeIntervalDescriptionTo(date: chatRoom.newestMsgDate)
        
        if let chatRoomTimeStampDict = UserDefaults.standard.dictionary(forKey: "chatRoomTimeStampDict"), let finalDate = chatRoomTimeStampDict[chatRoom.courseId] as? Date, let newestMsgDate = chatRoom.newestMsgDate {
            if newestMsgDate > finalDate {
                hasUpdateLabel.isHidden = false
            } else {
                hasUpdateLabel.isHidden = true
            }
        } else {
            hasUpdateLabel.isHidden = false
        }
        
    }
}

