//
//  SearchCourseViewController.swift
//  Signer
//
//  Created by Vernon on 2016/12/1.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SearchCourseViewController: UIViewController, UITableViewDataSource, UITableViewDelegate
{
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchTextField: UITextField!
    
    var historys: [String]?
    
    lazy var clearButton: UIButton = {
        let clearButton = UIButton(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 50))
        clearButton.setTitle("清空消息记录", for: .normal)
        clearButton.titleLabel?.textAlignment = .center
        clearButton.titleLabel?.font = UIFont.systemFont(ofSize: 14.0)
        clearButton.setTitleColor(UIColor(netHex: 0x666666), for: .normal)
        clearButton.addTarget(self, action: #selector(clearHistory), for: .touchUpInside)
        return clearButton
    }()

    lazy var searchResultVC: SearchResultViewController = {
       let searchResultVC = self.storyboard?.instantiateViewController(withIdentifier: "SearchResultViewController") as! SearchResultViewController
        return searchResultVC
    }()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        searchTextField.delegate = self
        searchTextField.enablesReturnKeyAutomatically = true
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.tableFooterView = clearButton

        let userDefaults = UserDefaults.standard
        historys = userDefaults.object(forKey: "historys") as? [String] ?? [String]()
    }
    
    override func viewDidAppear(_ animated: Bool)
    {
        super.viewDidAppear(animated)
        
        searchTextField.becomeFirstResponder()
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
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        let count = historys?.count ?? 0
        if count == 0 {
            clearButton.isHidden = true
        } else {
            clearButton.isHidden = false
        }
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SearchHistoryCell", for: indexPath) as! SearchHistoryCell
        cell.historyLabel.text = historys?[indexPath.row]
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath)
    {
        if let searchText = historys?[indexPath.row] {
            searchTextField.text = searchText
            searchTextField.resignFirstResponder()
            addSearchHistory(name: searchText)
            searchResultVC.searchCourseBy(courseName: searchText)
            tableView.addSubview(searchResultVC.view)
        }
    }
    
    @IBAction func cancelAction(_ sender: UIButton)
    {
        searchTextField.resignFirstResponder()
        dismiss(animated: true, completion: nil)
    }
    
    func addSearchHistory(name: String)
    {
        for index in 0..<historys!.count {
            if historys![index] == name {
                historys?.remove(at: index)
                break
            }
        }
        historys?.insert(name, at: 0)
        tableView.reloadData()
    }
    
}

extension SearchCourseViewController: UITextFieldDelegate
{
    func textFieldShouldReturn(_ textField: UITextField) -> Bool
    {
        let text = textField.text ?? ""
        if text.length > 0 {
            addSearchHistory(name: text)
            searchResultVC.searchCourseBy(courseName: text)
            tableView.addSubview(searchResultVC.view)
            searchTextField.resignFirstResponder()
        }
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField)
    {
        let text = textField.text ?? ""
        if text.length == 0 {
            searchResultVC.view.removeFromSuperview()
        }
    }

}
