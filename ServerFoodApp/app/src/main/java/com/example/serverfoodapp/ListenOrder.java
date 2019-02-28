package com.example.serverfoodapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.serverfoodapp.Model.Request;
import com.example.serverfoodapp.WrapperClass.App;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;
    private PendingIntent contentIndent;
    private String TAG = "ListenOrder";
    private Query query;

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FirebaseDatabase.getInstance();
        query= database.getReference("Requests").limitToLast(1);
        notificationManager = NotificationManagerCompat.from(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        query.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged: ");
        Request request = dataSnapshot.getValue(Request.class);
        showNotification(dataSnapshot.getKey(), request);
        Log.d(TAG, "onChildAdded: "+request.getTotal()+dataSnapshot.getKey());
    }

    private void showNotification(String key, Request request) {
        Toast.makeText(this, "notific", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getBaseContext(), all_order.class);
        contentIndent = PendingIntent.getActivity(getBaseContext(),0, intent, 0);


        Notification  mBuilder = new NotificationCompat.Builder(getBaseContext(), App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                .setContentTitle("Food Server")
                .setContentText("You have received a new order")
                .setContentIntent(contentIndent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Order ID# "+key))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        int random_int = new Random().nextInt(9999)+1;

        notificationManager.notify(random_int,mBuilder);
    }



    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(1);
        stopSelf();
        Log.d(TAG, "onDestroy: ");
    }
}
