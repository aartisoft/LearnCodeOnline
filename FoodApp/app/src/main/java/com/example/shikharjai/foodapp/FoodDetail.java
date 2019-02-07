package com.example.shikharjai.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shikharjai.foodapp.Model.FoodModel;
import com.example.shikharjai.foodapp.SqLite.DatabaseAccess;
import com.example.shikharjai.foodapp.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {
    private static final String TAG ="FoodDetail";
    private DatabaseReference databaseReference;
    ImageView detail_pic;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    FloatingActionButton btnFoodCart, showCart;
    NestedScrollView nestedScrollView;
    TextView food_detail_name, food_detail_price, food_details;
    ElegantNumberButton elegantNumberButton;
    private FoodModel foodModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        detail_pic = findViewById(R.id.detail_pic);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        appBarLayout = findViewById(R.id.detail_app_bar_layout);
        toolbar = findViewById(R.id.detail_toolbar);
        btnFoodCart = findViewById(R.id.btnFoodCart);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        food_detail_price = findViewById(R.id.food_detail_price);
        food_detail_name = findViewById(R.id.food_detail_name);
        food_details = findViewById(R.id.foodDetails);
        elegantNumberButton = findViewById(R.id.elagant_btn);
        showCart = findViewById(R.id.addCartBtn);

        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedAppbar);
        if(getIntent()!= null){
            String food_id = getIntent().getStringExtra("food_id");
            if(!food_id.isEmpty() && food_id != null){
                showDetails(food_id);

            }
        }

    }

    private void showDetails(final String food_id) {
        databaseReference = FirebaseDatabase.getInstance().getReference("foodlist");
//        Log.d(TAG, "showDetails: " + databaseReference.child(food_id).getKey().toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(food_id).exists()){

                    foodModel = dataSnapshot.child(food_id).getValue(FoodModel.class);
                    Picasso.get().load(foodModel.getImage()).into(detail_pic);
                    collapsingToolbarLayout.setTitle(foodModel.getName());
                    food_details.setText(foodModel.getDescription());
                    food_detail_name.setText(foodModel.getName());
                    food_detail_price.setText(foodModel.getPrice());


                    btnFoodCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                            databaseAccess.open();

                            Order order = new Order(food_id, foodModel.getName(), foodModel.getPrice(), foodModel.getDiscount(),elegantNumberButton.getNumber() );

                            databaseAccess.addToCart(order);
                            Toast.makeText(FoodDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        }
                    });


                    showCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(FoodDetail.this, ShowCart.class));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
