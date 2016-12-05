//
//  ChatRoomViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/8.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO
import SwiftyJSON
import DZNEmptyDataSet

class ChatRoomViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!

    var chatRooms = [ChatRoom]()
    
    lazy var socket: SocketIOClient = {
        let socket = SocketIOClient(socketURL: URL(string: "http://120.25.65.207:3000")!, config: [.log(false), .nsp("/sign"), .forceNew(true), .reconnects(true)])
        return socket
    }()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.emptyDataSetSource = self
        tableView.emptyDataSetDelegate = self

        socket.on("connect") {data, ack in
            self.socket.emit("room-list", Student().id)
        }
        
        socket.on("room-list") {data, ack in
//            print("------------room list: \(data)")
            self.configureUIWith(json: JSON(data[0]))
        }
        
        socket.connect()
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()

    func configureUIWith(json: JSON)
    {
        print("json: \(json)")
        
        if json["code"] == "200" {
            for (_, chatRoomJson) in json["data"] {
                let courseId = chatRoomJson["courseId"].stringValue
                let avatarUrl = chatRoomJson["msg"]["avatar"].stringValue
                let name = chatRoomJson["name"].stringValue
                let count = chatRoomJson["count"].intValue
                let newestMsg = chatRoomJson["msg"]["content"].stringValue
                let newestMsgDate = dateFormatter.date(from: chatRoomJson["msg"]["createdAt"].stringValue)!
                
                let chatRoom = ChatRoom(courseId: courseId, avatarUrl: avatarUrl, name: name, count: count, newestMsg: newestMsg, newestMsgDate: newestMsgDate)
                chatRooms.append(chatRoom)
                tableView.reloadData()
            }
        } else {
            view.makeToast("加载聊天室失败", duration: 1.0, position: .center)
        }
    }
    
}

extension ChatRoomViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return chatRooms.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ChatCourseCell", for: indexPath) as! ChatCourseCell
        cell.configureWith(chatRoom: chatRooms[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return ChatCourseCell.cellHeight
    }
}

// MARK: - empty table

extension ChatRoomViewController: DZNEmptyDataSetSource, DZNEmptyDataSetDelegate
{
    func image(forEmptyDataSet scrollView: UIScrollView!) -> UIImage!
    {
        return UIImage(named: "emptyRecord")
    }
    
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString!
    {
        let attributes = [NSFontAttributeName: UIFont.boldSystemFont(ofSize: 14.0),
                          NSForegroundColorAttributeName: UIColor(netHex: 0xbbbbbb)]

        return NSAttributedString(string: "暂时还木有记录. . .", attributes: attributes)
    }
    
    func emptyDataSetShouldAllowScroll(_ scrollView: UIScrollView!) -> Bool
    {
        return true
    }
}
