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


    // 创建首页课程数据库
    public static String CREATE_COURSETABLE = "CREATE TABLE IF NOT EXISTS "
            + TableHelper.TABLE_COURSE + " ("
            + "id   VARCHAR(50)   PRIMARY KEY ,"
            + "name   VARCHAR(50)   NOT NULL ,"
            + "number   INTEGER   NOT NULL ,"
            + "courseId   VARCHAR(50)   NOT NULL ,"
            + "avatars   VARCHAR(255)   NOT NULL )";

    // 删除首页课程数据库
    public static String DELETE_COURSETABLE = "DROP TABLE IF EXISTS "
            + TableHelper.TABLE_COURSE;
}
