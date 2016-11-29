//
//  CalendarView.swift
//  Signer
//
//  Created by Vernon on 2016/11/29.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit

@IBDesignable class CalendarView: UIView
{
    @IBOutlet weak var yearLabel: UILabel!
    @IBOutlet weak var monthLabel: UILabel!
    
    @IBOutlet weak var stackView0: UIStackView!
    @IBOutlet weak var stackView1: UIStackView!
    @IBOutlet weak var stackView2: UIStackView!
    @IBOutlet weak var stackView3: UIStackView!
    @IBOutlet weak var stackView4: UIStackView!
    @IBOutlet weak var stackView5: UIStackView!
    
    var dayLabels = [UILabel]()
    
    var year: Int = 0 {
        didSet {
            yearLabel.text = "\(year)"
        }
    }
    
    var month: Int = 0 {
        didSet {
            monthLabel.text = "\(month)"
            configureUI()
        }
    }
    
    override init(frame: CGRect)
    {
        super.init(frame: frame)
        
        loadViewFromNib()
    }
    
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
        
        let tempArray = [stackView0, stackView1, stackView2, stackView3, stackView4, stackView5]
        for i in 0..<6 {
            for j in 0..<7 {
                guard let label = tempArray[i]?.arrangedSubviews[j] as? UILabel else  {
                    fatalError("StackView里面不是UILabel")
                }
                dayLabels.append(label)
            }
        }
    }
    
    func configureUI()
    {
        
    }
    
    @IBAction func lastMonthAction(_ sender: UIButton)
    {
        
    }
   
    @IBAction func nextMonthAction(_ sender: UIButton)
    {
        
    }
    
    lazy var calendar: Calendar = {
       return Calendar.current
    }()
    
    lazy var dateComponents: DateComponents = {
        return self.calendar.dateComponents([.year, .month, .day], from: Date())
    }()
    
    // 获得指定月份下有多少天
    private func getDaysAt(year: Int, month: Int) -> Int
    {
        switch month {
        case 1, 3, 5, 7, 8, 10, 12:
            return 31
        case 4, 6, 9, 11:
            return 30
        case 2:
            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                return 29
            } else {
                return 28
            }
        default:
            return -1
        }
    }
    
    // 返回某个月份1号位于周几(1 = Sunday, 2 = Monday, etc.)
    private func getFirstDayAt(year: Int, month: Int) -> Int
    {
        dateComponents.year = year
        dateComponents.month = month
        dateComponents.day = 1
        return calendar.component(.weekday, from: calendar.date(from: dateComponents)!)
    }
}
