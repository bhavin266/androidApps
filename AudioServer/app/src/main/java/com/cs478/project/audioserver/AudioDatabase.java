package com.cs478.project.audioserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bhavin Chauhan on 11-27-2016.
 */
public class AudioDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Music.db";
    public static final String TABLE_NAME = "tracks";
    public static final String COL_1 = "ACTION";
    public static final String COL_2 = "TIME";

    //Implementing constructor
    public AudioDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("create table " + TABLE_NAME + " (ACTION VARCHAR(50), TIME VARCHAR(50)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);            //create new table
    }

    public boolean insertSong(String action, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, action);
        contentValues.put(COL_2, time);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public String[] getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        String songs[] = new String[1000];
        int i = 0;
        while (cursor.moveToNext()) {
            songs[i] = cursor.getString(0) + " " + cursor.getString(1);
            i++;
        }
        return songs;
    }
}
