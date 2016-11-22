//
//  PreviewAvatarViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/22.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class PreviewAvatarViewController: UIViewController
{
    var image: UIImage?

    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        guard let image = image else {
            fatalError("PreviewAvatarViewController初始图片为空")
        }
        
        avatarImageView.image = image
    }
    
    

}
