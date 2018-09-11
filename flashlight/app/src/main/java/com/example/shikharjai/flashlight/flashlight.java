package com.example.shikharjai.flashlight;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class flashlight extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 50;
    Button perm;
    ImageView flash;
    boolean flashlight_status=false;
    boolean set_enable_disable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Checks if camera permission is granted
        set_enable_disable = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//        Log.d("s0","aaa"+hasCameraFlash+set_enable_disable);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        perm = findViewById(R.id.perm);
        flash = findViewById(R.id.flash);

        perm.setEnabled(!set_enable_disable);
        flash.setEnabled(set_enable_disable);

        perm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(flashlight.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST );
            }
        });

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasCameraFlash){
                    Log.d("sss","hascamera");
                    if(!flashlight_status){
                            flashlightOn();
                            Log.d("sss","on");
                        } else {
                            flashlightOff();
                        Log.d("sss","off");
                    }

                } else {
                    Toast.makeText(flashlight.this, "No flash in your devixe", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    perm.setTextColor(Color.parseColor("#000000"));
                    perm.setEnabled(false);
                    perm.setText("Camera Permission Granted");
                    flash.setEnabled(true);
                } else {
                    Toast.makeText(flashlight.this, "Permission denied fo the camera", Toast.LENGTH_SHORT).show();
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void flashlightOn(){
        CameraManager cam = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            flashlight_status = true;
            flash.setImageResource(R.drawable.flashon);
            String cameraId = cam.getCameraIdList()[0];
            cam.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void flashlightOff(){
        CameraManager cam = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            flashlight_status = false;
            String camId = cam.getCameraIdList()[0];
            flash.setImageResource(R.drawable.flashoff);
            cam.setTorchMode(camId,false);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

}
