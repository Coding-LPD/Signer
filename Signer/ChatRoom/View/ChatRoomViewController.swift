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
import SDWebImage
import JSQMessagesViewController

class ChatRoomViewController: JSQMessagesViewController
{
    weak var socket: SocketIOClient?
    
    var courseId: String?

    var totalPageOfMessage = 0      // 总共加载了多少页消息
    let limitOfMsg = 18             // 分页加载消息的每页的消息数量
    var isLoadAllMessage = false    // 是否加载完所有的消息

    var messages = [Message]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        senderId = Student().id
        senderDisplayName = Student().name

        showLoadEarlierMessagesHeader = false
        inputToolbar.contentView.leftBarButtonItem = nil
        inputToolbar.contentView.rightBarButtonItem.setTitle("发送", for: .normal)
        inputToolbar.contentView.rightBarButtonItem.setTitleColor(UIColor(netHex: 0x97cc00), for: .normal)
        inputToolbar.contentView.rightBarButtonItem.setTitle("发送", for: .disabled)
        inputToolbar.contentView.textView.placeHolder = ""
        
        collectionView.addPullToRefresh { [unowned self] in
            self.loadMessageFromService()
        }
        collectionView.pullToRefreshView.setTitle("", forState: 0)
        collectionView.pullToRefreshView.setTitle("", forState: 1)
        
        guard let courseId = courseId, let socket = socket else {
            fatalError("ChatRoomViewController初始值为空")
        }

        loadMessageFromService()      // 加载第0页的消息

        // 监听新的聊天信息的事件
        socket.emit("new-msg", courseId, Student().id, "", "")
        socket.on("new-msg") { data, ack in
            self.receiveNewMessage(messageJSON: JSON(data))
        }
        
        socket.on("msg-list") { data, ack in
            self.receiveMessageList(messageListJSON: JSON(data))
        }
    }
    
    override func viewDidDisappear(_ animated: Bool)
    {
        super.viewDidDisappear(animated)
        
        var chatRoomTimeStampDict: [String: Date]!
        if let dict = UserDefaults.standard.dictionary(forKey: "chatRoomTimeStampDict") as? [String: Date] {
            chatRoomTimeStampDict = dict
        } else {
            chatRoomTimeStampDict = [String: Date]()
        }
        chatRoomTimeStampDict[courseId!] = Date()
        
        UserDefaults.standard.set(chatRoomTimeStampDict, forKey: "chatRoomTimeStampDict")
    }

    // 分页加载从服务器获取第page页的消息，每页limitOfMsg条消息
    func loadMessageFromService()
    {
        if isLoadAllMessage {
            
        } else {
            socket!.emit("msg-list", courseId!, totalPageOfMessage, limitOfMsg)
        }
    }

    func receiveMessageList(messageListJSON json: JSON)
    {
        print("-------------消息列表: \(json)")
        
        collectionView.pullToRefreshView.stopAnimating()
        
        var newMessages = [Message]()
        for (_, messageJSON) in json[0]["data"] {
            let message = convertJSONToMessage(messageJSON: messageJSON)
            newMessages.insert(message, at: 0)
        }
        
        totalPageOfMessage += 1
        if newMessages.count < limitOfMsg {
            isLoadAllMessage = true
            collectionView.showsPullToRefresh = false
        }
        
        messages.insert(contentsOf: newMessages, at: 0)
        collectionView.reloadData()
        
        let item = collectionView.numberOfItems(inSection: 0) - (totalPageOfMessage - 1) * limitOfMsg - 1
        scroll(to: IndexPath(item: item, section: 0), animated: false)
    }
    
    func receiveNewMessage(messageJSON json: JSON)
    {
        print("-------------有新消息: \(json)")
        
        let message = convertJSONToMessage(messageJSON: json[0]["data"])
        messages.append(message)
        finishReceivingMessage(animated: true)
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()
    
    func convertJSONToMessage(messageJSON: JSON) -> Message
    {
        let studentId = messageJSON["studentId"].stringValue
        let teacherId = messageJSON["teacherId"].stringValue
        let id = (studentId.length>0 ? studentId : teacherId)
        let name = messageJSON["name"].stringValue
        let avatarUrl = messageJSON["avatar"].string
        let content = messageJSON["content"].stringValue
        let createdDate = dateFormatter.date(from: messageJSON["createdAt"].stringValue)!
        
        return Message(senderId: id, senderDisplayName: name, avatarUrl: avatarUrl, text: content, date: createdDate)
    }

    func sendMessage(text: String)
    {
        socket!.emit("new-msg", courseId!, Student().id, text, "")
    }
    
    lazy var bubbleFactory: JSQMessagesBubbleImageFactory? = {
        let bubbleFactory = JSQMessagesBubbleImageFactory()
        return bubbleFactory
    }()
    
}

