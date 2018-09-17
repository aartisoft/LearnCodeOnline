package com.example.shikharjai.dicegame;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import java.util.Random;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class dicegame extends AppCompatActivity {
Button dicebtn;
Button shufflebtn;
String arr[]={"one", "two", "three", "four", "five", "six"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicegame);
        dicebtn = findViewById(R.id.dicebtn);
        shufflebtn = findViewById(R.id.shufflebtn);
        final float startScale = 0f;
        final float endScale  = 0.6f;
        shufflebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation rotate = new RotateAnimation(0, 2400, dicebtn.getWidth()/2, dicebtn.getHeight()/2);
                rotate.setDuration(1500);
                dicebtn.startAnimation(rotate);
                Random random = new Random();
                Resources res = getResources();
                TypedArray img = res.obtainTypedArray(R.array.dices);
                Drawable drawable = img.getDrawable(random.nextInt(5));
                dicebtn.setBackground(drawable);
                dicebtn.setText("");
            }
        });
    }
}
