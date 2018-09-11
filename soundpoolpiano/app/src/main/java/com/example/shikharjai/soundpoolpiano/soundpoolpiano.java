    package com.example.shikharjai.soundpoolpiano;

import android.animation.ObjectAnimator;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

    public class soundpoolpiano extends AppCompatActivity {

    SoundPool soundPool;
    int a,b,c,d,e,f,g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundpoolpiano);

        //condition checks if using new version of soundpool library which only supports lollipop or higher
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build();

            //Press Ctrl+B on USAGE_ASSISTANCE_SONIFICATION to know more

            soundPool= new SoundPool.Builder().setMaxStreams(6).setAudioAttributes(audioAttributes).build();
            //maxstreams defines how many sounds you can play at same time

        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
            a = soundPool.load(this, R.raw.a, 0);
            b = soundPool.load(this, R.raw.b, 0);
            c = soundPool.load(this, R.raw.c, 0);
            d = soundPool.load(this, R.raw.d, 0);
            e = soundPool.load(this, R.raw.e, 0);
            f = soundPool.load(this, R.raw.f, 0);
            g = soundPool.load(this, R.raw.g, 0);

        }

    public void playMusic(View v){ // called by onClick attribute of button xml in activity
        Button temp = (Button)findViewById(v.getId());
        ObjectAnimator.ofFloat(temp, "alpha", 1f).setDuration(100).start();
       switch (v.getId()) {
           case R.id.a:
               soundPool.play(a, 1, 1, 0, 0, 1);
               break;
           case R.id.b:
               soundPool.play(b, 1, 1, 0, 0, 1);
               break;
           case R.id.c:
               soundPool.play(c, 1, 1, 0, 0, 1);
               break;
           case R.id.d:
               soundPool.play(d, 1, 1, 0, 0, 1);
               break;
           case R.id.e:
               soundPool.play(e, 1, 1, 0, 0, 1);
               break;
           case R.id.f:
               soundPool.play(f, 1, 1, 0, 0, 1);
               break;
           case R.id.g:
               soundPool.play(g, 1, 1, 0, 0, 1);
               break;
       }
    }
}
