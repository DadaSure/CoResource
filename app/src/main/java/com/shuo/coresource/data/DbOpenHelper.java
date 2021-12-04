package com.shuo.coresource.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "resource.db";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //用于执行数据库创建的字符串
        String sql = null;


        //创建users表
        sql="CREATE TABLE users(user_name varchar(20) primary key,user_password varchar(20),user_count int(10))";

        try{
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }


        //创建cards表
        sql="CREATE TABLE cards(card_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,user_name varchar(20),card_name varchar(30),card_description varchar(30),card_date varchar(20),card_count int (5),card_completed Boolean,FOREIGN KEY (user_name) REFERENCES users(user_name))";

        try{
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

        //创建consume表
        sql="CREATE TABLE consume(consume_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,user_name varchar(20),consume_name varchar(30),consume_description varchar(30),consume_date varchar(20),consume_count int(5),FOREIGN KEY (user_name) REFERENCES users(user_name))";

        try{
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
