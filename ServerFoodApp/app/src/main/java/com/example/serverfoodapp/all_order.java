    package com.example.serverfoodapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.serverfoodapp.Common.User;
import com.example.serverfoodapp.Model.Order;
import com.example.serverfoodapp.Model.Request;
import com.example.serverfoodapp.ViewHolder.MyOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class all_order extends food_menu implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "all_orders";
    private String spinner_text;
    private FirebaseRecyclerAdapter<Request, MyOrdersViewHolder> firebaseRecyclerAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_all_order, null, false);
        drawer.addView(contentView);
        floatingActionButton.hide();
        databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
        if(User.currentUser == null){
            Toast.makeText(this, "User Logged Out. Kindly Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        fetchAllOrders();
    }

    private void fetchAllOrders() {
        Query query = FirebaseDatabase.getInstance().getReference("Requests");
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(query, new SnapshotParser<Request>() {
            @NonNull
            @Override
            public Request parseSnapshot(@NonNull DataSnapshot snapshot) {

                ArrayList<Order> orders = new ArrayList<>();
//                Log.d(TAG, "parseSnapshot: "+snapshot.child("requestedOrderList").getChildrenCount());

                for(DataSnapshot snapshot1: snapshot.child("requestedOrderList").getChildren()){
                    orders.add(snapshot1.getValue(Order.class));
                }

                return new Request(snapshot.child("name").getValue().toString(), snapshot.child("phone").getValue().toString(), snapshot.child("address").getValue().toString(), snapshot.child("total").getValue().toString(), orders, Integer.parseInt(snapshot.child("orderStatus").getValue().toString()));

            }
        }).build();

         firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Request, MyOrdersViewHolder>(options) {
            @NonNull
            @Override
            public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View v = inflater.inflate(R.layout.my_order_template, viewGroup, false);
                return new MyOrdersViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyOrdersViewHolder holder, final int position, @NonNull final Request model) {
                Log.d(TAG, "onBindViewHolder: ");
                holder.setOrder_id(firebaseRecyclerAdapter.getRef(position).getKey());
                holder.setOrder_status(model.getOrderStatus());

                holder.setOrder_items(model.getRequestedOrderList().size()+"");
                holder.setOrderTotal(model.getTotal());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(all_order.this, MapsActivity.class);
                        User.currentRequest = model;
                        startActivity(intent);
//                        Toast.makeText(all_order.this, ""+firebaseRecyclerAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
         recyclerView.setAdapter(firebaseRecyclerAdapter);
}

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "onContextItemSelected: "+item.getTitle());
        if(item.getTitle().equals("Update Order")) {
            updateOrder(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey(), firebaseRecyclerAdapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals("Delete Order")){
            deleteOrder(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOrder(final String key) {
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(all_order.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(all_order.this);
        }
        alert.setMessage("Sure to delete Order");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(key).removeValue();
                firebaseRecyclerAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void updateOrder(final String itemId, final Request item)  {
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            alert = new AlertDialog.Builder(all_order.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alert = new AlertDialog.Builder(all_order.this);
        }

        View view = LayoutInflater.from(all_order.this).inflate(R.layout.change_order_status, null,false);
        final Spinner spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(all_order.this);
        String[] categories = {"Placed", "Shipped", "Reached to Recepient's City", "Delivered"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(all_order.this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        alert.setView(view);
        alert.setMessage("Sure to Update Order Status?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setOrderStatus((User.convertStatusToDigit(spinner_text)));
                databaseReference.child( itemId).setValue(item) ;
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_text = parent.getItemAtPosition(position).toString();
        Toast.makeText(all_order.this, ""+spinner_text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
