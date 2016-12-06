//
//  Date+extension.swift
//  Signer
//
//  Created by Vernon on 2016/12/2.
//  Copyright © 2016年 Vernon. All rights reserved.
//

extension Date
{
    func getTimeIntervalDescriptionTo(date toDate: Date?) -> String?
    {
        guard let toDate = toDate else {
            return nil
        }
        
        let fromMillSeconds = self.timeIntervalSince1970
        let toMillSeconds = toDate.timeIntervalSince1970
        let millSecondDuration = fromMillSeconds - toMillSeconds
        
        let dayDuration = Int((fromMillSeconds - toMillSeconds) / (3600 * 24))    // 计算天数的间隔
        if dayDuration > 7 {
            // 大于7天直接返回形如"2016-12-02"的日期字符串
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            return dateFormatter.string(from: toDate)
        } else if dayDuration > 0 {
            return "\(dayDuration)天前"
        } else {
            let hourDuration = Int(millSecondDuration / (3600))           // 计算小时的间隔
            if hourDuration > 0 {
                return "\(hourDuration)小时前"
            } else {
                let minuteDuration = Int(millSecondDuration / (60))       // 计算分钟的间隔
                if minuteDuration > 0 {
                    return "\(minuteDuration)分钟前"
                } else {
                    return "刚刚"
                }
            }
        }
    }

}
