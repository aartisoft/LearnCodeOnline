package com.example.shikharjai.contactsyn;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class conDatabase extends SQLiteOpenHelper {
    Context context;
    public static String DATABASE_NAME = "offline_db.db";
    public static int DATABASE_VERSION = 1;
    public static String DATABASE_TABLE = "contact_table";
    public static String CONTACT_ID = "contact_id";
    public static String CONTACT_UID = "contact_uid";
    public static String CONTACT_NAME = "contact_name";
    public static String CONTACT_EMAIL = "contact_email";
    public static String CONTACT_NUMBER = "contact_number";
    public static String CONTACT_ADDRESS = "contact_address";

    public conDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DATABASE_TABLE + "( "
                + CONTACT_ID+" varchar(255) PRIMARY KEY,"
                + CONTACT_UID+" varchar(255) ,"
                + CONTACT_NAME+" varchar(255),"
                + CONTACT_NUMBER+" varchar(20),"
                + CONTACT_EMAIL+" varchar(255),"
                + CONTACT_ADDRESS+" varchar(255)"
                + ") ; ";

        db.execSQL(createTable);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addContact(Contact contact) {
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_UID, contact.getContact_Uid());
        cv.put(CONTACT_NAME, contact.getContact_name());
        cv.put(CONTACT_NUMBER, contact.getContact_number());
        cv.put(CONTACT_EMAIL, contact.getContact_email());
        cv.put(CONTACT_ADDRESS, contact.getContact_add());

        boolean h = db1.insert(DATABASE_TABLE, null, cv) != -1;
        return h;
    }

    public void deleteAll(){
        SQLiteDatabase db1 = getWritableDatabase();
        db1.execSQL("delete from "+DATABASE_TABLE);
    }

    public List<Contact> getContact(){
        List<Contact> fetched_con = new ArrayList<>();

        SQLiteDatabase db2 = getReadableDatabase();
        Cursor c= db2.rawQuery("SELECT * FROM "+ DATABASE_TABLE, null);
        if(c.moveToFirst()){
            do{
                fetched_con.add(new Contact(c.getString(1), c.getString(2),c.getString(3),c.getString(4), c.getString(5)));
            } while (c.moveToNext());
        }
        return fetched_con;
    }
}
