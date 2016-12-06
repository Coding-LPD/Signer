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
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let courseId = courseId, let socket = socket else {
            fatalError("ChatRoomViewController初始值为空")
        }
        
        _ = messageInputView.rx.observeWeakly(CGFloat.self, "viewHeight").subscribe(onNext: { viewHeight in
            self.messageInputViewHeightConstraint.constant = viewHeight ?? 55
        })
        
        // 请求指定courseId聊天室的消息列表
        socket.emit("msg-list", courseId, 0, limitOfMsg)
        socket.on("msg-list") { data, ack in
         //   print("-------------msg-list: \(JSON(data))")
        }
        
        // 监听新的聊天信息的事件
        socket.emit("new-msg", courseId, Student().id, "", "")
        socket.on("new-msg") { data, ack in
          //  print("-------------new-msg: \(JSON(data))")
        }

        //courseId, studentId, content, teacherId（studentId, teacherId选择一个进行设置，另一个则为空字符串）
    }

}
