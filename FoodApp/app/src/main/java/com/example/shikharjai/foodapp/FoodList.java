package com.example.shikharjai.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shikharjai.foodapp.ItemClickListener.ItemClickLitener;
import com.example.shikharjai.foodapp.Model.FoodModel;
import com.example.shikharjai.foodapp.ViewHolderPackage.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FoodList extends AppCompatActivity{

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter2;
    private String TAG="FoodList";
    private RecyclerView foodListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);

        foodListView = findViewById(R.id.food_recycler_view);
        foodListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if(getIntent()!=null){
            String categoryId = getIntent().getStringExtra("category_key");
            if(!categoryId.isEmpty() && categoryId!=null){
                fetch(categoryId);
            }

        }

    }

    private void fetch(String categoryId) {


        Query query = FirebaseDatabase.getInstance().getReference("foodlist").orderByChild("MenuId").equalTo(categoryId);


        FirebaseRecyclerOptions<FoodModel> options = new FirebaseRecyclerOptions.Builder<FoodModel>().setQuery(query, new SnapshotParser<FoodModel>() {
            @NonNull
            @Override
            public FoodModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "parseSnapshot: "+ snapshot.getChildrenCount());

                return new FoodModel(snapshot.child("Description").getValue().toString(), snapshot.child("Discount").getValue().toString(), snapshot.child("Image").getValue().toString(), snapshot.child("MenuId").getValue().toString(), snapshot.child("Name").getValue().toString(), snapshot.child("Price").getValue().toString());
            }
        }).build();


        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<FoodModel, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View v = inflater.inflate(R.layout.food_items, viewGroup, false);
                return new FoodViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, final int position, @NonNull FoodModel model) {
                holder.setFoodName(model.getName());
                holder.setFoodImage(model.getImage());
                holder.setFoodDescription(model.getDescription());
                holder.setItemClickListener(new ItemClickLitener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClick) {
                        String food_id = firebaseRecyclerAdapter2.getRef(position).getKey().toString();
                        Intent i = new Intent(FoodList.this, FoodDetail.class);
                        i.putExtra("food_id", food_id);
                        startActivity(i);
                    }
                });
            }
        };
        foodListView.setAdapter(firebaseRecyclerAdapter2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter2.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter2.stopListening();
    }
}