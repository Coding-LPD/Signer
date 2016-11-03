package com.scnu.zhou.signer.component.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scnu.zhou.signer.component.util.sqlite.SQLHelper;
import com.scnu.zhou.signer.component.util.sqlite.TableHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhou on 16/11/3.
 */
public class SearchOperateTable {

    private SQLiteDatabase db = null;

    public SearchOperateTable(SQLiteDatabase db) {
        this.db = db;
    }

    public void insert(String key) {
        String sql = "INSERT INTO " + TableHelper.TABLE_SEARCH
                + " (key) "
                + "VALUES ('" + key + "')";
        this.db.execSQL(sql);
        this.db.close();
    }

    /**
     * 清空数据表
     */
    public void clear() {
        this.db.execSQL(SQLHelper.DELETE_SEARCHTABLE);
        this.db.close();
    }

    public List<String> find() {
        List<String> all = new ArrayList<String>();
        String sql = "SELECT key FROM "+ TableHelper.TABLE_SEARCH;
        Cursor result = this.db.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {

            all.add(result.getString(0));
        }
        this.db.close();

        // 将最新的放在最前面
        Collections.reverse(all);
        return all;

    }

    public boolean find(String key) {

        List<String> all = new ArrayList<String>();
        String sql = "SELECT key FROM "+ TableHelper.TABLE_SEARCH + " WHERE key = '" + key + "'";
        Cursor result = this.db.rawQuery(sql, null);
        for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {

            all.add(result.getString(0));
        }
        this.db.close();

        if (all.size() == 0){
            return false;
        }
        else{
            return true;
        }

    }


    public void remove(String key) {

        String sql = "DELETE FROM "+ TableHelper.TABLE_SEARCH + " WHERE key = '" + key + "'";
        this.db.execSQL(sql);
        this.db.close();

    }
}

