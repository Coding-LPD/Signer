//
//  SearchCourseViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/1.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SearchCourseViewController: UITableViewController
{
    var historys: [String]?
    
    lazy var clearButton: UIButton = {
        let clearButton = UIButton(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 50))
        clearButton.setTitle("清空消息记录", for: .normal)
        clearButton.titleLabel?.textAlignment = .center
        clearButton.setTitleColor(UIColor(netHex: 0x666666), for: .normal)
        clearButton.addTarget(self, action: #selector(clearHistory), for: .touchUpInside)
        return clearButton
    }()

    lazy var searchResultVC: SearchResultViewController = {
       let searchResultVC = self.storyboard?.instantiateViewController(withIdentifier: "SearchResultViewController") as! SearchResultViewController
        return searchResultVC
    }()
    
    lazy var searchController: UISearchController = {
        let searchController = UISearchController(searchResultsController: self.searchResultVC)
        searchController.searchBar.placeholder = "搜索与你相关的课程"
        searchController.searchBar.delegate = self
        searchController.obscuresBackgroundDuringPresentation = true
        return searchController
    }()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        tableView.tableFooterView = clearButton
        tableView.tableHeaderView = searchController.searchBar
        
        searchController.searchBar.tintColor = UIColor(netHex: 0x97cc00)
        searchController.searchBar.barTintColor = UIColor(netHex: 0xf5f5f5)
//        searchController.searchBar.setValue("取消", forKey:"_cancelButtonText")
        
        let userDefaults = UserDefaults.standard
        historys = userDefaults.object(forKey: "historys") as? [String] ?? [String]()
    }
    
    override func viewWillDisappear(_ animated: Bool)
    {
        super.viewWillDisappear(animated)
        
        let userDefaults = UserDefaults.standard
        userDefaults.setValue(historys, forKey: "historys")
        userDefaults.synchronize()
    }
    
    func clearHistory()
    {
        historys?.removeAll()
        tableView.reloadData()
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return historys?.count ?? 0
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SearchHistoryCell", for: indexPath) as! SearchHistoryCell
        cell.historyLabel.text = historys?[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if let searchText = historys?[indexPath.row] {
            searchResultVC.searchCourseBy(courseName: searchText)
        }
    }
}

extension SearchCourseViewController: UISearchBarDelegate
{
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar)
    {
        searchResultVC.searchCourseBy(courseName: searchBar.text!)
        historys?.append(searchBar.text!)
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar)
    {
        dismiss(animated: true, completion: nil)
    }

}
