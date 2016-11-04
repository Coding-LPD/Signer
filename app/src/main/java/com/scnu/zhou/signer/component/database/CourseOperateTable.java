package com.scnu.zhou.signer.component.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scnu.zhou.signer.component.bean.main.MainCourse;
import com.scnu.zhou.signer.component.util.sqlite.SQLHelper;
import com.scnu.zhou.signer.component.util.sqlite.TableHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhou on 16/11/3.
 */
public class CourseOperateTable {

    private SQLiteDatabase db = null;

    public CourseOperateTable(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(MainCourse course) {

        String avatars = "";
        for (String a:course.getAvatars()){
            if (avatars.equals("")){
                avatars = a;
            }
            else{
                avatars += "," + a;
            }
        }

        String sql = "INSERT INTO " + TableHelper.TABLE_COURSE
                + " (name,number,courseId,avatars) "
                + "VALUES ('" + course.getName() + "','" + course.getNumber() + "','" + course.getCourseId()
                + "','" + avatars + "')";
        this.db.execSQL(sql);
        //this.db.close();
    }

    public void insertList(List<MainCourse> courses){

        for (MainCourse course: courses){
            insert(course);
        }
    }

    public void reset(List<MainCourse> courses){

        clear();
        insertList(courses);
    }

    /**
     * 清空数据表
     */
    public void clear() {
        this.db.execSQL(SQLHelper.DELETE_COURSETABLE);
        //this.db.close();
    }

    public List<MainCourse> find() {
        List<MainCourse> all = new ArrayList<MainCourse>();
        String sql = "SELECT name, number, courseId, avatars FROM "+ TableHelper.TABLE_COURSE;
        Cursor result = this.db.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {

            MainCourse course = new MainCourse();
            course.setName(result.getString(0));
            course.setNumber(result.getInt(1));
            course.setCourseId(result.getString(2));

            String[] as = result.getString(3).split(",");
            List<String> avatars = new ArrayList<>();
            for (String a:as) {
                avatars.add(a);
            }

            course.setAvatars(avatars);

            all.add(course);
        }
        //this.db.close();

        // 将最新的放在最前面
        //Collections.reverse(all);
        return all;

    }


    public void remove(String courseId) {

        String sql = "DELETE FROM "+ TableHelper.TABLE_COURSE + " WHERE courseId = '" + courseId + "'";
        this.db.execSQL(sql);
        //this.db.close();
    }
}

