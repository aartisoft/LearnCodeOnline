package com.example.shikharjai.trump;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DrumPad extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MainActivity";
    Button one, two, three, four, five, six,label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drum_panel);
        one=findViewById(R.id.one);
        two=findViewById(R.id.two);
        three=findViewById(R.id.three);
        four=findViewById(R.id.four);
        five=findViewById(R.id.five);
        six=findViewById(R.id.six);
        label = findViewById(R.id.label);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
    }

    public void playMusic(View view){
        //id  in integer form
        int id = view.getId();
        String nameId = view.getResources().getResourceEntryName(id);
        Log.d(TAG, "idname is"+nameId);

        int musicid = getResources().getIdentifier(nameId, "raw", "com.example.shikharjai.trump");
        MediaPlayer md = MediaPlayer.create(this, musicid);
        md.start();
    }

    @Override
    public void onClick(View v) {
        Button b = findViewById(v.getId());
        label.setText(b.getText().toString());
        playMusic(v);
    }
}

