package com.example.serverfoodapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serverfoodapp.Common.User;
import com.example.serverfoodapp.Model.CategoryModel;
import com.example.serverfoodapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class food_menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int GALLERY_REQUEST_CODE = 200;
    private static final int PERMIT_CODE = 201;
    private List<String> categoryList = new ArrayList();
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    protected DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    public String TAG = "food_menu";
    private AlertDialog.Builder alertDialog;
    private MaterialEditText category_name;
    private Button select, upload;
    private Uri uri;
    private CategoryModel newCategoryModel;
    DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Storage storage;
    private ImageView category_image;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            uri = data.getData();
            Log.d(TAG, "onActivityResult: if executed");
            upload.setEnabled(true);
            select.setText("IMAGE SELECTED");
            upload.setText("UPLOAD IMAGE");

            try {
                category_image.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
            } catch (IOException e) {
                Log.d(TAG, "onActivityResult: catchhhh"+e.toString());
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "onActivityResult:else "+requestCode+resultCode+data.getData().toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMIT_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(food_menu.this, "Permission accepted to read your External storage", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(food_menu.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_menu);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(food_menu.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(food_menu.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMIT_CODE);
            }
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");

        setSupportActionBar(toolbar);
        TextView navHeaderTitle;

        floatingActionButton = findViewById(R.id.fab);
        databaseReference = FirebaseDatabase.getInstance().getReference("category");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        View v = navigationView.getHeaderView(0);
        TextView t = v.findViewById(R.id.nav_header_title);

        Query query = FirebaseDatabase.getInstance().getReference("category");

        FirebaseRecyclerOptions<CategoryModel> options = new FirebaseRecyclerOptions.Builder<CategoryModel>().setQuery(query, new SnapshotParser<CategoryModel>() {
            @NonNull
            @Override
            public CategoryModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onBindViewHolder: ");
                return new CategoryModel(snapshot.child("Image").getValue().toString(), snapshot.child("Name").getValue().toString());
            }
        }).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CategoryModel, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(food_menu.this);
                View v = inflater.inflate(R.layout.menu_items, viewGroup, false);
                return new MenuViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, final int position, @NonNull CategoryModel model) {
                Log.d(TAG, "onBindViewHolder: ");
                holder.setCategoryText(model.getName());
                holder.setCategoryImage(model.getImage());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      String categoryId = firebaseRecyclerAdapter.getRef(position).getKey();
                      Toast.makeText(food_menu.this, "Successfully Transferred"+categoryId, Toast.LENGTH_SHORT).show();
                      Intent intent = new Intent(food_menu.this, food_list.class);
                      intent.putExtra("categoryId", categoryId);
                      startActivity(intent);
                    }
                });
             }
        };
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(food_menu.this));

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Update")){
            Log.d(TAG, "onContextItemSelected: UPDATE"+item.getOrder());
            showUpdateDialog(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey(), (CategoryModel) firebaseRecyclerAdapter.getItem(item.getOrder()));
        } else if(item.getTitle().equals("Delete")){
            deleteCategoryItem(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategoryItem(final String key) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alertDialog = new AlertDialog.Builder(food_menu.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(food_menu.this);
        }

        alertDialog.setTitle("Sure to delete?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(key).removeValue();
                firebaseRecyclerAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void showDialog() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alertDialog = new AlertDialog.Builder(food_menu.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(food_menu.this);
        }

        alertDialog.setTitle("Add Category");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category, null);
        alertDialog.setView(view);
        category_image = view.findViewById(R.id.cat_image);
        category_name = view.findViewById(R.id.category_name);
        select =  view.findViewById(R.id.select_image);
        upload =  view.findViewById(R.id.upload_image);



        upload.setEnabled(false);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Pick Image"), GALLERY_REQUEST_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(newCategoryModel != null){
                    databaseReference.push().setValue(newCategoryModel);
                } else{
                    Toast.makeText(food_menu.this, "failed in pushing value", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void uploadImage() {
        select.setText("SELECT IMAGE");

        if(uri != null){
            final ProgressDialog progressDialog = new ProgressDialog(food_menu.this);
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReferenceProfilePic = firebaseStorage.getReferenceFromUrl("gs://chuckstuffs-59a64.appspot.com/");
            final StorageReference imageRef = storageReferenceProfilePic.child("food_images" + "/" + UUID.randomUUID().toString());

            imageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successful
                            //hiding the progress dialog
                            //and displaying a success toast
                            progressDialog.dismiss();
                            upload.setText("UPLOADED");
                            upload.setEnabled(false);
                            select.setText("SELECT ANOTHER");
                             Toast.makeText(food_menu.this, "Image Upload Success", Toast.LENGTH_SHORT).show();
                             Log.d(TAG, "onSuccess: ");

                             imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     newCategoryModel = new CategoryModel(uri.toString(),category_name.getText().toString());
                                 }
                             });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            //and displaying error message
                            upload.setText("UPLOAD FAILED");
                            Log.d(TAG, "onFailure: "+exception.toString()+exception.getCause().toString());
                            Toast.makeText(food_menu.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            upload.setText("UPLOADING");
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

            progressDialog.show();

        }
    }

    private void showUpdateDialog(final String key, final CategoryModel item) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alertDialog = new AlertDialog.Builder(food_menu.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(food_menu.this);
        }

        alertDialog.setTitle("Update Category");

        final LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category, null);
        alertDialog.setView(view);
        category_image = view.findViewById(R.id.cat_image);
        category_name = view.findViewById(R.id.category_name);
        select =  view.findViewById(R.id.select_image);
        upload =  view.findViewById(R.id.upload_image);
        TextView confirm = view.findViewById(R.id.confirm);
        upload.setEnabled(false);

        confirm.setText("Confirm Update?");

        category_name.setText(item.getName());
        Picasso.get().load(item.getImage()).into(category_image);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pick Image"), GALLERY_REQUEST_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setName(category_name.getText().toString());
                try{
                    databaseReference.child(key).setValue(item);
                } catch (Exception e){
                    Toast.makeText(food_menu.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void changeImage(final CategoryModel item) {
        select.setText("CHANGE IMAGE");

        if(uri != null){
            final ProgressDialog progressDialog = new ProgressDialog(food_menu.this);
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReferenceProfilePic = firebaseStorage.getReferenceFromUrl("gs://chuckstuffs-59a64.appspot.com/");
            final StorageReference imageRef = storageReferenceProfilePic.child("updated_images" + "/" + UUID.randomUUID().toString());

            imageRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successful
                            //hiding the progress dialog
                            //and displaying a success toast
                            progressDialog.dismiss();
                            upload.setText("UPLOADED");
                            upload.setEnabled(false);
                            select.setText("SELECT ANOTHER");

                            Toast.makeText(food_menu.this, "Image Upload Success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onSuccess: ");

                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            //and displaying error message
                            upload.setText("UPLOAD FAILED");
                            Log.d(TAG, "onFailure: "+exception.toString()+exception.getCause().toString());
                            Toast.makeText(food_menu.this, exception.getCause().getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            upload.setText("UPLOADING");
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                        //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });

            progressDialog.show();

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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        Log.d(TAG, "onNavigationItemSelected: "+id);
        if (id == R.id.nav_orders) {
            Log.d(TAG, "onNavigationItemSelected: all orders");
            startActivity(new Intent(food_menu.this, all_order.class));

        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(food_menu.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            User.currentUser = null;
            startActivity(i);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
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

}