extension ChatRoomViewController
{
    // MARK: - JSQMessages CollectionView DataSource
    
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, messageDataForItemAt indexPath: IndexPath!) -> JSQMessageData!
    {
        return messages[indexPath.item]
    }

    // 气泡类型（接收/发送）
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, messageBubbleImageDataForItemAt indexPath: IndexPath!) -> JSQMessageBubbleImageDataSource!
    {
        let message = messages[indexPath.item]
        if message.senderId == senderId {
            return bubbleFactory?.outgoingMessagesBubbleImage(with: UIColor(netHex: 0x97cc00))
        }
        return bubbleFactory?.incomingMessagesBubbleImage(with: UIColor.jsq_messageBubbleLightGray())
    }

    // 头像
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, avatarImageDataForItemAt indexPath: IndexPath!) -> JSQMessageAvatarImageDataSource!
    {
        let avatarImage = UIImage(named: "default_avatar_1")
        let avatarImageView = JSQMessagesAvatarImageFactory.avatarImage(with: avatarImage, diameter: UInt(kJSQMessagesCollectionViewAvatarSizeDefault))
        return avatarImageView
    }
    
    // 每隔5条信息显示一次聊天时间
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, attributedTextForCellTopLabelAt indexPath: IndexPath!) -> NSAttributedString!
    {
        if indexPath.item % 5 == 0 {
            let message = messages[indexPath.item]
            return JSQMessagesTimestampFormatter.shared().attributedTimestamp(for: message.date)
        }
        
        return nil
    }
    
    // 聊天者昵称
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, attributedTextForMessageBubbleTopLabelAt indexPath: IndexPath!) -> NSAttributedString!
    {
        let currentMessage = messages[indexPath.item]
        if currentMessage.senderId == senderId {
            return nil
        }
        
        if indexPath.item - 1 > 0 {
            let previousMessage = messages[indexPath.item - 1]
            if previousMessage.senderId == currentMessage.senderId {
                return nil
            }
        }
        
        return NSAttributedString(string: currentMessage.senderDisplayName)
    }

    // MARK: - UICollectionView DataSource
    
    // 消息数量
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int
    {
        return messages.count
    }
    
    // 每条消息
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell
    {
        let cell = super.collectionView(collectionView, cellForItemAt: indexPath) as! JSQMessagesCollectionViewCell
        let message = messages[indexPath.item]
        if message.senderId == self.senderId {
            cell.textView.textColor = UIColor(netHex: 0xffffff)
        } else {
            cell.textView.textColor = UIColor(netHex: 0x333333)
        }
        
        
        if let avatarUrl = message.avatarUrl {
            if avatarUrl.length > 0 {
                cell.avatarImageView.sd_setImage(with: URL(string: avatarUrl), completed: { (image, error, cacheType, imageUrl) in
                    cell.avatarImageView.image = JSQMessagesAvatarImageFactory.circularAvatarImage(image, withDiameter: UInt(kJSQMessagesCollectionViewAvatarSizeDefault))
                })
            }
        }

        return cell
    }
    
    // MARK: - JSQMessages collection view flow layout delegate
    
    // 聊天时间Label高度
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, layout collectionViewLayout: JSQMessagesCollectionViewFlowLayout!, heightForCellTopLabelAt indexPath: IndexPath!) -> CGFloat
    {
        if indexPath.item % 5 == 0 {
            return kJSQMessagesCollectionViewCellLabelHeightDefault
        }
        return 0.0
    }
    
    // 聊天者昵称Label高度
    override func collectionView(_ collectionView: JSQMessagesCollectionView!, layout collectionViewLayout: JSQMessagesCollectionViewFlowLayout!, heightForMessageBubbleTopLabelAt indexPath: IndexPath!) -> CGFloat
    {
        let currentMessage = messages[indexPath.item]
        if currentMessage.senderId == senderId {
            return 0.0
        }
        
        if indexPath.item - 1 > 0 {
            let previousMessage = messages[indexPath.item - 1]
            if previousMessage.senderId == currentMessage.senderId {
                return 0.0
            }
        }
        
        return kJSQMessagesCollectionViewCellLabelHeightDefault
    }

    //MARK: - JSQMessagesViewController method overrides
    
    override func didPressSend(_ button: UIButton!, withMessageText text: String!, senderId: String!, senderDisplayName: String!, date: Date!)
    {
        let message = Message(senderId: senderId, senderDisplayName: senderDisplayName, avatarUrl: Student().avatarUrl, text: text, date: date)
        messages.append(message)
        sendMessage(text: text)
        finishSendingMessage(animated: true)
    }
}


