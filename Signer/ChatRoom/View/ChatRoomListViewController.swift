//
//  ChatRoomListViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/8.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SocketIO
import SwiftyJSON
import DZNEmptyDataSet

class ChatRoomListViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!

    let isStudent = UserDefaults.standard.bool(forKey: "isStudent")
    
    var chatRooms = [ChatRoom]()
    
    lazy var socket: SocketIOClient = {
        let socket = SocketIOClient(socketURL: URL(string: SignUpRouter.baseSocketUrl)!, config: [.log(false), .nsp("/sign"), .forceNew(true), .reconnects(true)])
        return socket
    }()
    
    lazy var refreshControl: UIRefreshControl = {
        let refreshControl = UIRefreshControl()
        refreshControl.tintColor = UIColor(netHex: 0x97cc00)
        refreshControl.addTarget(self, action: #selector(requestRoomListFromService), for: .valueChanged)
        return refreshControl
    }()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        navigationController?.view.backgroundColor = UIColor.white
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.emptyDataSetSource = self
        tableView.emptyDataSetDelegate = self
        tableView.addSubview(refreshControl)

        socket.on("connect") {data, ack in
            self.requestRoomListFromService()
        }
        
        socket.on("room-list") {data, ack in
            self.refreshControl.endRefreshing()
            self.configureUIWith(json: JSON(data[0]))
        }
        
        socket.on("new-msg") { data, ack in
            self.requestRoomListFromService()
        }
        
        socket.connect()
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        requestRoomListFromService()
    }
    
    override func viewDidDisappear(_ animated: Bool)
    {
        super.viewDidDisappear(animated)
        
        refreshControl.endRefreshing()
    }
    
    func requestRoomListFromService()
    {
        if isStudent {
            self.socket.emit("room-list", Student().id, "")     // 获取学生的聊天室列表
        } else {
            self.socket.emit("room-list", "", Student().id)     // 获取老师的聊天室列表
        }
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()

    func configureUIWith(json: JSON)
    {
       // print("聊天室列表: \(json)")
        
        chatRooms.removeAll()
        
        if json["code"] == "200" {
            for (_, chatRoomJson) in json["data"] {
                let courseId = chatRoomJson["courseId"].stringValue
                let name = chatRoomJson["name"].stringValue
                let count = chatRoomJson["count"].intValue
                let avatarUrl = chatRoomJson["msg"]["avatar"].string
                let newestMsg = chatRoomJson["msg"]["content"].string
                var newestMsgDate: Date? = nil
                if let newestMsgDateString = chatRoomJson["msg"]["createdAt"].string {
                    newestMsgDate = dateFormatter.date(from: newestMsgDateString)
                }
                
                let chatRoom = ChatRoom(courseId: courseId, name: name, count: count, avatarUrl: avatarUrl, newestMsg: newestMsg, newestMsgDate: newestMsgDate)
                chatRooms.append(chatRoom)
                tableView.reloadData()
            }
        } else {
            view.makeToast("加载聊天室失败", duration: 1.0, position: .center)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "ChatRoom" {
            if let desVC = segue.destination as? ChatRoomViewController {
                desVC.socket = self.socket
                let chatRoom = chatRooms[tableView.indexPathForSelectedRow!.row]
                desVC.courseId = chatRoom.courseId
                desVC.title = chatRoom.name + "(\(chatRoom.count)人)"
                let cell = tableView.cellForRow(at: IndexPath(row: tableView.indexPathForSelectedRow!.row, section: 0)) as! ChatCourseCell
                cell.hasUpdateLabel.isHidden = true
            }
        }
    }
    
}

extension ChatRoomListViewController: UITableViewDataSource, UITableViewDelegate
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

extension ChatRoomListViewController: DZNEmptyDataSetSource, DZNEmptyDataSetDelegate
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
