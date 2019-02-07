package com.example.shikharjai.foodapp.SqLite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.shikharjai.foodapp.Model.Order;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class DatabaseAccess {
    private DatabaseOpenHelper databseOpenHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;


    private DatabaseAccess(Context c){
        databseOpenHelper = new DatabaseOpenHelper(c);
    }

    public static DatabaseAccess getInstance(Context context){
        if(instance == null){
                instance = new DatabaseAccess(context);
            }
        return instance;
    }

    public void open(){
        db = databseOpenHelper.getWritableDatabase();
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public void addToCart(Order order){
        String insert_query = String.format("INSERT INTO OrderDetails(ProductID, ProductName, ProductPrice, ProductDiscount, ProductQuantity) VALUES('%s', '%s', '%s', '%s', '%s');", order.getProductId(), order.getProductName(), order.getProductPrice(), order.getProductDiscount(), order.getProductQuantity());
    try{
        db.execSQL(insert_query);
        Log.d(TAG, "Successfully executed");;
    } catch (SQLException s){
        Log.d(TAG, "addToCart: "+s.toString());;
    }
    }

    public ArrayList<Order> getCarts(){
        String columns[] = {"ProductID, ProductName, ProductPrice, ProductDiscount, ProductQuantity"};
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables("OrderDetails");
        Cursor cursor = sqLiteQueryBuilder.query(db, columns, null, null, null, null, null);
        ArrayList<Order> orders = new ArrayList<>();
        Log.e(TAG, "getCarts: "+cursor.getCount() );
        if(cursor.moveToFirst()){
            do{
                try {
                    orders.add(new Order(cursor.getString(cursor.getColumnIndex("ProductID")), cursor.getString(cursor.getColumnIndex("ProductName")), cursor.getString(cursor.getColumnIndex("ProductPrice")), cursor.getString(cursor.getColumnIndex("ProductDiscount")), cursor.getString(cursor.getColumnIndex("ProductQuantity") )));
                } catch (Exception e){
                    Log.e(TAG, "getCarts: "+e.toString() );
                }
            } while (cursor.moveToNext());
        }
        return orders;
    }

    public void cleanCart() {
        String cleanQuery = String.format("DELETE FROM OrderDetails");
        db.execSQL(cleanQuery);
    }
}
