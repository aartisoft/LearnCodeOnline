package com.example.shikharjai.foodapp.ViewHolderPackage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shikharjai.foodapp.R;
import com.squareup.picasso.Picasso;

public class MenuViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "menuviewholder";
    public TextView menuText;
    public ImageView menuImage;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        menuText = itemView.findViewById(R.id.menu_text);
        menuImage = itemView.findViewById(R.id.menu_image);
    }

    public void setMenuText(String s) {
        menuText.setText(s);
    }

    public void setMenuImage(String s) {
        Log.d(TAG, "parseSnapshot: "+s);
        Picasso.get().load(""+s).into(menuImage);
    }

}
