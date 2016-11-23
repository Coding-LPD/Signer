//
//  ChangeAvatarViewController.swift
//  Signer
//
//  Created by Vernon on 2016/11/22.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

class ChangeAvatarViewController: UIViewController, UINavigationControllerDelegate, UIImagePickerControllerDelegate
{
    
    var selectedImage: UIImage?

    override func viewDidLoad()
    {
        super.viewDidLoad()

        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
    }

    @IBAction func selectFromPhotoAlbumAction(_ sender: UIButton)
    {
        let imagePicker = UIImagePickerController()
        imagePicker.delegate = self
        imagePicker.sourceType = .photoLibrary
        imagePicker.allowsEditing = false
        self.present(imagePicker, animated: true, completion: nil)
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any])
    {
        guard let selectedImage = info[UIImagePickerControllerOriginalImage] as? UIImage else {
            fatalError("从相册中获取相片出错")
        }
        
        self.selectedImage = selectedImage
        performSegue(withIdentifier: "previewAvatar", sender: nil)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?)
    {
        super.prepare(for: segue, sender: sender)
        
        if segue.identifier == "previewAvatar" {
            if let desVC = segue.destination as? PreviewAvatarViewController {
                desVC.image = selectedImage
            }
        } else if segue.identifier!.contains("default_avatar_") {
            if let desVC = segue.destination as? PreviewAvatarViewController {
                desVC.image = UIImage(named: segue.identifier!)
            }
        }
    }

}
