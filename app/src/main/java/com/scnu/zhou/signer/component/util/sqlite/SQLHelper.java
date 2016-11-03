package com.scnu.zhou.signer.component.util.sqlite;

/**
 * Created by zhou on 16/11/3.
 */
public class SQLHelper {

    // 创建搜索记录数据库
    public static String CREATE_SEARCHTABLE = "CREATE TABLE IF NOT EXISTS "
            + TableHelper.TABLE_SEARCH + " ("
            + "key   VARCHAR(50)   NOT NULL )";

    // 删除搜索记录数据库
    public static String DELETE_SEARCHTABLE = "DROP TABLE IF EXISTS "
            + TableHelper.TABLE_SEARCH;
}
