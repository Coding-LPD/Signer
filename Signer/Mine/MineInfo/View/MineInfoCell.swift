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
    
    var leftText: String?
    var contentText: String?
    var parameterName: String?
    
    func configureCell(leftText: String?, parameterName: String, contentText: String?, placeText: String?, isLineHidden: Bool)
    {
        leftLabel.text = leftText
        contentLabel.text = (contentText ?? "").isEmpty ? placeText : contentText
        seperateLine.isHidden = isLineHidden
        
        self.leftText = leftText
        self.parameterName = parameterName
        self.contentText = contentText
    }
    
}
