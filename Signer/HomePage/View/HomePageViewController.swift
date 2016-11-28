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

class HomePageViewController: UIViewController, UISearchBarDelegate
{
    var reach: Reachability?
    
    @IBOutlet weak var searchBar: UISearchBar!
    @IBOutlet weak var tableView: UITableView!
    
    var courses = [Course]()
    
    var isReachableNet = true {
        didSet {
            tableView.reloadEmptyDataSet()
        }
    }
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        reach = Reachability.forInternetConnection()
        NotificationCenter.default.addObserver(self, selector: #selector(reachabilityChanged), name: NSNotification.Name.reachabilityChanged, object: nil)
        reach!.startNotifier()
        
        initUI()
        
        Alamofire
            .request(StudentRouter.requestSignedCourses(phone: Student().phone))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("获取学生相关签到课程信息: \(json)")
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
    
    func initUI()
    {
        tableView.dataSource = self
        tableView.delegate = self
        tableView.emptyDataSetSource = self
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        
        searchBar.delegate = self
    }
    
    func configureUIWith(json: JSON)
    {
        self.courses = []
        
        for (_, courseJSON) in json {
            let name = courseJSON["name"].stringValue
            let number = courseJSON["number"].intValue
            var avatarUrls = [String]()
            for (_, avatarUrl) in courseJSON["avatars"] {
                avatarUrls.append(avatarUrl.stringValue)
            }
            let course = Course(name: name, signedNumber: number, avatarUrls: avatarUrls)
            print("课程: \(name)-\(number)-\(avatarUrls)")
            self.courses.append(course)
        }
        
        tableView.reloadData()
    }
    
    func reachabilityChanged(notification: NSNotification)
    {
        if self.reach!.isReachableViaWiFi() || self.reach!.isReachableViaWWAN() {
            print("Service avalaible!!!")
        } else {
            print("No service avalaible!!!")
        }
    }
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar)
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
        return SignCourseCell.cellHeight
    }
}

// MARK: - empty table

extension HomePageViewController: DZNEmptyDataSetSource
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
            return NSAttributedString(string: "暂时还木有记录. . ", attributes: attributes)
        } else {
            return NSAttributedString(string: "好像梦见断网了. . .", attributes: attributes)
        }
    }
}
