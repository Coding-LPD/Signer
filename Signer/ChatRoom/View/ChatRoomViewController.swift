//
//  ChatRoomViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/6.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO
import SwiftyJSON
import JSQMessagesViewController

class ChatRoomViewController: JSQMessagesViewController
{
    weak var socket: SocketIOClient?
    
    var courseId: String?

    let limitOfMsg = 18

    var messages = [Message]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        senderId = Student().id
        senderDisplayName = Student().name
        
        showLoadEarlierMessagesHeader = true
        inputToolbar.contentView.leftBarButtonItem = nil
        
//        guard let courseId = courseId, let socket = socket else {
//            fatalError("ChatRoomViewController初始值为空")
//        }
//
//        // 请求指定courseId聊天室的消息列表
//        socket.emit("msg-list", courseId, 0, limitOfMsg)
//        socket.on("msg-list") { data, ack in
//            self.convertJSONToMessages(json: JSON(data))
//        }
//
//        // 监听新的聊天信息的事件
//        socket.emit("new-msg", courseId, Student().id, "", "")
//        socket.on("new-msg") { data, ack in
//            print("-------------有消息: \(JSON(data))")
//        }

    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()
    
    func convertJSONToMessages(json: JSON)
    {
        print("-------------消息列表: \(json)")
        
        messages.removeAll()
        for (_, msgJSON) in json[0]["data"] {
            let avatarUrl = msgJSON["avatar"].stringValue
            let content = msgJSON["content"].stringValue
            let name = msgJSON["name"].stringValue
            let createdDate = dateFormatter.date(from: msgJSON["createdAt"].stringValue)!
            
            let msg = Message(avatarUrl: avatarUrl, content: content, name: name, createdDate: createdDate)
            messages.insert(msg, at: 0)
        }

    }

}

extension ChatRoomViewController
{
    // MARK: - UICollectionView DataSource
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int
    {
        return messages.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell
    {
        let cell = super.collectionView.cellForItem(at: indexPath) as! JSQMessagesCollectionViewCell
        return cell
    }
    
    //MARK: - JSQMessagesViewController method overrides
    
    override func didPressSend(_ button: UIButton!, withMessageText text: String!, senderId: String!, senderDisplayName: String!, date: Date!)
    {
        JSQSystemSoundPlayer.jsq_playMessageSentSound()
        
    }
    
//    - (void)didPressSendButton:(UIButton *)button
//    withMessageText:(NSString *)text
//    senderId:(NSString *)senderId
//    senderDisplayName:(NSString *)senderDisplayName
//    date:(NSDate *)date
//    {
//    /**
//     *  Sending a message. Your implementation of this method should do *at least* the following:
//     *
//     *  1. Play sound (optional)
//     *  2. Add new id<JSQMessageData> object to your data source
//     *  3. Call `finishSendingMessage`
//     */
//    [JSQSystemSoundPlayer jsq_playMessageSentSound];
//    
//    JSQMessage *message = [[JSQMessage alloc] initWithSenderId:senderId
//    senderDisplayName:senderDisplayName
//    date:date
//    text:text];
//    
//    [self.demoData.messages addObject:message];
//    
//    [self finishSendingMessageAnimated:YES];
//    }

}

