package com.developer.manuelquinteros.clinicadentalx.prefs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.developer.manuelquinteros.clinicadentalx.model.Notifications;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final  String DATABASE_NAME = "notifications_db";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create crud table
        db.execSQL(Notifications.CREATE_TABLE);
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Notifications.TABLE_NOTIFICATIONS);

        //Create tables again
        onCreate(db);
    }


    public long insertNotification(String title, String description, String expiry, String discount) {
        //get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Notifications.COLUMN_TITLE, title);
        values.put(Notifications.COLUMN_DESCRIPTION, description);
        values.put(Notifications.COLUMN_EXPIRY_DATE, expiry);
        values.put(Notifications.COLUMN_DISCOUNT, discount);

        //insert row
        long id = db.insert(Notifications.TABLE_NOTIFICATIONS, null, values);

        //close db connection
        db.close();

        //return newly inserted row id
        return id;
    }


    public Notifications getNotification(long id) {
        //get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Notifications.TABLE_NOTIFICATIONS,
                new String[]{Notifications.COLUMN_ID, Notifications.COLUMN_TITLE, Notifications.COLUMN_DESCRIPTION, Notifications.COLUMN_EXPIRY_DATE, Notifications.COLUMN_DISCOUNT}, Notifications.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        //prepare crud object
        Notifications notifications = new Notifications(
                cursor.getInt(cursor.getColumnIndex(Notifications.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_EXPIRY_DATE)),
                cursor.getFloat(cursor.getColumnIndex(Notifications.COLUMN_DISCOUNT)));
        //close the db connection
        cursor.close();

        return notifications;

    }

    public List<Notifications> getAllNotifications() {
        List<Notifications> allnotification = new ArrayList<>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + Notifications.TABLE_NOTIFICATIONS + " ORDER BY " + Notifications.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Notifications notifications = new Notifications();
                notifications.setId(cursor.getInt(cursor.getColumnIndex(Notifications.COLUMN_ID)));
                notifications.setmTitle(cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_TITLE)));
                notifications.setmDescription(cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_DESCRIPTION)));
                notifications.setmExpiryDate(cursor.getString(cursor.getColumnIndex(Notifications.COLUMN_EXPIRY_DATE)));
                notifications.setmDiscount(cursor.getFloat(cursor.getColumnIndex(Notifications.COLUMN_DISCOUNT)));
                allnotification.add(notifications);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return crud list
        return allnotification;
    }

    public void deleteNotifications(Notifications data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Notifications.TABLE_NOTIFICATIONS, Notifications.COLUMN_ID + " = ?",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }

}
