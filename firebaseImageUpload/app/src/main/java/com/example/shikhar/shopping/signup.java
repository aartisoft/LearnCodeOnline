package com.example.shikhar.shopping;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vyanktesh.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText email;
    Button signup,loginAgain;
    ProgressBar progressBar2;
    EditText password,password1;
    DatabaseReference Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

mAuth=FirebaseAuth.getInstance();

        email=findViewById(R.id.Email1);
        password=findViewById(R.id.pwd1);
        password1=findViewById(R.id.pwd1c);
        signup=findViewById(R.id.signup);
        loginAgain=findViewById(R.id.loginAgain);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.INVISIBLE);


        final DatabaseReference Email= FirebaseDatabase.getInstance().getReference("Users");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=email.getText().toString();
                String Password=password.getText().toString();
                String Password1=password1.getText().toString();
                if (!TextUtils.isEmpty(Email)&&!TextUtils.isEmpty(Password)&&!TextUtils.isEmpty(Password1)){
                    if (Password.equals(Password1)){
                        progressBar2.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isComplete()){
                                    Intent intent=new Intent(signup.this,SetUpActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    String errorMessage=task.getException().getMessage();
                                    Toast.makeText(signup.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        progressBar2.setVisibility(View.INVISIBLE);
                        Toast.makeText(signup.this,"Both passwords do not match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        loginAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null){
            Intent intent=new Intent(signup.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
