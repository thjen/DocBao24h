package io.qthjen_dev.docbao24h.Utils.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUtil extends SQLiteOpenHelper {

    public static final String DB_NAME = "favoritestate";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "favState";
    public static final String FAV_ID = "id";
    public static final String FAV_TITLE = "title";
    public static final String FAV_LINK = "link";
    public static final String FAV_DATE = "date";
    public static final String FAV_IMAGE = "image";

    public static final String TABLE_RECENTLY_TIME = "recently";
    public static final String RET_ID = "idRe";
    public static final String RET_TITLE = "titleRet";
    public static final String RET_LINK = "linkRet";
    public static final String RET_DATE = "dateRet";
    public static final String RET_IMAGE = "imageRet";

    public SQLiteUtil(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

//    public void QUERY_DATA(String sql) {
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        sqLiteDatabase.execSQL(sql);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FAV_TITLE + " VARCHAR, " + FAV_DATE + " VARCHAR, " + FAV_LINK + " VARCHAR, " + FAV_IMAGE + " VARCHAR)";
        db.execSQL(sqlQuery);
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_RECENTLY_TIME + "(" + RET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RET_TITLE + " VARCHAR, " + RET_DATE + " VARCHAR, " + RET_LINK + " VARCHAR, " + RET_IMAGE + " VARCHAR)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
