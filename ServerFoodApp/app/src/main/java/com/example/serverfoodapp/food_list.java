package com.example.serverfoodapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serverfoodapp.Model.FoodModel;
import com.example.serverfoodapp.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class food_list extends AppCompatActivity {
    public String TAG = "food_list";
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    Toolbar toolbar;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private MaterialSearchBar searchBar;
    private FloatingActionButton floatingActionButton;
    private int change_food_image_permit = 100;
    private Bitmap imgBitmap;
    private Button upload;
    private Button select;
    private ImageView food_image;
    TextView food_name;
    TextView food_price;
    TextView food_discount;
    TextView food_desc;
    TextView food_id;
    private FoodModel food_model;
    private String categoryId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == change_food_image_permit && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(
                        selectedImage);
                Bitmap bmp = BitmapFactory.decodeStream(imageStream);
                Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 300, 300, true);
                imgBitmap = bitmap;
            } catch (FileNotFoundException e) {
                Log.d(TAG, "onActivityResult: "+e.toString());
            }

            //            Uri data1 = data.getData();

            upload.setEnabled(true);
            upload.setText("UPLOAD");
            select.setText("IMAGE SELECTED");

                food_image.setImageBitmap(imgBitmap);
            }
        }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.food_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference("foodlist");
        floatingActionButton = findViewById(R.id.food_list_fab);


        if (getIntent() != null) {
            if (getIntent().getStringExtra("categoryId") != null) {
                categoryId = getIntent().getStringExtra("categoryId");
                fetchFoodList(categoryId);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewItemDialog(categoryId);
                    }
                });            }
        }



    }

    private void fetchFoodList(String catID) {
        Query query = databaseReference.orderByChild("MenuId").equalTo(catID);
        FirebaseRecyclerOptions<FoodModel> options = new FirebaseRecyclerOptions.Builder<FoodModel>().setQuery(query, new SnapshotParser<FoodModel>() {
            @NonNull
            @Override
            public FoodModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "parseSnapshot: " + snapshot.child("Discount").getValue().toString());

                return new FoodModel(snapshot.child("Description").getValue().toString(), snapshot.child("Discount").getValue().toString(), snapshot.child("Image").getValue().toString(), snapshot.child("MenuId").getValue().toString(), snapshot.child("Name").getValue().toString(), snapshot.child("Price").getValue().toString());
            }
        }).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FoodModel, FoodViewHolder>(options) {
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View v = inflater.inflate(R.layout.food_items, viewGroup, false);
                return new FoodViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, final int position, @NonNull FoodModel model) {
                holder.setFoodName(model.getName());
                holder.setFoodImage(model.getImage());
                Log.d(TAG, "onBindViewHolder: "+model.getImage());
                holder.setFoodDescription(model.getDescription());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(food_list.this, ""+firebaseRecyclerAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    private void addNewItemDialog(String catId) {
        AlertDialog.Builder alertDialog;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alertDialog = new AlertDialog.Builder(food_list.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(food_list.this);
        }

        alertDialog.setTitle("Add FoodItem");
        final LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_food_item, null);
        alertDialog.setView(view);

        food_image = view.findViewById(R.id.food_image);
        food_name = view.findViewById(R.id.food_name);
        food_price = view.findViewById(R.id.food_price);
        food_discount = view.findViewById(R.id.food_discount);
        food_desc = view.findViewById(R.id.food_desc);
        food_id = view.findViewById(R.id.food_category_id);
        select =  view.findViewById(R.id.select_image);
        upload =  view.findViewById(R.id.upload_image);

        upload.setEnabled(false);
        food_id.setText(catId);
        upload.setEnabled(false);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Pick Image"), change_food_image_permit);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(null);
            }
        });

        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(food_model != null){
                    databaseReference.push().setValue(food_model);
                } else{
                    Toast.makeText(food_list.this, "failed in pushing value", Toast.LENGTH_SHORT).show();
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

    private void uploadImage(final FoodModel item) {
        if(imgBitmap != null){
            final ProgressDialog progressBar = new ProgressDialog(food_list.this);
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = ((FirebaseStorage) storage).getReferenceFromUrl("gs://chuckstuffs-59a64.appspot.com");
            final StorageReference imageRef = storageReference.child("food_imsges/"+ UUID.randomUUID().toString()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            imageRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.dismiss();
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "onSuccess: "+uri.toString());
                            if(item == null){
                                food_model = new FoodModel(food_desc.getText().toString(), food_discount.getText().toString(), uri.toString(), food_id.getText().toString(),food_name.getText().toString(),food_price.getText().toString());
                            } else {
                                item.setImage(uri.toString());
                            }
                        }
                    });
                    upload.setText("UPLOADED");
                    upload.setEnabled(false);
                    select.setText("CHANGE ANOTHER");
                    Log.d(TAG, "onSuccess: ");
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Double d = (double)100*(taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setMessage("Uploading"+d+"%");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.dismiss();
                    upload.setText("UPLOAD FAILED");
                    upload.setEnabled(false);
                    Toast.makeText(food_list.this, ""+e.getCause().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+e.getCause().getLocalizedMessage());
                }
            });
            progressBar.show();
        }

    }

    private void updateItemDialog(final String itemId, final FoodModel item) {
        AlertDialog.Builder alertDialog;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            alertDialog = new AlertDialog.Builder(food_list.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            alertDialog = new AlertDialog.Builder(food_list.this);
        }

        alertDialog.setTitle("Change FoodItem");
        final LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_food_item, null);
        alertDialog.setView(view);

        food_image = view.findViewById(R.id.food_image);
        food_name = view.findViewById(R.id.food_name);
        food_price = view.findViewById(R.id.food_price);
        food_discount = view.findViewById(R.id.food_discount);
        food_desc = view.findViewById(R.id.food_desc);
        food_id = view.findViewById(R.id.food_category_id);
        select =  view.findViewById(R.id.select_image);
        upload =  view.findViewById(R.id.upload_image);
        TextView confirm = view.findViewById(R.id.confirm_food_item);
        confirm.setText("Confirm Update?");

        upload.setEnabled(false);

        food_id.setText(item.getMenuId());
        food_name.setText(item.getName());
        food_desc.setText(item.getDescription());
        food_discount.setText(item.getDiscount());
        food_price.setText(item.getPrice());
        Log.d(TAG, "updateItemDialog: "+item.getImage());
        Picasso.get().load(item.getImage()).into(food_image);

        upload.setEnabled(false);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Pick Image"), change_food_image_permit);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(item);
            }
        });

        alertDialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                item.setDescription(food_desc.getText().toString());
                item.setDiscount(food_discount.getText().toString());
                item.setName(food_name.getText().toString());
                item.setPrice(food_price.getText().toString());
                    databaseReference.child(itemId).setValue(item);
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
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("Update")){
            updateItemDialog(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey(), (FoodModel) firebaseRecyclerAdapter.getItem(item.getOrder()));
        } else if(item.getTitle().equals("Delete")){
            showDeleteDialog(firebaseRecyclerAdapter.getRef(item.getOrder()).getKey());

        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteDialog(final String key) {
        AlertDialog.Builder alert;
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            alert = new AlertDialog.Builder(food_list.this, android.R.style.Theme_Material_Dialog_Alert);
        } else{
            alert = new AlertDialog.Builder(food_list.this);           
        }
        alert.setTitle("Sure to delete?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(key).removeValue();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
