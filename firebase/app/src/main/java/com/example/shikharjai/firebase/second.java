package com.example.shikharjai.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class second extends AppCompatActivity{
    android.support.v7.widget.Toolbar tl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tl = (Toolbar) findViewById(R.id.tl);
        setSupportActionBar(tl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(second.this, firebase.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
