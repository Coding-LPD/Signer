//
//  CourseDetailViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/25.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class CourseDetailViewController: UIViewController, BMKLocationServiceDelegate
{
    var courseQRCode: String?
    
    @IBOutlet weak var courseLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var sectionLabel: UILabel!
    @IBOutlet weak var classroomLabel: UILabel!
    @IBOutlet weak var teacherLabel: UILabel!
    
    @IBOutlet weak var avatarImageView1: UIImageView!
    @IBOutlet weak var avatarImageView2: UIImageView!
    @IBOutlet weak var avatarImageView3: UIImageView!
    @IBOutlet weak var avatarImageView4: UIImageView!
    @IBOutlet weak var avatarImageView5: UIImageView!
    @IBOutlet weak var avatarImageView6: UIImageView!
    @IBOutlet weak var avatarImageView7: UIImageView!
    @IBOutlet weak var avatarImageView8: UIImageView!
    @IBOutlet weak var avatarImageView9: UIImageView!
    @IBOutlet weak var avatarImageView10: UIImageView!
    
    @IBOutlet weak var studentNameLabel1: UILabel!
    @IBOutlet weak var studentNameLabel2: UILabel!
    @IBOutlet weak var studentNameLabel3: UILabel!
    @IBOutlet weak var studentNameLabel4: UILabel!
    @IBOutlet weak var studentNameLabel5: UILabel!
    @IBOutlet weak var studentNameLabel6: UILabel!
    @IBOutlet weak var studentNameLabel7: UILabel!
    @IBOutlet weak var studentNameLabel8: UILabel!
    @IBOutlet weak var studentNameLabel9: UILabel!
    @IBOutlet weak var studentNameLabel10: UILabel!
    
    @IBOutlet weak var studentPadHeight: NSLayoutConstraint!
    @IBOutlet weak var scrollViewHeight: NSLayoutConstraint!
    
    var avartarImageViewArray = [UIImageView]()
    var nameLabelArray = [UILabel]()
    
    weak var weakSignVC: SignViewController?
    var dismissSignVCDelegate: dismissSignViewControllerDelegate?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let courseQRCode = courseQRCode else {
            fatalError("CourseDetailViewController courseQRCode没有初始值")
        }
        
        view.makeToastActivity(.center)
        
        Alamofire
            .request(SignRouter.scanQRCode(code: courseQRCode))
            .responseJSON { (response) in
                self.view.hideToastActivity()
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取课程详情: \(json)")
                    if json["code"] == "200" {
                        self.configureUIWith(json: json["data"])
                    } else {
                        let alert = UIAlertController(title: "课程不存在", message: nil, preferredStyle: .alert)
                        let doneAction = UIAlertAction(title: "确定", style: .cancel, handler: { (_) in
                            self.dismissSignVCDelegate?.dismissSignVC()
                        })
                        alert.addAction(doneAction)
                        self.present(alert, animated: true, completion: nil)
                    }
                case .failure(_):
                    let alert = UIAlertController(title: "获取课程详情失败，检查网络连接", message: nil, preferredStyle: .alert)
                    let doneAction = UIAlertAction(title: "确定", style: .cancel, handler: { (_) in
                        self.dismissSignVCDelegate?.dismissSignVC()
                    })
                    alert.addAction(doneAction)
                    self.present(alert, animated: true, completion: nil)
                    
                }
            }
        
        avartarImageViewArray = [avatarImageView1, avatarImageView2, avatarImageView3, avatarImageView4, avatarImageView5, avatarImageView6, avatarImageView7, avatarImageView8, avatarImageView9, avatarImageView10]
        nameLabelArray = [studentNameLabel1, studentNameLabel2, studentNameLabel3, studentNameLabel4, studentNameLabel5, studentNameLabel6, studentNameLabel7, studentNameLabel8, studentNameLabel9, studentNameLabel10]
    }
    
    private let zeroLineOfStudentHeight: CGFloat = 43
    private let oneLineOfStudentHeight: CGFloat = 130
    
    func configureUIWith(json: JSON)
    {
        self.signId = json["signId"].string
        
        // 设置课程信息面板
        courseLabel.text = json["course"]["name"].string
        let stringTuple = handleStringFromService(stringFromService: json["course"]["time"].stringValue)
        timeLabel.text = stringTuple.timeString
        sectionLabel.text = stringTuple.sectionString
        classroomLabel.text = json["course"]["location"].string
        teacherLabel.text = json["course"]["teacherName"].string

        // 设置学生面板
        let studentCount = json["records"].count
        for (indexString, studentJson): (String, JSON) in json["records"] {
            let index = Int(indexString)!
            let avartarimageView = avartarImageViewArray[index]
            avartarimageView.layer.cornerRadius = avartarimageView.frame.width / 2
            avartarimageView.clipsToBounds = true
            avartarimageView.sd_setImage(with: URL(string: studentJson["avatar"].stringValue))
            nameLabelArray[index].text = studentJson["name"].string
        }
        
        for index in studentCount..<10 {
            avartarImageViewArray[index].alpha = 0
            nameLabelArray[index].alpha = 0
        }
        
        if studentCount == 0 {
            studentPadHeight.constant = zeroLineOfStudentHeight
        } else if studentCount < 6 {
            studentPadHeight.constant = oneLineOfStudentHeight
        }
        
    }
    
    var signId: String?
    var signType: Int?      // 签到类别: 0-课前签到/1-课后签到
    
    lazy var locService: BMKLocationService = {
       let locService = BMKLocationService()
        locService.delegate = self
        return locService
    }()
    
    // 课前/课后签到(signType: 0-课前签到/1-课后签到)
    @IBAction func signAction(_ button: UIButton)
    {
        view.makeToastActivity(.center)

        signType = button.tag
        locService.startUserLocationService()
    }
    
    func didUpdate(_ userLocation: BMKUserLocation!)
    {
        locService.stopUserLocationService()
        print("获取位置成功，经度：\(userLocation.location.coordinate.longitude) 纬度：\(userLocation.location.coordinate.latitude)")
        signWith(signType: signType, longitude: userLocation.location.coordinate.longitude, latitude: userLocation.location.coordinate.latitude)
    }
    
    func signWith(signType: Int?, longitude: Double, latitude: Double)
    {
        guard let signId = signId, let signType = signType else {
            fatalError("CourseDetailViewController signWith初始值为空")
        }

        let student = Student()
        UIDevice.current.isBatteryMonitoringEnabled = true
        let battery = UIDevice.current.batteryLevel * 100
        
        Alamofire
            .request(SignRouter.sign(signId: signId, phoneId: student.phone, studentId: student.id, type: signType, battery: battery, longitude: longitude, latitude: latitude))
            .responseJSON { (response) in
                self.view.hideToastActivity()
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("学生签到: \(json)")
                    if json["code"] == "200" {
                        self.battery = json["data"]["battery"].int
                        self.distance = json["data"]["distance"].int
                        self.performSegue(withIdentifier: "signResult", sender: nil)
                    } else {
                        self.view.makeToast("签到失败，原因: \(json["msg"])", duration: 1.0, position: .center)
                    }
                case .failure(_):
                    self.view.makeToast("签到失败，检查网络连接", duration: 1.0, position: .center)
                }
        }
    }

    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        dismissSignVCDelegate?.dismissSignVC()
    }
    
    var battery: Int?
    var distance: Int?
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "signResult" {
            if let desVC = segue.destination as? SignResultViewController {
                desVC.battery = battery
                desVC.distance = distance
                desVC.dismissSignVCDelegate = weakSignVC
            }
        }
    }

    // MARK: - private
    
    // 将服务器返回的字符串"星期一 6节-7节,星期三 7节-8节"转为(timeString: "星期一 星期三", sectionString: "6节-7节 7节-8节")
    private func handleStringFromService(stringFromService: String) -> (timeString: String, sectionString: String)
    {
        var timeString = ""
        var sectionString = ""
        
        let stringArray = stringFromService.components(separatedBy: ",")
        for oneString in stringArray {
            let tempString = oneString.components(separatedBy: " ")
            timeString = timeString.appending(tempString[0]+" ")
            sectionString = sectionString.appending(tempString[1]+" ")
        }
        
        return (timeString, sectionString)
    }
}
