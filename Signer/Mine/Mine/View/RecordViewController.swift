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
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let recordType = recordType else {
            fatalError("RecordViewController没有初始值")
        }
        
        let components = NSCalendar.current.dateComponents([.year, .month , .day], from: Date())
        let year = components.year!
        let month = components.month!
        let day = components.day!
        calendarView.set(year: year, month: month)
        calendarView.delegate = self
        
        dateLabel.text = "\(month)月\(day)日"
        
        switch recordType {
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
                    print("获取\(dateString)的签到记录: \(json)")
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
                    print("获取\(dateString)的发言记录: \(json)")
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
                        
                    } else {
                        fatalError("获取\(dateString)日期的签到记录失败")
                    }
                case .failure:
                    break
                }
            }
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

                    } else {
                        fatalError("获取\(dateString)日期的发言记录失败")
                    }
                case .failure:
                    break
                }
            }
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
        
        switch recordType! {
        case .sign:
            requestSignAt(dateString: "\(year)-\(month)-\(day)")
        case .chat:
            requestChatAt(dateString: "\(year)-\(month)-\(day)")
        }
    }
}


