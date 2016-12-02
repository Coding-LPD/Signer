//
//  NoticeViewController.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import DZNEmptyDataSet

class NoticeViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!

    var notices = [Notice]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.delegate = self
        tableView.dataSource = self
        
        Alamofire
            .request(StudentRouter.requestNotice(phone: Student().phone, type: 0))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取通知: \(json)")
                    if json["code"] == "200" {
                        self.configureUIWith(json: json["data"])
                    } else {
                        fatalError("获取通知出错")
                    }
                case .failure(_):
                    self.view.makeToast("获取通知失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()
    
    func configureUIWith(json: JSON)
    {
        notices.removeAll()
        
        for (_, noticeJson) in json {
            let courseName = noticeJson["courseName"].stringValue
            let signNumber = noticeJson["signNumber"].intValue
            let signState = SignState(num: noticeJson["signState"].intValue)!
            let distance = noticeJson["signDistance"].intValue
            let signDate = dateFormatter.date(from: noticeJson["signAt"].stringValue)!
            let confirmDate = dateFormatter.date(from: noticeJson["confirmAt"].stringValue)!
            
            let notice = Notice(courseName: courseName, signNumber: signNumber, signState: signState, distance: distance, signDate: signDate, confirmDate: confirmDate)
            notices.append(notice)
        }
        
        tableView.reloadData()
    }

}

extension NoticeViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return notices.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "NoticeCell", for: indexPath) as! NoticeCell
        cell.configureWith(notice: notices[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return NoticeCell.cellHeight
    }
}
