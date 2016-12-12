//
//  SignRecordCalendarViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/29.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class SignRecordCalendarViewController: UIViewController
{
    var courseId: String?
    
    @IBOutlet weak var calendarView: CalendarView!
    @IBOutlet weak var calendarViewHeight: NSLayoutConstraint!
    
    @IBOutlet weak var signedTimeLabel: UILabel!
    @IBOutlet weak var unsignedTimeLabel: UILabel!
    
    var signedDates = [String]()
    var unsignedDates = [String]()
        
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        guard let courseId = courseId else {
            fatalError("SignRecordCalendarViewController没有初始值")
        }
        
        Alamofire
            .request(CourseRouter.requestSignRecord(courseId: courseId, studentId: Student().id))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取签到记录: \(json)")
                    if json["code"] == "200" {
                        self.getRecordFrom(json: json["data"])
                    } else {
                        fatalError("获取签到记录失败")
                    }
                case .failure(_):
                    self.view.makeToast("获取签到记录失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
        
        let backBarButton = UIBarButtonItem(image: UIImage(named: "Back"), style: .plain, target: self, action: #selector(dismissAction))
        navigationItem.leftBarButtonItem = backBarButton

        let components = NSCalendar.current.dateComponents([.month , .year], from: Date())
        calendarView.set(year: components.year!, month: components.month!)
        calendarView.delegate = self
    }
    
    func getRecordFrom(json: JSON)
    {
        for (_, recordJson) in json {
            if recordJson["tag"].boolValue == true {
                signedDates.append(recordJson["time"].stringValue)
            } else {
                unsignedDates.append(recordJson["time"].stringValue)
            }
        }
        
        calendarView.setMarkedDates(signedDates: signedDates, unsignedDates: unsignedDates)
        
        signedTimeLabel.text = "已签到\(signedDates.count)次"
        unsignedTimeLabel.text = "未签到\(unsignedDates.count)次"
    }
    
    func dismissAction()
    {
        dismiss(animated: true, completion: nil)
    }
}

extension SignRecordCalendarViewController: CalendarViewDelegate
{
    func calendarViewHeightChange(height: CGFloat)
    {
        calendarViewHeight.constant = height
    }
}
