package com.example.shikharjai.recorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.viewholder> {
    Context context;
    List<String> file_list;
    List<String> file_list_path;
    MediaPlayer mp;

    public adapter(Context context, List<String> file_list, List<String> file_list_path){
        this.context = context;
        this.file_list = file_list;
        this.file_list_path = file_list_path;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recorder_list, viewGroup, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder,final int i) {
        viewholder.sno.setText(""+(i+1));
        viewholder.filename.setText(file_list.get(i));
        viewholder.clickhere.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(file_list_path.get(i));
                    mp.prepare();
                    mp.start();
                    Toast.makeText(context, "playing"+file_list.get(i), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return file_list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        Button sno;
        TextView filename;
        RelativeLayout clickhere;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            filename = itemView.findViewById(R.id.filename);
            filename.setHorizontallyScrolling(true);
            filename.setSelected(true);


            clickhere = itemView.findViewById(R.id.click_here);
        }
    }
}
