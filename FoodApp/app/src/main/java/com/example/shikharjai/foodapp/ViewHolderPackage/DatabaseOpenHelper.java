package com.example.shikharjai.foodapp.ViewHolderPackage;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public static String databaseName = "ShoppAppDB.db";
    public static int databaseVersion = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, databaseName,null, databaseVersion);
    }
}
