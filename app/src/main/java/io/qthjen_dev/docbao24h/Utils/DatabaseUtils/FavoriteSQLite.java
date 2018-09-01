package io.qthjen_dev.docbao24h.Utils.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.qthjen_dev.docbao24h.Model.ItemFavorited;

public class FavoriteSQLite {

    private SQLiteDatabase sqLiteDatabase;
    private SQLiteUtil sqLiteUtil;

    public FavoriteSQLite(Context context) {
        sqLiteUtil = new SQLiteUtil(context);
    }

    /*public void openDatabase() {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
    }

    public void readDatabase() {
        sqLiteDatabase = sqLiteUtil.getReadableDatabase();
    }*/

    /*public void closeDatabase() {
        sqLiteUtil.close();
    }*/

    public boolean insertFavoriteItem(ItemFavorited favorited) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(sqLiteUtil.FAV_TITLE, favorited.getTitleFav());
        contentValues.put(sqLiteUtil.FAV_DATE, favorited.getDateFav());
        contentValues.put(sqLiteUtil.FAV_LINK, favorited.getLinkFav());
        contentValues.put(sqLiteUtil.FAV_IMAGE, favorited.getImageFav());

        long countId = sqLiteDatabase.insert(sqLiteUtil.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
        if (countId != 0)
            return true;
        else
            return false;
    }

    public ItemFavorited getDataFavoriteItem(int idFav) {
        sqLiteDatabase = sqLiteUtil.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(sqLiteUtil.TABLE_NAME, null, sqLiteUtil.FAV_ID + " = ?", new String[]{String.valueOf(idFav)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ItemFavorited itemFavorited = new ItemFavorited(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return itemFavorited;
    }

    public List<ItemFavorited> getAllFavoriteItem() {
        List<ItemFavorited> itemFavoriteds = new ArrayList<>();
        String query = "SELECT * FROM " + sqLiteUtil.TABLE_NAME;

        sqLiteDatabase = sqLiteUtil.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while(cursor.isAfterLast() == false) {
            ItemFavorited favorited = new ItemFavorited(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            itemFavoriteds.add(favorited);
            cursor.moveToNext();
        }
        return itemFavoriteds;
    }

    public void updateFavoriteItem(ItemFavorited itemFavorited) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(sqLiteUtil.FAV_TITLE, itemFavorited.getTitleFav());
        values.put(sqLiteUtil.FAV_DATE, itemFavorited.getDateFav());
        values.put(sqLiteUtil.FAV_LINK, itemFavorited.getLinkFav());
        values.put(sqLiteUtil.FAV_IMAGE, itemFavorited.getImageFav());
        sqLiteDatabase.update(sqLiteUtil.TABLE_NAME, values, sqLiteUtil.FAV_ID + " = ?", new String[]{String.valueOf(itemFavorited.getIdFAV())});
        sqLiteDatabase.close();
    }

    public void deleteFavoriteItemWithTitle(String titleFav) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_NAME, sqLiteUtil.FAV_TITLE + " = ?", new String[]{titleFav});
        sqLiteDatabase.close();
    }

    public void deleteFavoriteItemWithId(int favId) {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_NAME, sqLiteUtil.FAV_ID + " = ?", new String[]{String.valueOf(favId)});
        sqLiteDatabase.close();
    }

    public void deleteAllFavorite() {
        sqLiteDatabase = sqLiteUtil.getWritableDatabase();
        sqLiteDatabase.delete(sqLiteUtil.TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }
}
