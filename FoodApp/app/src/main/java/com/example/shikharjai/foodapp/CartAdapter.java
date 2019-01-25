package com.example.shikharjai.foodapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.shikharjai.foodapp.ViewHolderPackage.Order;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<Order> orderList;
    Locale locale;

    public CartAdapter(List<Order> orderList, Locale locale) {
        this.orderList = orderList;
        this.locale = locale;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.foodName.setText(orderList.get(i).getProductName());

        //another way to find  currency symbol
        //NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        int price = Integer.parseInt(orderList.get(i).getProductPrice());
        int quantity = Integer.parseInt(orderList.get(i).getProductQuantity());
        int foodTotal = quantity*price;

        TextDrawable drawable = TextDrawable.builder().beginConfig().textColor(Color.WHITE).withBorder(2).endConfig().buildRoundRect(Currency.getInstance(locale).getSymbol()+" "+foodTotal, Color.DKGRAY, 15);

        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.foodPrice.setText(Currency.getInstance(locale).getSymbol()+"  "+price);

        viewHolder.foodQuantity.setText(""+quantity);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodName;
        TextView foodPrice;
        TextView foodQuantity;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            foodQuantity = itemView.findViewById(R.id.food_quantity);
            imageView = itemView.findViewById(R.id.food_total);
        }
    }
}
