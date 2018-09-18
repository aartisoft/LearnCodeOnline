package com.example.shikhar.bulbglowingapp;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.example.vyanktesh.bulbglowingapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "aaaaamainactivity" ;
    ImageView iv;
    private int currBrightness;
    private ContentResolver cResolver;
    private Window window;
    boolean canWriteSettings = false;
    Button enhance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cResolver = getContentResolver();
        window = getWindow();
        enhance = findViewById(R.id.enhance);
        iv=findViewById(R.id.iv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermision();
    }

    public void checkPermision(){
        Log.d(TAG, "checkPermision: ");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            canWriteSettings = Settings.System.canWrite(this);
            if(!canWriteSettings) {
                enhance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        MainActivity.this.startActivity(intent);
                    }
                });
            } else {
                enhance.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void changeVibrateState(View view){
        boolean checked=((ToggleButton)view).isChecked();
        if (checked){
            iv.setImageResource(R.drawable.bulbon);
            changeBrightness(1);
        }
        else {
            iv.setImageResource(R.drawable.bulbofff);
            changeBrightness(0);
        }
    }

    public void changeBrightness(int i){
            if(canWriteSettings){
                if(i == 0){
                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, currBrightness);
            } else {
                currBrightness = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,0);

                Settings.System.putInt(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 255);
            }
        }
    }
}
