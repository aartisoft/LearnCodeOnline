package com.example.serverfoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.serverfoodapp.Common.User;
import com.example.serverfoodapp.Model.userModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    private static final String TAG = "Login.java";
    EditText username, pswd;
    Button loginBtn, signupBtn, adminBtn;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        pswd = (EditText) findViewById(R.id.password);
        loginBtn = findViewById(R.id.login);
        signupBtn = findViewById(R.id.signup);
        adminBtn = findViewById(R.id.admin);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, user_reg.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: lgon");
                final String userName = username.getText().toString().trim();
                final String userPswd = pswd.getText().toString();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: "+dataSnapshot.getChildrenCount());

                        if(dataSnapshot.child(userName).exists()){
                            userModel userModel = dataSnapshot.child(userName)
                                    .getValue(userModel.class);
                            if(userModel.isStaff()){

                                if(userModel.getPassword().equals(userPswd)){
                                    User.currentUser = userModel;
                                    userModel.setUserName(userName);

                                    Log.d(TAG, "onDataChange: "+userModel.getName()+"  "+userModel.getUserName()+"  "+userModel.getPassword()+"  "+userModel.getContact());
                                    Toast.makeText(Login.this, "Welcome "+ User.currentUser.getName(), Toast.LENGTH_SHORT);

                                    startActivity(new Intent(Login.this, food_menu.class));
                                } else {
                                    Toast.makeText(Login.this, "Incorrect Password Typed", Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                Toast.makeText(Login.this, "Kindly login with Staff Account", Toast.LENGTH_SHORT).show();

                            }
                    } else {
                        Toast.makeText(Login.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
