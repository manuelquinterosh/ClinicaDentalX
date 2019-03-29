package com.developer.manuelquinteros.clinicadentalx.model;

public class Notifications {
    int id;
     String mTitle;
     String mDescription;
     String mExpiryDate;
     float mDiscount;

    public static final String TABLE_NOTIFICATIONS = "notifications";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_EXPIRY_DATE = "expiry_date";
    public static final String COLUMN_DISCOUNT = "discount";

    //Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NOTIFICATIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_EXPIRY_DATE + " TEXT,"
                    + COLUMN_DISCOUNT + " TEXT" + ")";

    public Notifications() {
    }

    public Notifications(int id, String mTitle, String mDescription, String mExpiryDate, float mDiscount) {
        this.id = id;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mExpiryDate = mExpiryDate;
        this.mDiscount = mDiscount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmExpiryDate() {
        return mExpiryDate;
    }

    public void setmExpiryDate(String mExpiryDate) {
        this.mExpiryDate = mExpiryDate;
    }

    public float getmDiscount() {
        return mDiscount;
    }

    public void setmDiscount(float mDiscount) {
        this.mDiscount = mDiscount;
    }

}
