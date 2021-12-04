package com.shuo.coresource.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CardDAO {
    //每个方法在使用时应由调用它的Activity实例化并传入DbOpenHelper
    //在每个方法中通过getReadableDatabase等方法获取数据库并执行SQL语句


    //卡片页面显示当前卡片列表
    //读 cards 表中当前 user_name 未消耗卡片信息
    //以List<Card>的形式返回一个list，其中每一个Card实例代表着一个卡片的查询结果
    public List<Card> readCards(DbOpenHelper dbOpenHelper, String userName){
        List<Card> list=new ArrayList<Card>();
        Cursor cursor=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //查询数据库
        try{
            cursor = db.rawQuery("SELECT * FROM cards WHERE card_completed=? AND user_name=?",new String[]{"0",userName});

            //通过cursor写入list
            if(cursor.moveToFirst()){
                do{
                    int id=cursor.getInt(cursor.getColumnIndex("card_id"));
                    String name=cursor.getString(cursor.getColumnIndex("card_name"));
                    String username=cursor.getString(cursor.getColumnIndex("user_name"));
                    String description=cursor.getString(cursor.getColumnIndex("card_description"));
                    String date=cursor.getString(cursor.getColumnIndex("card_date"));
                    int Count=cursor.getInt(cursor.getColumnIndex("card_Count"));
                    boolean completed=cursor.getInt(cursor.getColumnIndex("card_Count"))>0;
                    Card card=new Card(id, username, name, description, date, Count, completed);
                    list.add(card);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    //进入卡片详情页面显示需要查看卡片的信息
    //读 cards 表指定 card_id 卡片信息
    //card_id, card_name, card_description, card_date, card_Count
    //card_id由页面传入
    public List<Card> readIdCard(DbOpenHelper dbOpenHelper, int cardId){
        List<Card> list=new ArrayList<Card>();
        Cursor cursor=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //要执行的语句
        String sql = "SELECT * FROM cards WHERE card_id="+cardId;
        Log.i("get id card", "sql = "+sql);

        try{
            //查询数据库
            cursor = db.rawQuery(sql,null);

            Log.i("cursor","cursor size: "+cursor.getCount());

            //通过cursor写入list
            if(cursor.moveToFirst()){
                do{
                    int id=cursor.getInt(cursor.getColumnIndex("card_id"));
                    String name=cursor.getString(cursor.getColumnIndex("card_name"));
                    String username=cursor.getString(cursor.getColumnIndex("user_name"));
                    String description=cursor.getString(cursor.getColumnIndex("card_description"));
                    String date=cursor.getString(cursor.getColumnIndex("card_date"));
                    int Count=cursor.getInt(cursor.getColumnIndex("card_Count"));
                    boolean completed=cursor.getInt(cursor.getColumnIndex("card_Count"))>0;
                    Card card=new Card(id, username, name, description, date, Count, completed);
                    list.add(card);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    //消耗卡片，获得当前卡片的数量值
    //更新 cards 表指定 card_id 行的 card_completed 属性为 True
    //查询 users 表指定 user_id 行的 user_Count 属性值，存入局部变量currentCount
    //更新 users 表指定 user_id 行的 user_Count 属性值为currentCount+getCount
    //getCount由页面调用此方法时传入
    public void completeCard(DbOpenHelper dbOpenHelper, int cardId, String userName, int getCount){
        int currentCount=0;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        try{
            //更新 card_completed
            db.execSQL("UPDATE cards SET card_completed = 1 WHERE card_id =?",new String[] {String.valueOf(cardId)});

            //查询 currentCount
            Cursor cursor = db.rawQuery("SELECT user_Count FROM users WHERE user_name=?",new String[] {userName});
            if(cursor.moveToFirst()){
                do{
                    currentCount=cursor.getInt(cursor.getColumnIndex("user_Count"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            //更新 user_Count
            int userCount = currentCount + getCount;
            db.execSQL("UPDATE users SET user_Count = "+userCount+" WHERE user_name ='"+ userName+"'");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //删除当前卡片
    //删除 cards 表中指定 card_id 的行
    //需要删除的 card_id 由页面传入
    public void deleteCard(DbOpenHelper dbOpenHelper, int cardId){
        String sql=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //需要执行的语句
        sql = "DELETE FROM cards WHERE card_id ="+ cardId;

        try{
            //执行
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //创建新卡片
    //向 cards 表插入新行
    //新的卡片user_name, card_name, card_description, card_date, card_Count, card_completed由页面传入
    public void createCard(DbOpenHelper dbOpenHelper, String userName, String cardName, String cardDescription, String cardDate, int cardCount, int cardCompleted) {
        String sql=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //需要执行的语句
        sql = "INSERT INTO cards (user_name, card_name, card_description, card_date, card_Count, card_completed) VALUES ('"+userName+"','"+cardName+"','"+cardDescription+"','"+cardDate+"',"+cardCount + ","+cardCompleted+")";

        try{
            //执行
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //在卡片消耗记录页面中显示已消耗的卡片
    //查询 cards 表中当前用户已消耗的卡片
    //当前用户名userName由页面传入
    public List<Card> readCompletedCards(DbOpenHelper dbOpenHelper, String userName){
        List<Card> list=new ArrayList<Card>();
        Cursor cursor=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        try {
            //查询数据库
            cursor = db.rawQuery("SELECT * FROM cards WHERE card_completed=? AND user_name=?",new String[]{"1",userName});

            //通过cursor写入list
            if(cursor.moveToFirst()){
                do{
                    int id=cursor.getInt(cursor.getColumnIndex("card_id"));
                    String name=cursor.getString(cursor.getColumnIndex("card_name"));
                    String username=cursor.getString(cursor.getColumnIndex("user_name"));
                    String description=cursor.getString(cursor.getColumnIndex("card_description"));
                    String date=cursor.getString(cursor.getColumnIndex("card_date"));
                    int Count=cursor.getInt(cursor.getColumnIndex("card_Count"));
                    boolean completed=cursor.getInt(cursor.getColumnIndex("card_Count"))>0;
                    Card card=new Card(id, username, name, description, date, Count, completed);
                    list.add(card);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    //读取指定用户名的密码
    public String readUserPassword(DbOpenHelper dbOpenHelper, String userName) {
        String password=null;
        String sql=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //要执行的语句
        sql="SELECT user_password FROM users WHERE user_name='"+userName+"' ";


        try {
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    password=cursor.getString(cursor.getColumnIndex("user_password"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        return password;
    }


    //创建新用户
    public void createUser(DbOpenHelper dbOpenHelper, String userName, String userPassword) {
        String sql=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //需要执行的语句
        sql = "INSERT INTO users (user_name, user_password, user_Count) VALUES ('"+userName+"','"+userPassword+"',0)";

        try{
            //执行
            db.execSQL(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //查询指定用户名是否存在
    public boolean isUserExist(DbOpenHelper dbOpenHelper, String userName) {
        String password=null;
        String sql=null;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //要执行的语句
        sql="SELECT user_password FROM users WHERE user_name='"+userName+"' ";


        try {
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    password=cursor.getString(cursor.getColumnIndex("user_password"));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }


        //若password为null或者长度为0，说明该用户不存在
        if(password==null || password.length()==0) {
            return false;
        }else {
            return true;
        }
    }


    

    //部分消耗卡片
    //查询 cards 表指定 card_id 行的 card_Count 属性值，存入局部变量currentCount
    //更新 cards 表指定 card_id 行的 card_Count 属性值为currentCount-consumedCount
    //getCount由页面调用此方法时传入
    public void completeSomeCard(DbOpenHelper dbOpenHelper, int cardId, int consumedCount){
        int currentCount=0;
        int setCount=0;

        //获取数据库
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        try{

            //查询 currentCount
            Cursor cursor = db.rawQuery("SELECT card_Count FROM cards WHERE card_id=?",new String[] {String.valueOf(cardId)});
            if(cursor.moveToFirst()){
                do{
                    currentCount=cursor.getInt(cursor.getColumnIndex("card_Count"));
                }while(cursor.moveToNext());
            }
            cursor.close();

            //更新 card_Count
            setCount = currentCount-consumedCount;
            db.execSQL("UPDATE cards SET card_Count = ? WHERE card_id =?",new String[] {String.valueOf(setCount), String.valueOf(cardId)});

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
