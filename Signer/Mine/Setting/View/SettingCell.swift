//
//  SettingCell.swift
//  Signer
//
//  Created by Vernon on 2016/11/10.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class SettingCell: UITableViewCell
{
    
    @IBOutlet weak var contentLabel: UILabel!
    
    @IBOutlet weak var separateLine: UIView!
    
    func configureCell(contentText: String, isLineHidden: Bool)
    {
        contentLabel.text = contentText
        separateLine.isHidden = isLineHidden
    }

}
