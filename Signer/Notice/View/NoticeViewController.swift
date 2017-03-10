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
import SVPullToRefresh
import DZNEmptyDataSet

// 签到类型（课前/课后）
enum NoticeType: Int
{
    case beforeClass = 0
    case afterClass = 1
}

class NoticeViewController: UIViewController
{
    @IBOutlet weak var tableView: UITableView!
    
    weak var mainTabBarVC: MainTabBarViewController?
    
    let noticesNumberPerPage = 10
    
    var reach: Reachability?
    var isReachableNet = false {
        didSet {
            tableView.reloadEmptyDataSet()
        }
    }
    
    var noticeType: NoticeType = .beforeClass {
        didSet {
            DispatchQueue.main.async {
                self.tableView.reloadEmptyDataSet()
                self.tableView.reloadData()
            }
        }
    }
    
    var beforeNotices = [Notice]()      // 课前通知
    var totalBeforeNoticePage = 0
    var isLoadAllBeforeNotices = false  // 所有课前通知都已经加载完
    
    var afterNotices = [Notice]()       // 课后通知
    var totalAfterNoticePage = 0
    var isLoadAllAfterNotices = false
    
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.emptyDataSetSource = self
        tableView.emptyDataSetDelegate = self
        tableView.addSubview(refreshControl)
        
        loadNoticesFor(type: .beforeClass, page: 0)
        loadNoticesFor(type: .afterClass, page: 0)

