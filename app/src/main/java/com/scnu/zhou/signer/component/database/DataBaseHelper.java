package com.scnu.zhou.signer.component.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scnu.zhou.signer.component.util.sqlite.SQLHelper;

/**
 * Created by zhou on 16/11/3.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "signer.db";
    private static final int DATABASEVERSION = 1;

    public DataBaseHelper(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASENAME, null, DATABASEVERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        Log.i("SearchDataBase", "create Database------------->");
        db.execSQL(SQLHelper.CREATE_SEARCHTABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.i("SearchDataBase", "create Database------------->");
        db.execSQL(SQLHelper.CREATE_SEARCHTABLE);
    }
}
