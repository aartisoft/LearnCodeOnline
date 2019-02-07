package com.example.shikharjai.foodapp;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.Model.CategoryModel;
import com.example.shikharjai.foodapp.ViewHolderPackage.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class food_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "food_menu";

    private List<String> categoryList = new ArrayList();
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter1;
    private FloatingActionButton floatingActionButton;
    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        TextView navHeaderTitle;
        RecyclerView recyclerView;
        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(food_menu.this, ShowCart.class));
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        navHeaderTitle = (TextView) headerView.findViewById(R.id.nav_header_title); //headerView is defined in the "app:headerLayout"
                                                                                   //attribute of the navigationView

//      navHeaderTitle.setText(User.currentUser.getName()); // Sets the name of the header title to name of the current
                                                                       // logged in user

            Query query = FirebaseDatabase.getInstance().getReference("category");
            recyclerView = findViewById(R.id.recycler_view);

        FirebaseRecyclerOptions<CategoryModel> options = new FirebaseRecyclerOptions.Builder<CategoryModel>().setQuery(query, new SnapshotParser<CategoryModel>() {
            @NonNull
            @Override
            public CategoryModel parseSnapshot(@NonNull DataSnapshot snapshot) {
//                Log.d(TAG, "parseSnapshot: "+snapshot.getChildrenCount());
                return new CategoryModel(snapshot.child("Image").getValue().toString(), snapshot.child("Name").getValue().toString());
             }
        }).build();

            firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<CategoryModel, MenuViewHolder>(options) {
                @NonNull
                @Override
                public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
                    View v = layoutInflater.inflate(R.layout.menu_items, viewGroup, false);
                    return new MenuViewHolder(v);
                }

                @Override
                protected void onBindViewHolder(@NonNull MenuViewHolder holder, final int position, @NonNull final CategoryModel model) {
                    holder.setMenuImage(model.getImage());
                    holder.setMenuText(model.getName());
                    
                    holder.menuImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(food_menu.this, ""+model.getName(), Toast.LENGTH_SHORT).show();
                            String categoryKey = firebaseRecyclerAdapter1.getRef(position).getKey().toString();
                            Intent i = new Intent(food_menu.this, FoodList.class);
                            i.putExtra("category_key", categoryKey);
                            startActivity(i);

                        }
                    });
                    }
            };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
//        Log.d(TAG, "onDataChange: "+categoryList.size());
        firebaseRecyclerAdapter1.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter1.stopListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.food_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(food_menu.this, ShowCart.class));

        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(food_menu.this, MyOrders.class));

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(food_menu.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            User.currentUser = null;
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
