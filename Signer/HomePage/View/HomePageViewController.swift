//
//  HomePageViewController.swift
//  Signer
//
//  Created by FSQ on 16/9/12.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class HomePageViewController: UIViewController
{
    var reach: Reachability?
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        self.reach = Reachability.forInternetConnection()
        NotificationCenter.default.addObserver(self, selector: #selector(reachabilityChanged), name: NSNotification.Name.reachabilityChanged, object: nil)
        self.reach!.startNotifier()
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = UIColor(netHex: 0xf5f5f5)
    }
    
    func reachabilityChanged(notification: NSNotification)
    {
        if self.reach!.isReachableViaWiFi() || self.reach!.isReachableViaWWAN() {
            print("Service avalaible!!!")
        } else {
            print("No service avalaible!!!")
        }
    }

}

// MARK: - tableView

extension HomePageViewController: UITableViewDataSource, UITableViewDelegate
{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 3
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SignCourseCell", for: indexPath) as! SignCourseCell
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        return SignCourseCell.cellHeight
    }
}
