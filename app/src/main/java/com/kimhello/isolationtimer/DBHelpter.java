package com.kimhello.isolationtimer;

import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

class DBHelpter extends SQLiteOpenHelper {
    private SQLiteDatabase db = null;

    private static final String DB_NAME = "item_db";
    private static final int DB_VERSION = 1;

    private static DBHelpter instance;

    // constructor
    private DBHelpter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        if(db==null) {
            db=getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemDBContract.SQL_DROP_TBL);
        db.execSQL(ItemDBContract.SQL_CREATE_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(ItemDBContract.SQL_DROP_TBL);
        onCreate(db);
    }

    public static DBHelpter getInstance(Context context){
        if (instance==null) {
            synchronized (DBHelpter.class) {
                if(instance==null) {
                    instance = new DBHelpter(context);
                }
            }
        }
        return instance;
    }

    public void insert (int id, String str_item) {
        db.execSQL(ItemDBContract.SQL_INSERT + "(" + id + ", " + '"' + str_item + '"' + ")");
    }

    public void delete(String title) {
        db.execSQL(ItemDBContract.SQL_DELETED + '"' + title + '"');
    }

    public void update(String title, int index) {
        db.execSQL(ItemDBContract.SQL_UPDATE+ '"' + title + '"' + ItemDBContract.SQL_WHERE+ index);
    }

    public ArrayList<String> getAll() {
        ArrayList<String> temp = new ArrayList<>();
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery(ItemDBContract.SQL_SELECT, null);

        while (cursor.moveToNext()) {
            temp.add(cursor.getString(0));
        }
        return temp;
    }
}
