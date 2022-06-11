package com.example.todo.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kapil on 29/9/17.
 */

public class TodoOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static TodoOpenHelper mInstance = null;



    public TodoOpenHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TodoOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TodoOpenHelper(context);
        }

        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + Contract.TODO_TABLE_NAME + " ( " +
                Contract.TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TODO_TITLE + " TEXT, " +
                Contract.TODO_DATE + " TEXT, " +
                Contract.TODO_PHOTO+ " BLOB, "+
                Contract.TODO_STATUS+" INT,"+
                Contract.TODO_END+" TEXT, "+
                Contract.TODO_CONTENT + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion + 1) {//注意，这里更新版本都是在上一个版本加1
            case 2: //这里没有break
                String sql2 = "ALTER TABLE " + Contract.TODO_TABLE_NAME + " ADD COLUMN " + Contract.TODO_END + "TEXT";
                sqLiteDatabase.execSQL(sql2);
                sql2 = "ALTER TABLE " + Contract.TODO_TABLE_NAME + " ADD COLUMN " + Contract.TODO_STATUS + "INT";
                sqLiteDatabase.execSQL(sql2);
                sql2 = "ALTER TABLE " + Contract.TODO_TABLE_NAME + " ADD COLUMN " + Contract.TODO_PHOTO + "BLOB";
                sqLiteDatabase.execSQL(sql2);
        }


        // TODO
    }


}
