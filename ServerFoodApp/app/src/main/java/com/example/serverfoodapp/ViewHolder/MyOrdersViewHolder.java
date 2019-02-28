package com.example.serverfoodapp.ViewHolder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.serverfoodapp.Common.User;
import com.example.serverfoodapp.ItemClickListener.ItemClickLitener;
import com.example.serverfoodapp.R;

import java.util.Currency;
import java.util.Locale;

public class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{
    TextView order_id, order_status, order_items;
    ImageView orderTotal;

    public void setItemClickLitener(ItemClickLitener itemClickLitener) {
        this.itemClickLitener = itemClickLitener;
    }

    ItemClickLitener itemClickLitener;

    public void setOrderTotal(String orderTotal) {
        Locale locale = new Locale("hi", "IN");
        TextDrawable drawable = TextDrawable.builder().beginConfig().textColor(Color.WHITE).withBorder(2).endConfig().buildRoundRect(Currency.getInstance(locale).getSymbol()+"  "+orderTotal, Color.DKGRAY,15);
        this.orderTotal.setImageDrawable(drawable);
    }


    public MyOrdersViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id = itemView.findViewById(R.id.order_id);
        order_status = itemView.findViewById(R.id.order_status);
        order_items = itemView.findViewById(R.id.order_items);
        orderTotal = itemView.findViewById(R.id.order_total);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public void setOrder_id(String order_id) {
       this.order_id.setText("Order #"+order_id);
    }

    public void setOrder_status(int order_status) {
        this.order_status.setText(User.convertToStatus(order_status));
    }


    public void setOrder_items(String order_items) {
        this.order_items.setText(order_items+" items");
    }

    @Override
    public void onClick(View v) {
        itemClickLitener.onClick(v, getAdapterPosition(), false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Change Order");
        menu.add(0,0,getAdapterPosition(),"Update Order");
        menu.add(0,1,getAdapterPosition(), "Delete Order");
    }
}
