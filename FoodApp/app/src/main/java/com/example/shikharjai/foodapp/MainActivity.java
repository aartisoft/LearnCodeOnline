package com.example.shikharjai.foodapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    ImageView imgLogo;
    LinearLayout btnset;
    Button signIn, signUp;
    private String TAG="MainActivity";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: airplane mode changed     "+intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)+intent.getAction());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgLogo = findViewById(R.id.imglogo);
        btnset = findViewById(R.id.btnset);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point2);
        imgLogo.startAnimation(animation);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);

        signIn = (Button) findViewById(R.id.login);
        signUp = (Button) findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, user_reg.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: BYpassing");
  //                startActivity(new Intent(MainActivity.this, food_menu.class)); finish();//bypassing for testing

                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnset.startAnimation(animation2);
                btnset.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
