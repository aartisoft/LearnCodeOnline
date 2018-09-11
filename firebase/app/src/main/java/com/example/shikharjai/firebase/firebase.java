 package com.example.shikharjai.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

 public class firebase extends AppCompatActivity {

    private int RC_SIGN_IN=1002;
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton click;
    public  String TAG="MainActivity";

@Override
public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.

    FirebaseUser currentUser = mAuth.getCurrentUser();

     /*if (currentUser.getEmail().isEmpty()){

     }else{
         startActivity(new Intent(firebase.this,second.class));
         finish();
     }*/


}

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();

         if(mAuth.getCurrentUser()!=null){
             startActivity(new Intent(this,second.class));
         }

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        click = findViewById(R.id.signin);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }


     private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }




     private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//         Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

         AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
         mAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             //Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                             //FirebaseUser user = mAuth.getCurrentUser();
                             startActivity(new Intent(firebase.this,second.class));
                             finish();
                         } else {
                             Toast.makeText(firebase.this, "error", Toast.LENGTH_SHORT).show();
                         }


                     }
                 });
     }


}
