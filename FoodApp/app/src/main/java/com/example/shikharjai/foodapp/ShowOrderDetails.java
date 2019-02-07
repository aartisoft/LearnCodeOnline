package com.example.shikharjai.foodapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.Model.TimeLineModel;
import com.example.shikharjai.foodapp.Model.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShowOrderDetails extends AppCompatActivity {

    private static final String TAG = "ShowOrderDetails";
    private String Name;
    private String Phone;
    private String Address;
    private String Total;
    private int orderStatus;
    private ArrayList<Order> orderList;
    private RecyclerView  timeLineRecyclerView;
    private ArrayList<TimeLineModel> mDataList = new ArrayList<>();
    private String order_id;

    TextView orderId;
    TextView deliveryAddress;
    TextView order_date;
    TextView order_total;
    TextView total_items;
    private RecyclerView ordered_items;
    private Locale locale = new Locale("hi","IN");
    private TextView delivered_to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_details);
        if(getIntent()!=null){
           Name = getIntent().getStringExtra("user");
           Phone = getIntent().getStringExtra("phone");
           Address = getIntent().getStringExtra("address");
           Total = getIntent().getStringExtra("total");
           orderStatus = getIntent().getIntExtra("status",0);

           orderList = getIntent().getParcelableArrayListExtra("order_bundle");
           order_id = getIntent().getStringExtra("order_id");

        }

        orderId = (TextView)findViewById(R.id.order_id);
        deliveryAddress = findViewById(R.id.delivery_address);
        delivered_to = findViewById(R.id.delivered_to);
        order_date = findViewById(R.id.order_date);
        order_total = findViewById(R.id.order_total);
        total_items = findViewById(R.id.total_items);
        orderId.setText("Order# "+order_id);
        deliveryAddress.setText("Delivery Address: "+Address);

        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
        Date date = new Date(Long.valueOf(order_id));
        order_date.setText("Ordered on: "+simple.format(date)+" IST");

        order_total.setText(Currency.getInstance(locale).getSymbol()+ " " + Total);

        total_items.setText(orderList.size()+" item/items");
        ordered_items = findViewById(R.id.ordered_items);
        ordered_items.setLayoutManager(new LinearLayoutManager(ShowOrderDetails.this));
        Log.e(TAG, "onCreate: "+orderList.size() );
        ordered_items.setAdapter(new CartAdapter(orderList, locale));
        delivered_to.setText("Delivered to: "+(orderStatus == 3 ? User.currentUser.getName():"Not yet delivered"));

//        setDataListItems();
//        timeLineRecyclerView = findViewById(R.id.timeline_rec);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        timeLineRecyclerView.setLayoutManager(linearLayoutManager);
//        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(mDataList);
//        timeLineRecyclerView.setAdapter(recyclerAdapter);
    }

    private void setDataListItems() {
        mDataList.add(new TimeLineModel("Item successfully delivered", "", "INACTIVE"));
        mDataList.add(new TimeLineModel("Courier is out to delivery your order", "2017-02-12 08:00", "ACTIVE"));
        mDataList.add(new TimeLineModel("Item has reached courier facility at New Delhi", "2017-02-11 21:00", "COMPLETED"));
        mDataList.add(new TimeLineModel("Item has been given to the courier", "2017-02-11 18:00", "COMPLETED"));
        mDataList.add(new TimeLineModel("Item is packed and will dispatch soon", "2017-02-11 09:30", "COMPLETED"));
        mDataList.add(new TimeLineModel("Order is being readied for dispatch", "2017-02-11 08:00", "COMPLETED"));
        mDataList.add(new TimeLineModel("Order processing initiated", "2017-02-10 15:00", "COMPLETED"));
        mDataList.add(new TimeLineModel("Order confirmed by seller", "2017-02-10 14:30", "COMPLETED"));
        mDataList.add(new TimeLineModel("Order placed successfully", "2017-02-10 14:00", "COMPLETED"));
    }


    private String convertToStatus(int order_status) {
        String str;
        switch (order_status){
            case 0 : str = "Placed";
                break;
            case 1 : str = "Shipped";
                break;
            case 2 : str = "Reached to Recepient's City";
                break;
            case 3 : str = "Delivered";
                break;
            default: str = "Pending";
                break;
        }
        return str;
    }}
