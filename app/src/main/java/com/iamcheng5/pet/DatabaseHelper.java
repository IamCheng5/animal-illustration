package com.iamcheng5.pet;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/" + "com.iamcheng5.pet" + "/databases/";
    private static String DB_NAME = "animal.db";

    private final Context mCtx;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mCtx = context;
    }

    public boolean createDatabase() {
        boolean dbExist = checkDatabase();
        this.getReadableDatabase();
        if (!dbExist) {
            return copyDatabase();
        }
        return true;
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        String dbpath = DB_PATH + DB_NAME;
        try {
            checkDB = SQLiteDatabase.openDatabase(dbpath,
                    null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            return false;
        }
        if (checkDB != null) {
            checkDB.close();
            return true;
        }
        return false;
    }

    private boolean copyDatabase() {
        try {
            InputStream input = mCtx.getAssets().open(DB_NAME);
            this.getReadableDatabase();
            String outFileName = DB_PATH + DB_NAME;
            OutputStream output =
                    new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
    }
}