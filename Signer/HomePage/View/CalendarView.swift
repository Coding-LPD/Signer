//
//  CalendarView.swift
//  Signer
//
//  Created by Vernon on 2016/11/29.
//  Copyright © 2016年 Vernon. All rights reserved.
//

import UIKit
import RxSwift

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
    
    var signedDates = [String]()
    var unsignedDates = [String]()
    
    var dayLabels = [UILabel]()
    
    var year: Int = 2016
    
    var month: Int = 11 {
        didSet {
            configureUI()
        }
    }
    
    var viewHeight: Variable<CGFloat> = Variable(400)
    
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
                label.clipsToBounds = true
                label.layer.cornerRadius = 16
            }
        }
        
        configureUI()
    }
    
    func set(year: Int, month: Int)
    {
        guard year > 0, 1...12 ~= month else {
            fatalError("CalendarView设置year/month不在范围")
        }
        
        self.year = year
        self.month = month
    }
    
    func setMarkedDates(signedDates: [String], unsignedDates: [String])
    {
        self.signedDates = signedDates
        self.unsignedDates = unsignedDates
        
        configureUI()
    }
    
    func configureUI()
    {
        yearLabel.text = "\(year)"
        monthLabel.text = "\(month)月"
        
        let weekOfMonth = getFirstDayAt(year: year, month: month)   // 当月1号是星期几
        let daysOfMonth = getDaysAt(year: year, month: month)   // 这个月有多少天
        let daysOfLastMonth = getLastMonthDaysAt(year: year, month: month)  // 上个月有多少天
        
        for index in 0..<weekOfMonth {
            dayLabels[index].text = "\(daysOfLastMonth-weekOfMonth+index+1)"
            dayLabels[index].textColor = UIColor(netHex: 0xB0B0B0)
            dayLabels[index].backgroundColor = UIColor.clear
        }
        
        var temp = 1
        for index in weekOfMonth..<daysOfMonth+weekOfMonth {
            dayLabels[index].text = "\(temp)"
            dayLabels[index].textColor = UIColor(netHex: 0x333333)
            dayLabels[index].backgroundColor = UIColor.clear
            temp += 1
        }
        
        temp = 1
        for index in daysOfMonth+weekOfMonth..<42 {
            dayLabels[index].text = "\(temp)"
            dayLabels[index].textColor = UIColor(netHex: 0xB0B0B0)
            dayLabels[index].backgroundColor = UIColor.clear
            temp += 1
        }
        
        for signedDate in signedDates {
            let date = getYearAndMonthAndDayFrom(dateString: signedDate)
            if date.year == self.year && date.month == self.month {
                dayLabels[weekOfMonth+date.day-1].textColor = UIColor(netHex: 0xffffff)
                dayLabels[weekOfMonth+date.day-1].backgroundColor = UIColor(netHex: 0xFE8D41)
            }
        }
        for unsignedDate in unsignedDates {
            let date = getYearAndMonthAndDayFrom(dateString: unsignedDate)
            if date.year == self.year && date.month == self.month {
                dayLabels[weekOfMonth+date.day-1].textColor = UIColor(netHex: 0xffffff)
                dayLabels[weekOfMonth+date.day-1].backgroundColor = UIColor(netHex: 0xFF5968)
            }
        }
        
        if weekOfMonth + daysOfMonth > 35 {
            viewHeight.value = 390
            stackView5.isHidden = false
        } else {
            viewHeight.value = 350
            stackView5.isHidden = true
        }
    }
    
    @IBAction func lastMonthAction(_ sender: UIButton)
    {
        year = month == 1 ? year - 1 : year
        month = month == 1 ? 12 : month - 1
    }
   
    @IBAction func nextMonthAction(_ sender: UIButton)
    {
        year = month == 12 ? year + 1 : year
        month = month == 12 ? 1 : month + 1
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
    
    // 返回某个月份1号位于周几(0 = Sunday, 1 = Monday, etc.)
    private func getFirstDayAt(year: Int, month: Int) -> Int
    {
        dateComponents.year = year
        dateComponents.month = month
        dateComponents.day = 1
        return calendar.component(.weekday, from: calendar.date(from: dateComponents)!) - 1
    }
    
    // 获得指定月份的上一个月有多少天
    private func getLastMonthDaysAt(year: Int, month: Int) -> Int
    {
        let lastMonth = month == 1 ? 12 : month - 1
        let newYear = month == 1 ? year - 1 : year
        return getDaysAt(year: newYear, month: lastMonth)
    }
    
    lazy var dateFormatter: DateFormatter = {
        let dateFormatter = DateFormatter()
        return dateFormatter
    }()
    
    // 从形如"2016-12-01"的日期字符串中得到年月日
    private func getYearAndMonthAndDayFrom(dateString: String) -> (year: Int, month: Int, day: Int)
    {
        dateFormatter.dateFormat = "yyyy-MM-dd"
        let date = dateFormatter.date(from: dateString)
        let dateComponents = calendar.dateComponents([.year, .month, .day], from: date!)
        return (year: dateComponents.year!, month: dateComponents.month!, day: dateComponents.day!)
    }
}
