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

protocol dismissSignViewControllerDelegate
{
    func dismissSignVC()
}

class CourseDetailViewController: UIViewController
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
                        fatalError("获取课程详情失败")
                    }
                case .failure(_):
                    self.view.makeToast("获取课程详情失败，检查网络连接", duration: 1.0, position: .center)
                    self.dismissSignVCDelegate?.dismissSignVC()
                }
            }
        
        avartarImageViewArray = [avatarImageView1, avatarImageView2, avatarImageView3, avatarImageView4, avatarImageView5, avatarImageView6, avatarImageView7, avatarImageView8, avatarImageView9, avatarImageView10]
        nameLabelArray = [studentNameLabel1, studentNameLabel2, studentNameLabel3, studentNameLabel4, studentNameLabel5, studentNameLabel6, studentNameLabel7, studentNameLabel8, studentNameLabel9, studentNameLabel10]
    }
    
    func configureUIWith(json: JSON)
    {
        courseLabel.text = json["course"]["name"].string
        timeLabel.text = json["course"]["time"].string
        
        classroomLabel.text = json["course"]["location"].string
        teacherLabel.text = json["course"]["teacherName"].string
        
        for (index, studentJson): (String, JSON) in json["records"] {
            avartarImageViewArray[index].sd_setImage(with: URL(string: studentJson["avatar"].stringValue))
            nameLabelArray[index].text = studentJson["name"].string
        }
    }
    
    @IBAction func backAction(_ sender: UIBarButtonItem)
    {
        dismissSignVCDelegate?.dismissSignVC()
    }
    
    
}
