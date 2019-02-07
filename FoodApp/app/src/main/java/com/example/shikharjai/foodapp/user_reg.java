package com.example.shikharjai.foodapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikharjai.foodapp.Model.userModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_reg extends AppCompatActivity{
    public static final String TAG = "user_REG";
    EditText fullName, userName, mobileNumber, location, userPass, confirmPass;
    CheckBox confirm;
    Button register;
    TextView oldUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg);

        fullName = findViewById(R.id.fullName);
        mobileNumber = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        userName = findViewById(R.id.user_name);
        userPass = findViewById(R.id.user_password);
        confirmPass = findViewById(R.id.confirmPassword);


        confirm = (CheckBox) findViewById(R.id.terms_conditions);

        register = findViewById(R.id.signUpBtn);
        oldUser = findViewById(R.id.already_user);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onDataChange: if fire.......d");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(userName.getText().toString()).exists()){
                            Log.d(TAG, "onDataChange: if fired");
                        } else {
                            Log.d(TAG, "onDataChange: else fired");
                            if(confirmPass.getText().toString().trim().equals(userPass.getText().toString().trim())){
                                userModel userModel = new userModel(fullName.getText().toString(), userPass.getText().toString(), Long.valueOf(mobileNumber.getText().toString()));
                                databaseReference.child(userName.getText().toString()).setValue(userModel);
                                Toast.makeText(user_reg.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(user_reg.this, "Your Passwords do not match", Toast.LENGTH_SHORT).show();
                            }
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
