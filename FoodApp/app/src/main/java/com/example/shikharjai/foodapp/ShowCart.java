package com.example.shikharjai.foodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.Model.Request;
import com.example.shikharjai.foodapp.ViewHolderPackage.DatabaseAccess;
import com.example.shikharjai.foodapp.ViewHolderPackage.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getAvailableLocales;

public class ShowCart extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Order> orderList;
    private String TAG="ShowCart";
    TextView total_price;
    int total;
    Locale locale;
    private Button placeOrder;
    DatabaseReference requests;
    DatabaseAccess databaseAccess;
    private CartAdapter adapter;

    public ShowCart() {

        total = 0;
        this.orderList = new ArrayList<>();
        locale = new Locale("hi","IN");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);

        Log.d(TAG, "onCreate: "+getAvailableLocales().toString() );
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShowCart.this);
        databaseAccess.open();
        orderList = databaseAccess.getCarts();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowCart.this);


        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(orderList, locale);
        recyclerView.setAdapter(adapter);

        total_price = findViewById(R.id.total_price);
        for(Order order: orderList){
            total += Integer.parseInt(order.getProductPrice())*Integer.parseInt(order.getProductQuantity());
        }
        total_price.setText(Currency.getInstance(locale).getSymbol()+"  "+total);

        placeOrder =findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderList.size()==0){
                    Toast.makeText(ShowCart.this, "You have no items in your cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                askAddress();
            }
        });

        requests = FirebaseDatabase.getInstance().getReference("Requests");

    }

    private void askAddress() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowCart.this);
        alertDialog.setTitle("One More Step !!!");
        alertDialog.setMessage("Enter Your Address");

        final EditText editText = new EditText(ShowCart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(User.currentUser.getUserName(), ""+User.currentUser.getContact(), editText.getText().toString(), ""+total, orderList);

                requests.child(System.currentTimeMillis()+"").setValue(request);
                databaseAccess=DatabaseAccess.getInstance(ShowCart.this);
                databaseAccess.open();
                databaseAccess.cleanCart(); // clearing the sqlite orderdetails table
                orderList.clear();  // as well as the orders fethched from the sqlite orderdetais table
                adapter.notifyDataSetChanged();
                total_price.setText(Currency.getInstance(locale).getSymbol()+"  "+0);
                Toast.makeText(ShowCart.this, "Thanks " + User.currentUser.getName()+" for placing the order.", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
