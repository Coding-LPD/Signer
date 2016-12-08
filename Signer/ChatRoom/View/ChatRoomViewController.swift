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

class ChatRoomViewController: UIViewController
{
    weak var socket: SocketIOClient?
    
    var courseId: String?

    let limitOfMsg = 18
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var messageInputView: MessageInputView!
    @IBOutlet weak var messageInputViewHeightConstraint: NSLayoutConstraint!
    
    var messages = [Message]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let courseId = courseId, let socket = socket else {
            fatalError("ChatRoomViewController初始值为空")
        }

        // 请求指定courseId聊天室的消息列表
        socket.emit("msg-list", courseId, 0, limitOfMsg)
        socket.on("msg-list") { data, ack in
            self.convertJSONToMessages(json: JSON(data))
        }

        // 监听新的聊天信息的事件
        socket.emit("new-msg", courseId, Student().id, "", "")
        socket.on("new-msg") { data, ack in
            print("-------------有消息: \(JSON(data))")
        }
        
        tableView.dataSource = self
        messageInputView.delegate = self
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
        
        tableView.reloadData()
    }

}

extension ChatRoomViewController: UITableViewDataSource
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageCell", for: indexPath)
        
        let msg = messages[indexPath.row]
//        if msg.avatarUrl.length > 0 {
//            cell.imageView?.sd_setImage(with: URL(string: msg.avatarUrl)!, placeholderImage: UIImage(named: Constant.defaultAvatar1Url))
//        }
        cell.textLabel?.text = msg.content
        cell.detailTextLabel?.text = msg.name + " - " + dateFormatter.string(from: msg.createdDate)
        
        return cell
    }
}

extension ChatRoomViewController: MessageInputDelegate
{
    func messageInputView(messageInputView: MessageInputView, clickSendButtonWith inputText: String)
    {
        socket!.emit("new-msg", courseId!, Student().id, inputText, "")
        
        messageInputView.setText(newText: "")
        
        let student = Student()
        let msg = Message(avatarUrl: student.avatarUrl, content: inputText, name: student.name, createdDate: Date())
        messages.append(msg)
        tableView.reloadData()
    }

    func messageInputView(messageInputView: MessageInputView, heightDidChangeTo viewHeight: CGFloat)
    {
        messageInputViewHeightConstraint.constant = viewHeight
    }

}
