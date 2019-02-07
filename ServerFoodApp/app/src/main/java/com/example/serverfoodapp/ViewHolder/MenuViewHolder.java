package com.example.serverfoodapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.serverfoodapp.R;
import com.squareup.picasso.Picasso;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    TextView categoryText;
    ImageView categoryImage;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryText = itemView.findViewById(R.id.menu_text);
        categoryImage = itemView.findViewById(R.id.menu_image);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void setCategoryText(String s) {
        categoryText.setText(s);
    }

    public void setCategoryImage(String s) {
        Picasso.get().load(s).into(categoryImage);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select an action");
        menu.add(0,0, getAdapterPosition(), "Update");
        menu.add(0,1, getAdapterPosition(), "Delete");
    }
}
