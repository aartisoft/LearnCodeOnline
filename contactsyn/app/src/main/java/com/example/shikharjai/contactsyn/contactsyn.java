package com.example.shikharjai.contactsyn;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class contactsyn extends AppCompatActivity {

    conDatabase con;
    EditText name;
    EditText number;
    EditText email;
    EditText address;
    Dialog save_contact;
    Button save_btn;
    FloatingActionButton addcontact;
    List<Contact> contactList;
    RecyclerView recyclerView;
    android.support.v7.widget.Toolbar tl;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delall:
                con.deleteAll();
                Toast.makeText(this, "Contacts Deleted", Toast.LENGTH_SHORT).show();
                fetchContacts();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactsyn);
        con = new conDatabase(contactsyn.this);
        tl = findViewById(R.id.tl);
        setSupportActionBar(tl);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, 100);
            }
        }

        addcontact=findViewById(R.id.add_contact);
        fetchContacts();
        final Random random = new Random();
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              save_contact = new Dialog(contactsyn.this);
              View dg = LayoutInflater.from(contactsyn.this).inflate(R.layout.save_contact, null);
              save_contact.setContentView(dg);
              save_contact.getWindow().setBackgroundDrawable(new ColorDrawable(Color.CYAN));
              name = save_contact.findViewById(R.id.name);
              number = save_contact.findViewById(R.id.number);
              email = save_contact.findViewById(R.id.email);
              address = save_contact.findViewById(R.id.address);
              save_btn = save_contact.findViewById(R.id.save_btn);

                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        con.addContact(new Contact(String.valueOf(random.nextInt(9999999)+9999),name.getText().toString(),
                                number.getText().toString(), email.getText().toString(),
                                address.getText().toString()));
                        save_contact.dismiss();
                        fetchContacts();
                        Toast.makeText(contactsyn.this, "Synchronized to Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
                    save_contact.show();
                }
            });
        }

        public void fetchContacts(){
            recyclerView = findViewById(R.id.rec);
            contactList=new ArrayList<Contact>();
            contactList.clear();
            contactList = con.getContact();
            synctoFirebase();
            if(!contactList.isEmpty()){
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                con_adepter adepter = new con_adepter(this, contactList);
                recyclerView.setAdapter(adepter);
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                con_adepter adepter = new con_adepter(this, contactList);
                recyclerView.setAdapter(adepter);
            }
        }

        public void synctoFirebase(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("user");

            if(contactList.size()>0){
                Toast.makeText(this, "Synchronized to Firebase", Toast.LENGTH_SHORT).show();
                for(Contact c : contactList){
                    String uid = c.contact_add;
                    myRef.child(uid).setValue(c);
                }
            } else {
                myRef.setValue(null);
            }
        }
    }
