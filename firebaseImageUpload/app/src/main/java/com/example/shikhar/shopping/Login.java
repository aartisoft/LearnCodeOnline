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
import android.widget.TextView;
import android.widget.Toast;

import com.example.vyanktesh.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button login;
    EditText email,pwd;
    ProgressBar loginProgress;
    TextView signup;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();

        signup=findViewById(R.id.signup);
        email=findViewById(R.id.Email);
        pwd=findViewById(R.id.pwd);
        login=findViewById(R.id.Login);
        loginProgress=findViewById(R.id.loginProgress);
        loginProgress.setVisibility(View.INVISIBLE);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,signup.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail=email.getText().toString();
                String loginPassword=pwd.getText().toString();

                if (!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPassword)){
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent mainIntent=new Intent(Login.this,SetUpActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }else {
                                String errorMessage=task.getException().getMessage();
                                Toast.makeText(Login.this,"Error: "+errorMessage,Toast.LENGTH_SHORT).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser != null){
            sendtoMain();
        }
    }

    private void sendtoMain() {
        Intent mainIntent=new Intent(Login.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
