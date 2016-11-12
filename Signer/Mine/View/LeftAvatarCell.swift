//
//  LeftAvatarCell.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class LeftAvatarCell: UITableViewCell
{

    @IBOutlet weak var avatarImageView: UIImageView!
    
    @IBOutlet weak var userNameLabel: UILabel!
    
    @IBOutlet weak var idLabel: UILabel!
    
    static let cellHeight: CGFloat = 70
    
    func configureCell(withAvatarUrl: String?, userName: String?, id: String?)
    {
        
        userNameLabel.text = userName
        idLabel.text = id
    }

}
