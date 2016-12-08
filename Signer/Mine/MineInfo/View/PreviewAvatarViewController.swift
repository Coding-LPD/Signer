//
//  PreviewAvatarViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/22.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

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
    
    // 上传头像
    @IBAction func uploadAvatarAction(_ sender: UIButton)
    {
        view.makeToastActivity(.center)
        Alamofire.upload(
            multipartFormData: { (multipartFormData) in
                if let imageData = UIImageJPEGRepresentation(self.image!, 1) {
                    multipartFormData.append(imageData, withName: "avatar", fileName: "avatar.png", mimeType: "image/png")
                }},
            to: SignUpRouter.baseAPIURL.appending("/students/images"),
            method: .post,
            headers: ["Content-Type": "multipart/form-data"],
            encodingCompletion: { encodingResult in
                switch encodingResult {
                case .success(let upload, _, _):
                    upload.responseJSON { response in
                        switch response.result {
                        case .success(let value):
                            let json = JSON(value)
                            print("上传返回: \(value)")
                            self.modifyAvatarWith(newAvatarURL: json["data"].stringValue)
                        case .failure(_):
                            self.view.makeToast("上传失败，请检查网络连接", duration: 1.0, position: .center)
                        }
                    }
                case .failure(let encodingError):
                    fatalError("上传头像失败: \(encodingError)")
                }})
    }
    
    // 修改头像
    func modifyAvatarWith(newAvatarURL: String)
    {
        Alamofire
            .request(StudentRouter.modifyStudent(id: Student().id, parameters: ["avatar": newAvatarURL]))
            .responseJSON { (response) in
                switch response.result {
                case .success(let value):
                    let json = JSON(value)
                    print("修改学生头像: \(json)")
                    if json["code"] == "200" {
                        DispatchQueue.main.async {
                            let userDefaults = UserDefaults.standard
                            userDefaults.set(newAvatarURL, forKey: "avatarUrl")
                            userDefaults.synchronize()
                            self.view.hideToastActivity()
                            _ = self.navigationController?.popToViewController(self.navigationController!.viewControllers[1], animated: true)
                        }
                    } else {
                        fatalError("修改学生头像失败")
                    }
                case .failure(_):
                    DispatchQueue.main.async {
                        self.view.makeToast("修改头像失败，检查网络连接", duration: 1.0, position: .center)
                    }
                }
        }
    }

}
