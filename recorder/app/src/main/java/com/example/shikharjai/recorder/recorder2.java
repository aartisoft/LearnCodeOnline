package com.example.shikharjai.recorder;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class recorder2 extends Fragment{
    RecyclerView recyclerView;
    List<String> file_list;
    List<String> file_list_path;
    public static recorder2 getInstance(){
        recorder2 f = new recorder2();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.recorder2, container,false);
        recyclerView = v.findViewById(R.id.rec);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        file_list = new ArrayList<>();
        file_list_path = new ArrayList<>();

        final File folder = new File(Environment.getExternalStorageDirectory(), "/njaudio");
        if(!folder.exists()){
            folder.mkdir();
        }
        File[] listoffiles = folder.listFiles();
        for(File file : listoffiles){
            if(file.isFile()){
                file_list.add(file.getName());
                Log.d("fffff", ""+file.getAbsolutePath());
                file_list_path.add((file.getAbsolutePath()));
            }
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter adapter = new adapter(getContext(), file_list, file_list_path);
        recyclerView.setAdapter(adapter);
        super.onActivityCreated(savedInstanceState);
    }
}
