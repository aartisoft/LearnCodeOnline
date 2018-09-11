package com.example.shikharjai.busfinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Rcadapter extends RecyclerView.Adapter<Rcadapter.holder> {
    Context c;
    List<String> bus_detail;

    public Rcadapter(Context c, List<String> bus_detail) {
        this.c = c;
        this.bus_detail = bus_detail;
    }

    @NonNull
    @Override

    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.recycler_layout, viewGroup, false);

        return new holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {

        String[] str = bus_detail.get(i).toString().split(":");
        holder.type.setText(str[1]);
        holder.stop.setText(str[0]);
        holder.sno.setText(""+(i+1));

    }


    @Override
    public int getItemCount() {
        return bus_detail.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView sno;
        TextView stop;
        TextView type;

        public holder(@NonNull View itemView) {
            super(itemView);
            sno = (TextView) itemView.findViewById(R.id.sno);
            stop = (TextView) itemView.findViewById(R.id.stop);
            type = (TextView)itemView.findViewById(R.id.type);
        }
    }
}
