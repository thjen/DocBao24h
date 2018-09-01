package io.qthjen_dev.docbao24h.Utils.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Model.ItemFavorited;

public class RecentlyTimeSQLite {

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteUtil sqLiteUtil;

    public RecentlyTimeSQLite(Context context) {
        sqLiteUtil = new SQLiteUtil(context);
    }

    public boolean insertRecentlyTimeItemData(ItemFavorited itemFavorited) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sqLiteUtil.RET_TITLE, itemFavorited.getTitleFav());
        contentValues.put(sqLiteUtil.RET_LINK, itemFavorited.getLinkFav());
        contentValues.put(sqLiteUtil.RET_DATE, itemFavorited.getDateFav());
        contentValues.put(sqLiteUtil.RET_IMAGE, itemFavorited.getImageFav());

        long id = sqLiteDatabase.insert(sqLiteUtil.TABLE_RECENTLY_TIME, null, contentValues);
        sqLiteDatabase.close();
        if (id != 0) {
            return true;
        } else {
            return false;
        }
    }

    public ItemFavorited getDataItemRecently(int idRet) {
        sqLiteDatabase = sqLiteUtil.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(sqLiteUtil.TABLE_RECENTLY_TIME, null, sqLiteUtil.RET_ID + " = ?", new String[]{String.valueOf(idRet)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ItemFavorited itemFavorited = new ItemFavorited(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return itemFavorited;
    }

    public List<ItemFavorited> getAllItemRecently() {
        List<ItemFavorited> list = new ArrayList<>();
        String query = "SELECT * FROM " + sqLiteUtil.TABLE_RECENTLY_TIME;

        sqLiteDatabase = sqLiteUtil.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            ItemFavorited favorited = new ItemFavorited(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            list.add(favorited);
            cursor.moveToNext();
        }
        return list;
    }

    public void updateItemRecently(ItemFavorited itemFavorited) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sqLiteUtil.RET_TITLE, itemFavorited.getTitleFav());
        contentValues.put(sqLiteUtil.RET_DATE, itemFavorited.getDateFav());
        contentValues.put(sqLiteUtil.RET_LINK, itemFavorited.getLinkFav());
        contentValues.put(sqLiteUtil.RET_IMAGE, itemFavorited.getImageFav());
        sqLiteDatabase.update(sqLiteUtil.TABLE_RECENTLY_TIME, contentValues, sqLiteUtil.RET_ID + " = ?",new String[]{String.valueOf(itemFavorited.getIdFAV())});
        sqLiteDatabase.close();
    }

    public void deleteItemWithId(int idRecently) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_RECENTLY_TIME, sqLiteUtil.RET_ID + " = ?", new String[]{String.valueOf(idRecently)});
        sqLiteDatabase.close();
    }

    public void deleteItemWithTitle(String titleRe) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_RECENTLY_TIME, sqLiteUtil.RET_TITLE + " = ?", new String[]{titleRe});
        sqLiteDatabase.close();
    }

    public void deleteRecentlyAllItem() {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_RECENTLY_TIME, null, null);
        sqLiteDatabase.close();
    }
}
