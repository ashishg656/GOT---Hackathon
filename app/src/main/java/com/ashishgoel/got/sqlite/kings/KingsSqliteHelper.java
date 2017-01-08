package com.ashishgoel.got.sqlite.kings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ashishgoel.got.extras.AppConstants;
import com.ashishgoel.got.objects.kingDetails.KingDetailsObject;
import com.ashishgoel.got.utils.VolleyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish on 12/09/16.
 */
public class KingsSqliteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchEntry.TABLE_NAME + "(" +
                    SearchEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    SearchEntry.COLUMN_NAME_DETAIL_OBJ + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchEntry.TABLE_NAME;

    public static class SearchEntry implements BaseColumns {
        public static final String TABLE_NAME = "kings";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DETAIL_OBJ = "detail_obj";
    }

    public KingsSqliteHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void setData(List<KingDetailsObject> data) {
        try {
            if (data != null) {
                deleteAllItems();
                SQLiteDatabase db = this.getWritableDatabase();
                for (KingDetailsObject obj : data) {
                    ContentValues values = new ContentValues();
                    values.put(SearchEntry.COLUMN_NAME_TITLE, obj.getName());
                    values.put(SearchEntry.COLUMN_NAME_DETAIL_OBJ, VolleyUtils.getStringFromObject(obj));

                    long newRowId = db.insert(SearchEntry.TABLE_NAME, null, values);
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<KingDetailsObject> searchKings(String query) {
        List<KingDetailsObject> searches = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] projection = {
                    SearchEntry._ID,
                    SearchEntry.COLUMN_NAME_TITLE,
                    SearchEntry.COLUMN_NAME_DETAIL_OBJ
            };

            String[] queryArray = {""};
            queryArray[0] = query;

            String where = SearchEntry.COLUMN_NAME_TITLE + " LIKE '%" + query + "%'";

            Cursor cursor = db.query(SearchEntry.TABLE_NAME, projection, where, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String jsonString = cursor.getString(cursor.getColumnIndexOrThrow(SearchEntry.COLUMN_NAME_DETAIL_OBJ));
                    KingDetailsObject object = (KingDetailsObject) VolleyUtils.getResponseObject(jsonString, KingDetailsObject.class);
                    searches.add(object);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searches;
    }

    public List<KingDetailsObject> getAllKings() {
        List<KingDetailsObject> searches = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String[] projection = {
                    SearchEntry._ID,
                    SearchEntry.COLUMN_NAME_TITLE,
                    SearchEntry.COLUMN_NAME_DETAIL_OBJ
            };

            Cursor cursor = db.query(SearchEntry.TABLE_NAME, projection, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    String jsonString = cursor.getString(cursor.getColumnIndexOrThrow(SearchEntry.COLUMN_NAME_DETAIL_OBJ));
                    KingDetailsObject object = (KingDetailsObject) VolleyUtils.getResponseObject(jsonString, KingDetailsObject.class);
                    searches.add(object);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return searches;
    }

    void deleteAllItems() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(SearchEntry.TABLE_NAME, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
