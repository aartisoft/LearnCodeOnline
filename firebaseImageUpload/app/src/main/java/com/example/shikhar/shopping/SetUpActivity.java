package com.example.shikhar.shopping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.vyanktesh.shopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetUpActivity extends AppCompatActivity {

    CircleImageView setUpImage;
    Uri mainImageUri=null;
    String userId;
    Toolbar setUpToolbar;
    EditText name;
    boolean isChanged=false;
    Button button;
    ImageView setupimageforprofile;
    ProgressBar progressBar;

    //We are not using the firebase storage we are directly using storage refrence
    StorageReference storageReference;
    //We are using firebaseAuth to get the userid as the image that we are gonna store is with the name of userid
    FirebaseAuth firebaseAuth;
    //step 1 to add data to add to our firestore;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        setUpToolbar = findViewById(R.id.setUpToolBAr);
        progressBar=findViewById(R.id.progressBar3);
        setSupportActionBar(setUpToolbar);
        getSupportActionBar().setTitle("Account Setup");

        setUpImage = findViewById(R.id.setupimage);
        name=findViewById(R.id.Name);
        button=findViewById(R.id.Button);
        setupimageforprofile=findViewById(R.id.setupimage);
        progressBar.setVisibility(View.INVISIBLE);

        firebaseAuth=FirebaseAuth.getInstance();

        //Retrieving the data step 2
        userId=firebaseAuth.getCurrentUser().getUid();
        //step 2 initialising firebaseFirestore
        firebaseFirestore=FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();



        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        //Retrieving the data step 1
        firebaseFirestore.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    //This task.getResult.exists will check weather it will check that [firebaseFirestore.collection("Users").document(userId)] this path exists or not
                    if (task.getResult().exists()){
                        String namei=task.getResult().getString("name");
                        String imagei=task.getResult().getString("image");

                        mainImageUri=Uri.parse(imagei);

                        name.setText(namei);

                        RequestOptions placeholder=new RequestOptions();
                        placeholder.placeholder(R.drawable.food13);
                        Glide.with(SetUpActivity.this).setDefaultRequestOptions(placeholder).load(imagei).into(setupimageforprofile);
                        //Toast.makeText(SetUpActivity.this,"Data exists",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SetUpActivity.this,"Data does not exists",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String error=task.getException().getMessage();
                    Toast.makeText(SetUpActivity.this,"(Firestore Retrieving Error) :"+error,Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = name.getText().toString();
                if (!TextUtils.isEmpty(userName) && mainImageUri != null) {
                if (isChanged) {


                        //we are not going to make userId a member variable because i don't think we are gonna use it anywhere else
                        userId = firebaseAuth.getCurrentUser().getUid();

                        progressBar.setVisibility(View.VISIBLE);
                        //root of the firebase storage will have a folder with name profile_images which will have an image with name userId.jpg
                        StorageReference imagepath = storageReference.child("profile_images").child(userId + ".jpg");
                        //Then to store the file Uri into that path what we can do is putFile||here you need to pass the Uri which is mainImageUri and the you just set addOnCompleteListner
                        imagepath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFirestore(task, userName);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetUpActivity.this, "(Image Error) :" + error, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
                    }else storeFirestore(null,userName);

                }
            }
        });
        setUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //now checking that the user is marshmallow or greater version then we need to take the permissions from the user otherwise below marshmallow version we don't need to take permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //if the external storage read permission is not given then  we are gonna ask for one
                    if (ContextCompat.checkSelfPermission(SetUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetUpActivity.this, "Permission to read external storage is not granted", Toast.LENGTH_SHORT).show();
                        //without this line we have to go to app info and grant permission to read external storage manually but after adding this line the white dialog box to ask for permission will appear like what happens in whatsapp and big apps
                        //after this next video is started which starts with cropping up our image
                        //once our permissions are given by the user we don't want to show the toast instead we want to popup the activity where user will be able to select the image
                        ActivityCompat.requestPermissions(SetUpActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        //This will send the user to crop activity where he will see from where he want to access an image and then crop the selected image
                        //Once the image is cropped we can get our result in onActivityResult
                        BringImagePicker();

                        //Toast.makeText(SetUpActivity.this,"Permission to read external storage is already granted",Toast.LENGTH_SHORT).show();}
                    }
                }else {BringImagePicker();}

            }
        });
    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task,String userName) {
        //if the task is sucessfull then we want to start adding entries to the storage that is extended to next video i am in part 5 so it will be shown in part 6
        //We get the download url over here.
        Uri download_uri;
        if (task!=null) {
            download_uri = task.getResult().getDownloadUrl();
        }else {
            download_uri=mainImageUri;
        }

        //step 3 of adding data to firestore database
        Map<String,String> userMap=new HashMap<>();
        userMap.put("name",userName);
        userMap.put("image",download_uri.toString());
        //Now once our image is uploaded we can start uploading our data to firestore
        //We are going to create a collection in our firestore database and in that collection we're going to create a document with the name of userID and inside that document we are going to create two fields name and image
        firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SetUpActivity.this,"User settings are updated",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(SetUpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(SetUpActivity.this,"FireStore Error : "+error,Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        //Toast.makeText(SetUpActivity.this,"Yout image is uploaded",Toast.LENGTH_SHORT).show();
    }

    private void BringImagePicker() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(SetUpActivity.this);
    }

    //So what this basically do is check the  CROP_IMAGE_ACTIVITY_REQUEST_CODE  that is the one that is BringImagePicker code
    //And once that request code matches it will return us the result as Uri and if we get an error it will give an exception and then we will handle that
    //I am going to create the member variable for this uri||that is mainImageUri which is at the start of the code||by default this will be set to null
    //And once we get the image result we will set the mainImageUri to the result we get which is result Uri.Once we get the cropped image this will store the Uri of the cropped image in mainImageUri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                setUpImage.setImageURI(mainImageUri);

                isChanged=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}
