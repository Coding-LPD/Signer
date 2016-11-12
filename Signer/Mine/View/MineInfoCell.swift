//
//  MineInfoCell.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class MineInfoCell: UITableViewCell
{

    @IBOutlet weak var leftLabel: UILabel!

    @IBOutlet weak var contentLabel: UILabel!
    
    @IBOutlet weak var seperateLine: UIView!
    
    func configureCell(leftText: String, contentText: String, isLineHidden: Bool)
    {
        leftLabel.text = leftText
        contentLabel.text = contentText
        seperateLine.isHidden = isLineHidden
    }
    
}
