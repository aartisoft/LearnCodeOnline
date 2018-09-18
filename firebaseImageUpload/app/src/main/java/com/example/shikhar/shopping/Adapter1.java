package com.example.shikhar.shopping;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vyanktesh.shopping.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter1 extends RecyclerView.Adapter<Adapter1.ViewHolder>{

   int[] image;
   String[] type;
   Context context;
    public Adapter1(Context context,int[] image,String[] type){
        this.image=image;
        this.context=context;
        this.type=type;

   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.foodcart,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.dishname.setText(type[i]);
        viewHolder.circleImageView.setImageResource(image[i]);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View card;
        CircleImageView circleImageView;
        TextView dishname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card=itemView.findViewById(R.id.cardf);
            circleImageView=itemView.findViewById(R.id.dishImage);
            dishname=itemView.findViewById(R.id.dishName);

            }
    }
}
