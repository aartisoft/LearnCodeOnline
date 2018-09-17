package com.example.shikharjai.recorder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class recorder1 extends Fragment {
    Button rec, play, pause, stop;
    String path="";
    boolean isPaused=false;
    MediaRecorder mediaRecorder;
    MediaPlayer mp;
    int length;
    public static recorder1 getInstance(){
        recorder1 fragment = new recorder1();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recorder1, container, false);
        rec = v.findViewById(R.id.rec);
        pause = v.findViewById(R.id.pause);
        play = v.findViewById(R.id.play);
        stop = v.findViewById(R.id.stop);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {

        final File dir = new File(Environment.getExternalStorageDirectory(), "/njaudio/");
        if((!dir.exists())) {
            dir.mkdir();
        }
        mp = new MediaPlayer();

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Now Recording",Toast.LENGTH_SHORT).show();
                path = dir.getAbsolutePath()+"/"+ UUID.randomUUID()+".mp3";
                RecorderSetup(path);
                try {
                    mediaRecorder.prepare();
                    play.setEnabled(false);
                    v.setEnabled(false);
                    stop.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rec.setEnabled(false);

                File[] filelist  = dir.listFiles();
                String last_file_path = filelist[filelist.length - 1].getAbsolutePath();
                Log.d("ddd"+mp, ""+(mp.getCurrentPosition()));
                if(!isPaused){
                    try {
                    mp.stop();
                    mp.reset();
                    mp.setDataSource(last_file_path);
                    mp.prepare();
                    mp.start();
                    pause.setEnabled(true);
                    Toast.makeText(getContext(),"Playing", Toast.LENGTH_SHORT).show();
                } catch (IOException e){
                    e.printStackTrace();
                }} else {
                        mp.seekTo(length);
                        mp.start();
                        pause.setEnabled(true);
                        isPaused = false;
                        Toast.makeText(getContext(),"Resuming"+length, Toast.LENGTH_SHORT).show();
                    }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        rec.setEnabled(true);

                    }
                });

                }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    if(mp != null){
                        mp.pause();
                        isPaused = true;
                        v.setEnabled(false);
                        rec.setEnabled(true);
                        length = mp.getCurrentPosition();
                        Toast.makeText(getContext(), "Pausedaaaa"+length, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Currently Not Playing", Toast.LENGTH_SHORT).show();
                }
                    }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaRecorder != null){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    v.setEnabled(false);
                    play.setEnabled(true);
                    rec.setEnabled(true);
                    isPaused = false;
                    Toast.makeText(getContext(), "Recording Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }


    public void RecorderSetup(String p){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(p);
    }
}
