//
//  MessageInputView.swift
//  Signer
//
//  Created by Vernon on 2016/12/6.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift

@IBDesignable class MessageInputView: UIView
{
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var textViewHeightConstraint: NSLayoutConstraint!

    @IBOutlet weak var sendButton: UIButton!

    private let disposeBag = DisposeBag()
    
    override init(frame: CGRect)
    {
        super.init(frame: frame)
        
        loadViewFromNib()
    }
    
    dynamic var viewHeight: CGFloat = 55
    
    required init?(coder aDecoder: NSCoder)
    {
        super.init(coder: aDecoder)
        
        loadViewFromNib()
    }
    
    func loadViewFromNib()
    {
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: String(describing: type(of: self)), bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        view.frame = bounds
        view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(view)
        
        textView.scrollsToTop = false
        _ = rx.observeWeakly(CGSize.self, "textView.contentSize").subscribe ( onNext: { (contentSize) in
            self.textViewHeightConstraint.constant = contentSize!.height
            self.viewHeight = contentSize!.height + 14
        })
        
        
        textView.rx.text.subscribe(onNext: { content in
            self.sendButton.backgroundColor = content!.length > 0 ? UIColor(netHex: 0x97cc00) : UIColor(netHex: 0xB6DB4D)
            self.sendButton.isEnabled = content!.length > 0
        }).addDisposableTo(disposeBag)
    }
    
    @IBAction func sendAction(_ sender: UIButton)
    {
        
    }
    
}