        monitorToNetChange()
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        self.navigationController?.tabBarItem.badgeValue = nil
    }

    // MARK: - 下拉刷新
    
    lazy var refreshControl: UIRefreshControl = {
        let refreshControl = UIRefreshControl()
        refreshControl.tintColor = ThemeGreenColor
        refreshControl.addTarget(self, action: #selector(refreshNoticesFromService), for: .valueChanged)
        return refreshControl
    }()
    
    func refreshNoticesFromService()
    {
        self.navigationController?.tabBarItem.badgeValue = nil
        
        Alamofire
            .request(StudentRouter.requestNotice(phone: Student().phone, type: noticeType.rawValue, page: 0))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    if json["code"] == "200" {
                        DispatchQueue.global().async {
                            self.refreshNoticesFor(type: self.noticeType, newNotices: self.convertJsonToNotices(json: json["data"]))
                        }
                    } else {
                        fatalError("获取通知出错")
                    }
                case .failure(_):
                    self.refreshControl.endRefreshing()
                }
        }
    }
    
    func refreshNoticesFor(type: NoticeType, newNotices notices: [Notice])
    {
        if noticeType == .beforeClass {
            beforeNotices.removeAll()
            beforeNotices.append(contentsOf: notices)
            totalBeforeNoticePage = 1
            isLoadAllBeforeNotices = (notices.count < noticesNumberPerPage ? true : false)
        } else if noticeType == .afterClass {
            afterNotices.removeAll()
            afterNotices.append(contentsOf: notices)
            totalAfterNoticePage = 1
            isLoadAllAfterNotices = (notices.count < noticesNumberPerPage ? true : false)
        }
        
        DispatchQueue.main.async {
            self.tableView.reloadData()
            self.tableView.reloadEmptyDataSet()
            self.refreshControl.endRefreshing()
            
        }
    }

    // MARK: - 上拉加载更多通知
    
    func loadMoreNotice()
    {
        if noticeType == .beforeClass {
            if isLoadAllBeforeNotices {
                view.makeToast("无更多通知", duration: 0.3, position: .bottom)
            } else {
                loadNoticesFor(type: self.noticeType, page: self.totalBeforeNoticePage)
            }
        } else if noticeType == .afterClass {
            if isLoadAllAfterNotices {
                view.makeToast("无更多通知", duration: 0.3, position: .bottom)
            } else {
                loadNoticesFor(type: self.noticeType, page: self.totalAfterNoticePage)
            }
        }
    }
    
    // 从服务器获取第page页的通知
    func loadNoticesFor(type: NoticeType, page: Int)
    {
        Alamofire
            .request(StudentRouter.requestNotice(phone: Student().phone, type: type.rawValue, page: page))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    if json["code"] == "200" {
                        DispatchQueue.global().async {
                            self.addNotices(newNotices: self.convertJsonToNotices(json: json["data"]), noticeType: type, page: page)
                        }
                    } else {
                        fatalError("获取通知出错")
                    }
                case .failure(_):
                    self.tableView.infiniteScrollingView.stopAnimating()
                }
        }
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return dateFormatter
    }()
    
    // 将服务器返回的JSON转化成notice数组
    func convertJsonToNotices(json: JSON) -> [Notice]
    {
        var tempNotices = [Notice]()
        
        for (_, noticeJson) in json {
            let courseName = noticeJson["courseName"].stringValue
            let signNumber = noticeJson["signNumber"].intValue
            let signState = SignState(num: noticeJson["signState"].intValue)!
            let distance = noticeJson["signDistance"].intValue
            let signDate = self.dateFormatter.date(from: noticeJson["signAt"].stringValue)!
            let confirmDate = self.dateFormatter.date(from: noticeJson["confirmAt"].stringValue)!
            
            let notice = Notice(courseName: courseName, signNumber: signNumber, signState: signState, distance: distance, signDate: signDate, confirmDate: confirmDate)
            tempNotices.append(notice)
        }
        
        return tempNotices
    }
    
    // 将网页中获取到的第page页的新通知加到数据源中，更新tableView
    func addNotices(newNotices: [Notice], noticeType: NoticeType, page: Int)
    {
        var originalNoticeCount = 0
        
        if noticeType == .beforeClass {
            originalNoticeCount = beforeNotices.count
            beforeNotices.append(contentsOf: newNotices)
            totalBeforeNoticePage = page + 1
            isLoadAllBeforeNotices = (newNotices.count < noticesNumberPerPage ? true : false)
        } else if noticeType == .afterClass {
            originalNoticeCount = afterNotices.count
            afterNotices.append(contentsOf: newNotices)
            totalAfterNoticePage = page + 1
            isLoadAllAfterNotices = (newNotices.count < noticesNumberPerPage ? true : false)
        }
        
        if self.noticeType == noticeType {
            var appendIndexPaths = [IndexPath]()
            for i in 0 ..< newNotices.count {
                appendIndexPaths.append(IndexPath(row: originalNoticeCount + i, section: 0))
            }
            
            DispatchQueue.main.async {
                self.tableView.insertRows(at: appendIndexPaths, with: UITableViewRowAnimation.fade)
            }
        }
    }
    
    @IBAction func changeNoticeTypeAction(_ segmentedControl: UISegmentedControl)
    {
        noticeType = NoticeType(rawValue: segmentedControl.selectedSegmentIndex)!
    }
    
    // 监测网络状态变化
    func monitorToNetChange()
    {
        reach = Reachability.forInternetConnection()
        isReachableNet = reach!.isReachable()
        
        reach!.reachableBlock = { _ in
            DispatchQueue.main.async {
                self.view.makeToast("网络已恢复", duration: 1.0, position: .center)
                self.isReachableNet = true
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
    }
    
}

extension NoticeViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if noticeType == .beforeClass {
            return beforeNotices.count
        } else if noticeType == .afterClass {
            return afterNotices.count
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "NoticeCell", for: indexPath) as! NoticeCell
        
        if noticeType == .beforeClass {
            cell.configureWith(notice: beforeNotices[indexPath.row])
        } else if noticeType == .afterClass {
            cell.configureWith(notice: afterNotices[indexPath.row])
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return NoticeCell.cellHeight
    }
    
    // 加载更多
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath)
    {
        let noticeCount = noticeType == .beforeClass ? beforeNotices.count : afterNotices.count
        if noticeCount - 1 == indexPath.row {
            loadMoreNotice()
        }
    }
    
    
}

// MARK: - Empty table

extension NoticeViewController: DZNEmptyDataSetSource, DZNEmptyDataSetDelegate
{
    func image(forEmptyDataSet scrollView: UIScrollView!) -> UIImage!
    {
        if isReachableNet {
            return UIImage(named: "emptyNotice")
        } else {
            return UIImage(named: "failToNet")
        }
    }
    
    func title(forEmptyDataSet scrollView: UIScrollView!) -> NSAttributedString!
    {
        let attributes = [NSFontAttributeName: UIFont.boldSystemFont(ofSize: 14.0),
                          NSForegroundColorAttributeName: UIColor(netHex: 0xbbbbbb)]
        
        if isReachableNet {
            return NSAttributedString(string: "还没有签到通知. . .", attributes: attributes)
        } else {
            return NSAttributedString(string: "好像梦见断网了. . .", attributes: attributes)
        }
    }
    
    func emptyDataSetShouldAllowScroll(_ scrollView: UIScrollView!) -> Bool
    {
        return true
    }
}
