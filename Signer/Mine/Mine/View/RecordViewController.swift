//
//  RecordViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

struct Record
{
    var leftText: String
    var rightText: String
}

enum RecordType
{
    case sign, chat
}

class RecordViewController: UIViewController
{
    @IBOutlet weak var calendarView: CalendarView!
    @IBOutlet weak var calendarViewHeight: NSLayoutConstraint!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!

    var recordType: RecordType?

    var recordDates = [String]()
    
    var records = [Record]()
    
    let emptySignRecord = Record(leftText: "暂无签到", rightText: "")
    let emptyChatRecord = Record(leftText: "暂无发言", rightText: "")
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let _ = recordType else {
            fatalError("RecordViewController没有初始值")
        }

        tableView.dataSource = self
        tableView.delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        let components = NSCalendar.current.dateComponents([.year, .month , .day], from: Date())
        let year = components.year!
        let month = components.month!
        let day = components.day!
        calendarView.set(year: year, month: month)
        calendarView.delegate = self
        
        dateLabel.text = "\(month)月\(day)日"
        
        switch recordType! {
        case .sign:
            title = "我的签到"
            requestSignDatesInMonth(dateString: "\(year)-\(month)")
            requestSignAt(dateString: "\(year)-\(month)-\(day)")
        case .chat:
            title = "我的发言"
            requestChatDatesInMonth(dateString: "\(year)-\(month)")
            requestChatAt(dateString: "\(year)-\(month)-\(day)")
        }
    }
    
    // 获取形如"2016-12"的月份的签到日期
    func requestSignDatesInMonth(dateString: String)
    {
        Alamofire
            .request(StudentRouter.requestSignDatesInMonth(id: Student().id, date: dateString))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                //    print("获取\(dateString)的签到日期: \(json)")
                    if json["code"] == "200" {
                        self.getRecordDatesFrom(json: json["data"])
                    } else {
                        fatalError("获取签到记录失败")
                    }
                case .failure:
                    self.view.makeToast("获取签到记录失败，检查网络连接", duration: 0.5, position: .center)
                }
            }
    }
    
    // 获取形如"2016-12"的月份的发言日期
    func requestChatDatesInMonth(dateString: String)
    {
        Alamofire
            .request(StudentRouter.requestChatDatesInMonth(id: Student().id, date: dateString))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                //    print("获取\(dateString)的发言日期: \(json)")
                    if json["code"] == "200" {
                        self.getRecordDatesFrom(json: json["data"])
                    } else {
                        fatalError("获取发言记录失败")
                    }
                case .failure:
                    self.view.makeToast("获取发言记录失败，检查网络连接", duration: 0.5, position: .center)
                }
        }
    }

    func getRecordDatesFrom(json: JSON)
    {
        for (_, dateJSON) in json {
            recordDates.append(dateJSON.stringValue)
        }
        
        calendarView.setMarkedDates(signedDates: recordDates, unsignedDates: [])
    }
    
    // 获取形如"2016-12-12"的日期的签到课程
    func requestSignAt(dateString: String)
    {
        Alamofire
            .request(StudentRouter.requestSignInDate(id: Student().id, date: dateString))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取\(dateString)的签到记录: \(json)")
                    if json["code"] == "200" {
                        self.convertSignRecordsAtDateFrom(json: json["data"])
                    } else {
                        fatalError("获取\(dateString)日期的签到记录失败")
                    }
                case .failure:
                    break
                }
            }
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()

    func convertSignRecordsAtDateFrom(json: JSON)
    {
        records.removeAll()
        
        for (_, recordJSON) in json {
            let confirmAt = recordJSON["confirmAt"].stringValue
            let confirmDate = dateFormatter.date(from: confirmAt)!
            let dateComponents = NSCalendar.current.dateComponents([.hour, .minute], from: confirmDate)
            let hour = dateComponents.hour!
            let minute = dateComponents.minute!
            let courseName = recordJSON["courseName"].stringValue
            let record = Record(leftText: "\(hour):\(minute)", rightText: courseName)
            records.append(record)
        }
        
        if records.isEmpty {
            records.append(emptySignRecord)
        }
        
        tableView.reloadData()
    }
    
    // 获取形如"2016-12-12"的日期的发言
    func requestChatAt(dateString: String)
    {
        Alamofire
            .request(StudentRouter.requestChatInDate(id: Student().id, date: dateString))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取\(dateString)的发言记录: \(json)")
                    if json["code"] == "200" {
                        self.convertChatRecordsAtDateFrom(json: json["data"])
                    } else {
                        fatalError("获取\(dateString)日期的发言记录失败")
                    }
                case .failure:
                    break
                }
            }
    }

    func convertChatRecordsAtDateFrom(json: JSON)
    {
        records.removeAll()
        
        for (_, recordJSON) in json {
            let msgCount = recordJSON["msgCount"].intValue
            let courseName = recordJSON["courseName"].stringValue
            let record = Record(leftText: courseName, rightText: "\(msgCount)条发言")
            records.append(record)
        }
        
        if records.isEmpty {
            records.append(emptyChatRecord)
        }
        
        tableView.reloadData()
    }
  
}

extension RecordViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return records.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "RecordCell", for: indexPath) as! RecordCell
        let record = records[indexPath.row]
        cell.timeLabel.text = record.leftText
        cell.contentLabel.text = record.rightText
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return 30.0
    }
}

extension RecordViewController: CalendarViewDelegate
{
    func calendarViewHeightChange(height: CGFloat)
    {
        calendarViewHeight.constant = height
    }
    
    func calendarViewChangeDateTo(month: Int, year: Int)
    {
        switch recordType! {
        case .sign:
            requestSignDatesInMonth(dateString: "\(year)-\(month)")
        case .chat:
            requestChatDatesInMonth(dateString: "\(year)-\(month)")
        }

    }
    
    func calendarViewDidClickDate(day: Int, month: Int, year: Int)
    {
        dateLabel.text = "\(month)月\(day)日"

        let monthString = month < 10 ? "0\(month)" : "\(month)"
        let dayString = day < 10 ? "0\(day)" : "\(day)"
        let dateString = "\(year)-\(monthString)-\(dayString)"
        
        print("点击\(dateString)")
        
        switch recordType! {
        case .sign:
            requestSignAt(dateString: dateString)
        case .chat:
            requestChatAt(dateString: dateString)
        }
    }
}


