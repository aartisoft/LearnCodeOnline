package com.example.shikharjai.foodapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.ItemClickListener.ItemClickLitener;
import com.example.shikharjai.foodapp.Model.Request;
import com.example.shikharjai.foodapp.ViewHolderPackage.MyOrdersViewHolder;
import com.example.shikharjai.foodapp.Model.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    private static final String TAG = "MyOrders";
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private ArrayList<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        RecyclerView myOrderList = findViewById(R.id.myOrderList);
        Query query = FirebaseDatabase.getInstance().getReference("Requests").orderByChild("name").equalTo(User.currentUser.getUserName());

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(query, new SnapshotParser<Request>() {
            @NonNull
            @Override
            public Request parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onCreate: "+snapshot.getChildrenCount());
                orderList = new ArrayList<>();
                for(DataSnapshot orderSnapshot: snapshot.child("requestedOrderList").getChildren()){
                    orderList.add(orderSnapshot.getValue(Order.class));
                }

                return new Request(snapshot.child("name").getValue().toString(), snapshot.child("phone").getValue().toString(), snapshot.child("address").getValue().toString(), snapshot.child("total").getValue().toString(),  orderList);
            }
        }).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, MyOrdersViewHolder>(options){
            @NonNull
            @Override
            public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_template, viewGroup, false);
                return new MyOrdersViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position, @NonNull final Request model) {
                Log.d(TAG, "onCreate: "+position);
                final String order_id = firebaseRecyclerAdapter.getRef(position).getKey();
                holder.setOrder_id(firebaseRecyclerAdapter.getRef(position).getKey());
                holder.setOrder_status(model.getOrderStatus());
                holder.setOrder_items(model.getRequestedOrderList().size()+"");
                holder.setOrderTotal(model.getTotal());
                holder.setItemClickLitener(new ItemClickLitener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClick) {
                        Intent i = new Intent(
                                MyOrders.this, ShowOrderDetails.class);
                        i.putExtra("order_id", order_id);
                        i.putExtra("user", model.getName());
                        i.putExtra("total", model.getTotal());
                        i.putExtra("address", model.getAddress());
                        i.putExtra("status", model.getOrderStatus());
                        i.putExtra("phone", model.getPhone());

                        i.putParcelableArrayListExtra("order_bundle", orderList); //Passing an arraylist of objects to another activity via Parceable interfce

                         startActivity(i);

                    }
                });
            }
        };
        myOrderList.setLayoutManager(new LinearLayoutManager(MyOrders.this));
        myOrderList.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
