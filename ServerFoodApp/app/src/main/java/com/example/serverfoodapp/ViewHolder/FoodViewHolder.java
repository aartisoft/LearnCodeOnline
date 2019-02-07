package com.example.serverfoodapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serverfoodapp.ItemClickListener.ItemClickLitener;
import com.example.serverfoodapp.R;
import com.squareup.picasso.Picasso;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    private TextView foodName;
    private ImageView foodImage;
    private TextView foodDescription;
    private Button cartBtn;
    private Button saveForLaterBtn;
    private ItemClickLitener itemClickListener;

    public void setItemClickListener(ItemClickLitener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        foodName = itemView.findViewById(R.id.foodName);
        foodImage = itemView.findViewById(R.id.foodImage);
        foodDescription = itemView.findViewById(R.id.foodDescription);
        cartBtn = itemView.findViewById(R.id.cartBtn);
        saveForLaterBtn = itemView.findViewById(R.id.saveLaterBtn);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }
    public void setFoodName(String s) {
        foodName.setText(s);
    }

    public void setFoodImage(String s) {
        Picasso.get().load(s).into(foodImage);
    }

    public void setFoodDescription(String s) {
        foodDescription.setText(s);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Change Food Item");
        menu.add(0,0,  getAdapterPosition(), "Update");
        menu.add(0,1,  getAdapterPosition(), "Delete");

    }
}
