package com.mydarkappfactory.bpitextracurriculars;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "BPIT_EXTRAS";
    private static final int DB_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertEmailPassword(SQLiteDatabase db, String email, String password) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("EMAIL", email);
        recordValues.put("PASSWORD", password);
        db.insert("EMAIL_PASSWORD", null, recordValues);
    }

    private static void insertIsLoggedIn(SQLiteDatabase db, int ans) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("ANSWER", ans);
        db.insert("IS_LOGGED_IN", null, recordValues);
    }

    private static void insertIsFirstLogin(SQLiteDatabase db, int ans) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("ANSWER", ans);
        db.insert("IS_FIRST_LOGIN", null, recordValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVer, int newVer) {
        if (oldVer < 1) {
//            db.execSQL("CREATE TABLE APPETIZERS(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "NAME TEXT, "
//                    + "DESCRIPTION TEXT, "
//                    + "PRICE INTEGER, "
//                    + "RATING INTEGER, "
//                    + "QUANTITY INTEGER, "
//                    + "IMG_RES_ID INTEGER);");

            db.execSQL("CREATE TABLE EMAIL_PASSWORD(_id INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT, PASSWORD TEXT);");

            db.execSQL("CREATE TABLE IS_LOGGED_IN(_id INTEGER PRIMARY KEY AUTOINCREMENT, ANSWER INTEGER);");

            db.execSQL("CREATE TABLE IS_FIRST_LOGIN(_id INTEGER PRIMARY KEY AUTOINCREMENT, ANSWER INTEGER);");



            insertEmailPassword(db, "null", "null");
            insertIsLoggedIn(db, -1);
            insertIsFirstLogin(db, 0);
        }

        if (oldVer < 2) {
            //TODO
        }
    }

}
