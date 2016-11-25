package com.scnu.zhou.signer.component.util.time;

import com.scnu.zhou.signer.component.bean.chat.ChatRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by zhou on 16/11/25.
 */
public class RoomComparator implements Comparator<ChatRoom> {

    @Override
    public int compare(ChatRoom o1, ChatRoom o2) {

        if (o1.getMsg() == null && o2.getMsg() == null){

            return 0;
        }
        else if (o1.getMsg() == null){

            return 1;
        }
        else if (o2.getMsg() == null){
            return -1;
        }
        else {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //得到指定模范的时间
            try {
                Date d1 = sdf.parse(o1.getMsg().getCreatedAt());
                Date d2 = sdf.parse(o2.getMsg().getCreatedAt());

                if (d1.getTime() > d2.getTime()){

                    return -1;
                }
                else{
                    return 1;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }
}
