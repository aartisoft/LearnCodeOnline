package com.example.shikharjai.foodapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity{

    private FirebaseRecyclerAdapter firebaseRecyclerAdapter2;
    private FirebaseRecyclerAdapter firebaseSearchResultRecyclerAdapter;

    private String TAG="FoodList";
    private RecyclerView foodListView;
    private MaterialSearchBar searchBar;
    private List<String> suggestList = new ArrayList<>();
    private DatabaseReference databaseReference;
    Toolbar toolbar;
    private String categoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        foodListView = findViewById(R.id.food_recycler_view);
        foodListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        databaseReference = FirebaseDatabase.getInstance().getReference("foodlist");

        if(getIntent()!=null){
            categoryId = getIntent().getStringExtra("category_key");
            if(!categoryId.isEmpty() && categoryId!=null){
                fetch(categoryId);
                loadFoodsToList(categoryId);
            }

        }
        searchBar.setLastSuggestions(suggestList);
        searchBar.setCardViewElevation(10);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> userSuggestions = new ArrayList<>() ;
                for(String user_keywords: suggestList){
                    if(user_keywords.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        userSuggestions.add(user_keywords);
                    }
                }
                searchBar.setLastSuggestions(userSuggestions);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    foodListView.setAdapter(firebaseRecyclerAdapter2);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchFood(text.toString(), categoryId);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void loadFoodsToList(String categoryId) {
        databaseReference.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    FoodModel foodModel = dataSnapshot1.getValue(FoodModel.class);
                    suggestList.add(foodModel.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchFood(String f, String categoryId) {
        Log.d(TAG, "searchFood: "+f);

        Query query = FirebaseDatabase.getInstance().getReference("foodlist").orderByChild("Name").startAt(f).endAt(f+"\uf8ff");
        FirebaseRecyclerOptions<FoodModel> options = new FirebaseRecyclerOptions.Builder<FoodModel>().setQuery(query, new SnapshotParser<FoodModel>() {
            @NonNull
            @Override
            public FoodModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "searchFood: "+ snapshot.getChildrenCount());

                return new FoodModel(snapshot.child("Description").getValue().toString(), snapshot.child("Discount").getValue().toString(), snapshot.child("Image").getValue().toString(), snapshot.child("MenuId").getValue().toString(), snapshot.child("Name").getValue().toString(), snapshot.child("Price").getValue().toString());
            }
        }).build();

        firebaseSearchResultRecyclerAdapter = new FirebaseRecyclerAdapter<FoodModel, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_items, viewGroup, false);
                return new FoodViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull FoodModel model) {
                final int i = position;
                holder.setFoodName(model.getName());
                Log.d(TAG, "onBindViewHolderSearch: "+model.getName());
                holder.setFoodImage(model.getImage());
                holder.setFoodDescription(model.getDescription());
                holder.setItemClickListener(new ItemClickLitener() {
                    @Override
                    public void onClick(View v, int pos, boolean isLongClick) {
                        String food_id = firebaseSearchResultRecyclerAdapter.getRef(i).getKey().toString();
                        Intent i = new Intent(FoodList.this, FoodDetail.class);
                        i.putExtra("food_id", food_id);
                        startActivity(i);
                    }
                });

            }
        };
        firebaseSearchResultRecyclerAdapter.startListening();
        foodListView.setAdapter(firebaseSearchResultRecyclerAdapter);
    }


    private void fetch(String categoryId) {
        Query query = FirebaseDatabase.getInstance().getReference("foodlist").orderByChild("MenuId").equalTo(categoryId);
        FirebaseRecyclerOptions<FoodModel> options = new FirebaseRecyclerOptions.Builder<FoodModel>().setQuery(query, new SnapshotParser<FoodModel>() {
            @NonNull
            @Override
            public FoodModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "parseSnapshot: "+snapshot.child("Discount").getValue().toString());
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
                Log.d(TAG, "onBindViewHolder: "+model.getImage());
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