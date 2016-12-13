//
//  HomePageViewController.swift
//  Signer
//
//  Created by FSQ on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import DZNEmptyDataSet

class HomePageViewController: UIViewController
{
    var reach: Reachability?

    @IBOutlet weak var tableView: UITableView!
    
    var courses = [Course]()
    
    var isReachableNet = false {
        didSet {
            tableView.reloadEmptyDataSet()
        }
    }
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        initUI()
        
        reach = Reachability.forInternetConnection()
        isReachableNet = reach!.isReachable()
        
        reach!.reachableBlock = { _ in
            DispatchQueue.main.async {
                self.view.makeToast("网络已恢复", duration: 1.0, position: .center)
                self.isReachableNet = true
                self.refreshCourses()
                self.tableView.reloadEmptyDataSet()
            }
        }

        reach!.unreachableBlock = { _ in
            DispatchQueue.main.async {
                self.view.makeToast("网络好像出了问题", duration: 1.0, position: .center)
                self.isReachableNet = false
                self.tableView.reloadEmptyDataSet()
            }
        }

        reach!.startNotifier()

        refreshCourses()
    }
    
    lazy var refreshControl: UIRefreshControl = {
       let refreshControl = UIRefreshControl()
        refreshControl.tintColor = UIColor(netHex: 0x97cc00)
        refreshControl.addTarget(self, action: #selector(refreshCourses), for: .valueChanged)
        return refreshControl
    }()

    func initUI()
    {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.emptyDataSetSource = self
        tableView.emptyDataSetDelegate = self
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        tableView.addSubview(refreshControl)
    }
    
    func refreshCourses()
    {
        Alamofire
            .request(StudentRouter.requestSignedCourses(phone: Student().phone))
            .responseJSON { (response) in
                self.refreshControl.endRefreshing()
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("获取学生相关签到课程信息: \(json)")
                    if json["code"] == "200" {
                        self.configureUIWith(json: json["data"])
                    } else {
                        fatalError("获取学生相关签到课程信息失败")
                    }
                case .failure(_):
                    self.view.makeToast("首页更新失败，检查网络连接", duration: 1.0, position: .center)
                }
            }
    }
    
    func configureUIWith(json: JSON)
    {
        self.courses = []
        
        for (_, courseJSON) in json {
            let name = courseJSON["name"].stringValue
            let number = courseJSON["number"].intValue
            let courseId = courseJSON["courseId"].stringValue
            var avatarUrls = [String]()
            for (_, avatarUrl) in courseJSON["avatars"] {
                avatarUrls.append(avatarUrl.stringValue)
            }
            let course = Course(name: name, signedNumber: number, courseId: courseId, avatarUrls: avatarUrls)
            self.courses.append(course)
        }
        
        tableView.reloadData()
    }
    
    @IBAction func clickSearchBarAction(_ sender: UIButton)
    {
        performSegue(withIdentifier: "searchCourseResult", sender: nil)
    }

}

// MARK: - tableView

extension HomePageViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return courses.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SignCourseCell", for: indexPath) as! SignCourseCell
        cell.configureWith(course: courses[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return courses[indexPath.row].avatarUrls.count > 0 ? SignCourseCell.heightForHaveAvatar : SignCourseCell.heightForEmptyAvatar
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if let courseDetailVC = storyboard?.instantiateViewController(withIdentifier: "CourseDetailViewController") as? CourseDetailViewController {
            let navigationController = UINavigationController(rootViewController: courseDetailVC)
            navigationController.navigationBar.tintColor = UIColor(netHex: 0x666666)
            courseDetailVC.courseId = courses[indexPath.row].courseId
            present(navigationController, animated: false, completion: nil)
        }
    }
    
}

// MARK: - empty table

extension HomePageViewController: DZNEmptyDataSetSource, DZNEmptyDataSetDelegate
{
    func image(forEmptyDataSet scrollView: UIScrollView!) -> UIImage!
    {
        if isReachableNet {
            return UIImage(named: "emptyRecord")
        } else {
            return UIImage(named: "failToNet")
        }
    }
    
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString!
    {
        let attributes = [NSFontAttributeName: UIFont.boldSystemFont(ofSize: 14.0),
                          NSForegroundColorAttributeName: UIColor(netHex: 0xbbbbbb)]
        
        if isReachableNet {
            return NSAttributedString(string: "暂时还木有记录. . .", attributes: attributes)
        } else {
            return NSAttributedString(string: "好像梦见断网了. . .", attributes: attributes)
        }
    }
    
    func emptyDataSetShouldAllowScroll(_ scrollView: UIScrollView!) -> Bool
    {
        return true
    }
}
