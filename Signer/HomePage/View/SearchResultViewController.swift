//
//  SearchResultViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/1.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import DZNEmptyDataSet

protocol SearchResultViewControllerDelegate
{
    func searchResultViewControllerDidClickResult(courseDetailVC: CourseDetailViewController)
}

class SearchResultViewController: UIViewController
{
    
    @IBOutlet weak var tableView: UITableView!

    var courses = [Course]()
    
    
    var delegate: SearchResultViewControllerDelegate?
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.emptyDataSetSource = self
    }
    
    func searchCourseBy(courseName: String)
    {
        view.makeToastActivity(.center)
        
        Alamofire
            .request(StudentRouter.searchCourse(phone: Student().phone, courseName: courseName))
            .responseJSON { [weak self] (response) in
                self?.view.hideToastActivity()
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
//                    print("搜索课程: \(json)")
                    if json["code"] == "200" {
                        self?.configureUIWith(json: json["data"])
                    } else {
                        fatalError("搜索课程失败")
                    }
                case .failure(let error):
                    self?.view.makeToast("搜索课程失败，检查网络连接", duration: 1.0, position: .center)
//                    print(error.localizedDescription)
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

}

// MARK: - tableView

extension SearchResultViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return courses.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SearchResultCell", for: indexPath) as! SearchResultCell
        cell.configureWith(course: courses[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return courses[indexPath.row].avatarUrls.count > 0 ? SearchResultCell.heightForHaveAvatar : SearchResultCell.heightForEmptyAvatar
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if let courseDetailVC = storyboard?.instantiateViewController(withIdentifier: "CourseDetailViewController") as? CourseDetailViewController {
            courseDetailVC.isShowBackButton = false
            courseDetailVC.courseId = courses[indexPath.row].courseId
            delegate?.searchResultViewControllerDidClickResult(courseDetailVC: courseDetailVC)
        }
    }
    
}

// MARK: - empty table

extension SearchResultViewController: DZNEmptyDataSetSource
{
    func image(forEmptyDataSet scrollView: UIScrollView!) -> UIImage!
    {
        return UIImage(named: "emptyRecord")
    }
    
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString!
    {
        let attributes = [NSFontAttributeName: UIFont.boldSystemFont(ofSize: 14.0),
                          NSForegroundColorAttributeName: UIColor(netHex: 0xbbbbbb)]
        return NSAttributedString(string: "暂时还木有记录. . ", attributes: attributes)
    }

}
