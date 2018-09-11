package com.example.shikharjai.piano;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class piano extends AppCompatActivity {

    Button c;
    Button d;
    Button e;
    Button f;
    Button g;
    Button a;
    Button b;
    String TAG="StringName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piano);

        c = findViewById(R.id.c);
        d = findViewById(R.id.d);
        e = findViewById(R.id.e);
        f = findViewById(R.id.f);
        g = findViewById(R.id.g);
        a = findViewById(R.id.a);
        b = findViewById(R.id.b);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(v);
            }
        });
    }

    public void playMusic(View v){

        int id = v.getId();
        String nameId = v.getResources().getResourceEntryName(id);
        int musicid = getResources().getIdentifier(nameId, "raw", "com.example.shikharjai.piano");
        MediaPlayer md = MediaPlayer.create(this, musicid);
        md.start();

    }
}
