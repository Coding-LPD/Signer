//
//  Message.swift
//  Signer
//
//  Created by Vernon on 2016/12/8.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import JSQMessagesViewController

class Message: JSQMessage
{
    var avatarUrl: String?

    init(senderId: String, senderDisplayName: String, avatarUrl: String?, text: String, date: Date)
    {
        super.init(senderId: senderId, senderDisplayName: senderDisplayName, date: date, text: text)
        self.avatarUrl = avatarUrl
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
