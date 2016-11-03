package com.scnu.zhou.signer.ui.widget.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 16/10/23.
 */
public class NoteCalendar extends LinearLayout {

    private Context context;

    private TextView tv_year, tv_month;
    private Button btn_left, btn_right;
    private GridView gv_days;

    private int year = 1, month = 1, day = 1;
    private int monthDays = 0;

    private List<Integer> days;
    private DayAdapter adapter;

    private int cur_day_color = Color.parseColor("#97CC00");

    private Map<String, Boolean> note01;    //"2016-10-26", ture
    private Map<String, Boolean> note02;

    public NoteCalendar(Context context) {
        super(context);

        this.context = context;
        inflate(context, R.layout.layout_calendar, this);

        init();
    }

    public NoteCalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        inflate(context, R.layout.layout_calendar, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NoteCalendar);
        cur_day_color = a.getColor(R.styleable.NoteCalendar_cur_day_color, Color.parseColor("#97CC00"));

        a.recycle();

        init();
    }

    public void init(){

        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_month = (TextView) findViewById(R.id.tv_month);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        gv_days = (GridView) findViewById(R.id.gv_days);

        year = getCurrentYear();
        month = getCurrentMonth();
        day = getCurrentDay();

        days = new ArrayList<>();
        adapter = new DayAdapter(days);
        gv_days.setAdapter(adapter);

        btn_left.setOnClickListener(new OnClickLeftListener());
        btn_right.setOnClickListener(new OnClickRightListener());

        refreshView();
    }


    /**
     * 更新视图
     */
    public void refreshView(){
        tv_year.setText(year + "");
        tv_month.setText(getCurrentMonthCh(month));
        getDaysArray();
    }


    /**
     * 获得日子数组
     */
    private void getDaysArray(){

        days.clear();
        monthDays = getMonthDays(year, month);
        int start = getFirstDayWeek(year, month);
        int line = (start + monthDays) / 6;
        Log.e("start", start + "");
        Log.e("monthDays", monthDays + "");
        for (int i=0; i< 7 * line; i++){

            if (i < start || (i - start + 1) > monthDays){
                days.add(0);
            }
            else{
                days.add(i - start + 1);
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 获得当前年份
     */
    private int getCurrentYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }


    /**
     * 获得当前月份
     */
    private int getCurrentMonth(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获得当前月份中文版
     */
    private String getCurrentMonthCh(int month){

        switch (month){
            case 0: return "一月";
            case 1: return "二月";
            case 2: return "三月";
            case 3: return "四月";
            case 4: return "五月";
            case 5: return "六月";
            case 6: return "七月";
            case 7: return "八月";
            case 8: return "九月";
            case 9: return "十月";
            case 10: return "十一月";
            case 11: return "十二月";
            default: return "";
        }
    }

    /**
     * 获得当前日子
     */
    private int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获得指定月份下有多少天
     * @param year
     * @param month
     * @return
     */
    private int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
                    return 29;
                }else{
                    return 28;
                }
            default:
                return  -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：0		一：1		二：2		三：3		四：4		五：5		六：6
     */
    private int getFirstDayWeek(int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        //Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
        return calendar.get(Calendar.DAY_OF_WEEK)-1;
    }


    private class OnClickLeftListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            if (year == 1 && month == 1){   // 左边临界点
                return;
            }

            month--;
            if (month < 0){
                year--;
                month = 11;
            }

            refreshView();
        }
    }


    private class OnClickRightListener implements OnClickListener{

        @Override
        public void onClick(View v) {

            month++;
            if (month > 11){
                year++;
                month = 0;
            }

            refreshView();
        }
    }


    private class DayAdapter extends BaseAdapter{

        private List<Integer> days;

        public DayAdapter(List<Integer> days){
            this.days = days;
        }

        @Override
        public int getCount() {
            return days.size();
        }

        @Override
        public Integer getItem(int position) {
            return days.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView item = new TextView(context);
            LayoutParams layoutParams = new LayoutParams(
                    140, 140);
            item.setLayoutParams(layoutParams);
            if (year == getCurrentYear() && month == getCurrentMonth() && days.get(position) == getCurrentDay()){
                item.setTextColor(cur_day_color);
            }
            else {
                item.setTextColor(Color.parseColor("#333333"));
            }
            item.setTextSize(14);
            item.setGravity(Gravity.CENTER);
            if (days.get(position) != 0){
                item.setText(days.get(position) + "");
            }

            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal=Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, days.get(position));

            String key = df.format(cal.getTime());
            if (note01 != null){
                if (note01.get(key) != null){
                    item.setTextColor(Color.parseColor("#FFFFFF"));
                    item.setBackgroundResource(R.drawable.calendar_note01);
                }
            }
            if (note02 != null){
                if (note02.get(key) != null){
                    item.setTextColor(Color.parseColor("#FFFFFF"));
                    item.setBackgroundResource(R.drawable.calendar_note02);
                }
            }

            return item;
        }
    }


    public void setNote01(Map<String, Boolean> note){

        this.note01 = note;
        adapter.notifyDataSetChanged();
    }

    public void setNote02(Map<String, Boolean> note){

        this.note02 = note;
        adapter.notifyDataSetChanged();
    }
}
