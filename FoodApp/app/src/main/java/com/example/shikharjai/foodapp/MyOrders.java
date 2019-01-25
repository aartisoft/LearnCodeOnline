package com.example.shikharjai.foodapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.ItemClickListener.ItemClickLitener;
import com.example.shikharjai.foodapp.Model.Request;
import com.example.shikharjai.foodapp.ViewHolderPackage.MyOrdersViewHolder;
import com.example.shikharjai.foodapp.ViewHolderPackage.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends AppCompatActivity {

    private static final String TAG = "MyOrders";
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

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

                return new Request(snapshot.child("name").getValue().toString(), snapshot.child("phone").getValue().toString(), snapshot.child("address").getValue().toString(), snapshot.child("total").getValue().toString(), (List<Order>) snapshot.child("requestedOrderList").getValue());
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
            protected void onBindViewHolder(@NonNull MyOrdersViewHolder holder, int position, @NonNull Request model) {
                Log.d(TAG, "onCreate: "+position);
                holder.setOrder_id(firebaseRecyclerAdapter.getRef(position).getKey());
                holder.setOrder_status(model.getOrderStatus());
                holder.setOrder_items(model.getRequestedOrderList().size()+"");
                holder.setOrderTotal(model.getTotal());
                holder.setItemClickLitener(new ItemClickLitener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClick) {

                        Toast.makeText(MyOrders.this, ""+"Pending Action", Toast.LENGTH_SHORT).show();
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
