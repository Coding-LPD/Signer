//
//  PreviewAvatarViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/22.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire

class PreviewAvatarViewController: UIViewController
{
    var image: UIImage?

    @IBOutlet weak var cardView: UIView!
    @IBOutlet weak var avatarImageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
    
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        cardView.layer.borderColor = UIColor(netHex: 0xDDDDDD).cgColor
        cardView.layer.borderWidth = 1.0
        cardView.layer.cornerRadius = 12.0
        
        guard let image = image else {
            fatalError("PreviewAvatarViewController初始图片为空")
        }
        
        avatarImageView.image = image
    }
    
    @IBAction func uploadAvatarAction(_ sender: UIButton)
    {
        Alamofire.upload(multipartFormData: { (multipartFormData) in
            multipartFormData.append(<#T##data: Data##Data#>, withName: <#T##String#>)
        }, to: <#T##URLConvertible#>, encodingCompletion: <#T##((SessionManager.MultipartFormDataEncodingResult) -> Void)?##((SessionManager.MultipartFormDataEncodingResult) -> Void)?##(SessionManager.MultipartFormDataEncodingResult) -> Void#>)
    }
    
    @IBAction func cancelAction(_ sender: UIButton)
    {
        
    }

}
