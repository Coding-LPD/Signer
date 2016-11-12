//
//  MineLeftImageCell.swift
//  Signer
//
//  Created by Vernon on 16/9/13.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class MineLeftImageCell: UITableViewCell
{

    @IBOutlet weak var leftImageView: UIImageView!
    
    @IBOutlet weak var label: UILabel!
    
    @IBOutlet weak var separateLine: UIView!
    
    static let cellHeight: CGFloat = 50.0
    
    func configureCell(imageName: String, text: String, isLineHidden: Bool)
    {
        leftImageView.image = UIImage(named: imageName)
        label.text = text
        separateLine.isHidden = isLineHidden
    }
    
}
