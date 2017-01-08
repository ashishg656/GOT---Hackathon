package com.ashishgoel.got.sqlite.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.ashishgoel.got.extras.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashish on 12/09/16.
 */
public class SearchSqliteHelper extends SQLiteOpenHelper {

    int countToKeep = 15;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SearchEntry.TABLE_NAME + " (" +
                    SearchEntry._ID + " INTEGER PRIMARY KEY," +
                    SearchEntry.COLUMN_NAME_TITLE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SearchEntry.TABLE_NAME;

    public static class SearchEntry implements BaseColumns {
        public static final String TABLE_NAME = "search";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public SearchSqliteHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addItem(String text) {
        try {
            text = text.trim();

            List<SearchSqliteObject> pastSearches = getAllSearches();
            boolean found = false;
            if (pastSearches != null && pastSearches.size() > 0) {
                for (SearchSqliteObject obj : pastSearches) {
                    if (obj.getSearchText().equalsIgnoreCase(text)) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                deleteLastSearchItem();

                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(SearchEntry.COLUMN_NAME_TITLE, text);

                long newRowId = db.insert(SearchEntry.TABLE_NAME, null, values);

                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SearchSqliteObject> getAllSearches() {
        List<SearchSqliteObject> searches = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                SearchEntry._ID,
                SearchEntry.COLUMN_NAME_TITLE,
        };

        Cursor cursor = db.query(SearchEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                SearchSqliteObject search = new SearchSqliteObject();
                search.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SearchEntry._ID)));
                search.setSearchText(cursor.getString(cursor.getColumnIndexOrThrow(SearchEntry.COLUMN_NAME_TITLE)));
                searches.add(search);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return searches;
    }

    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                SearchEntry._ID,
        };

        Cursor cursor = db.query(SearchEntry.TABLE_NAME, projection, null, null, null, null, null);
        try {
            int count = cursor.getCount();

            cursor.close();
            db.close();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteLastSearchItem() {
        int count = getCount();
        if (count >= countToKeep) {
            String sortOrder = SearchEntry._ID + " ASC";

            SQLiteDatabase db = this.getReadableDatabase();

            String[] projection = {
                    SearchEntry._ID,
            };

            Cursor cursor = db.query(SearchEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);

            cursor.moveToFirst();

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(SearchEntry._ID));

            deleteItem(id);

            cursor.close();
            db.close();
        }
    }

    public void deleteAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SearchEntry.TABLE_NAME, null, null);
    }

    private void deleteItem(int id) {
        String selection = SearchEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SearchEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
