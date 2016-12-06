//
//  MessageCell.swift
//  Signer
//
//  Created by Vernon on 2016/12/6.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import SnapKit

enum MessageType
{
    case left, right
}

class MessageCell: UITableViewCell
{
    static let screenWidth = UIScreen.main.bounds.width
    let imageWidth: CGFloat = 38.0
    
    lazy var avatarImageView: UIImageView = {
        let avatarImageView = UIImageView(frame: CGRect(x: 0, y: 0, width: self.imageWidth, height: self.imageWidth))
        avatarImageView.layer.cornerRadius = self.imageWidth / 2
        avatarImageView.layer.masksToBounds = true
        return avatarImageView
    }()
    
    lazy var contentLabel: UILabel = {
        let contentLabel = UILabel(frame: CGRect(x: 0, y: 0, width: MessageCell.screenWidth, height: 30.0))
        contentLabel.backgroundColor = UIColor(netHex: 0x97cc00)
        contentLabel.layer.cornerRadius = 8.0
        return contentLabel
    }()
    
    var messageType: MessageType = .left {
        didSet {
            setMessageType()
        }
    }

    override init(style: UITableViewCellStyle, reuseIdentifier: String?)
    {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        
        contentView.addSubview(avatarImageView)
        contentView.addSubview(contentLabel)
        
        setMessageType()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func setMessageType()
    {
        avatarImageView.snp.removeConstraints()
        contentLabel.snp.removeConstraints()
        
        avatarImageView.snp.makeConstraints { (make) in
            make.top.equalTo(contentView).offset(20)
            make.height.equalTo(imageWidth)
            make.width.equalTo(imageWidth)
        }
        
        contentLabel.snp.makeConstraints { (make) in
            make.top.equalTo(contentView).offset(20)
        }
        
//        switch messageType {
//        case .left:
//            <#code#>
//        case .right:
//            <#code#>
//        }
    }

}
