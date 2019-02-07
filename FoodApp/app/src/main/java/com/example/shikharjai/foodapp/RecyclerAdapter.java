package com.example.shikharjai.foodapp;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikharjai.foodapp.Model.TimeLineModel;
import com.github.vipulasri.timelineview.TimelineView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder> {
    private List<TimeLineModel> mDataList;

    public RecyclerAdapter(List<TimeLineModel> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_timeline, viewGroup, false);
        return new ItemsViewHolder(v, i);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int i) {
        holder.mTimelineView.setMarker(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_add_black_24dp));

        if (!mDataList.get(i).getDate().isEmpty()) {
            holder.date.setVisibility(View.VISIBLE);

            holder.date.setText(mDataList.get(i).getDate());
        } else
            holder.date.setVisibility(View.GONE);

        holder.messsage.setText(mDataList.get(i).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        public  TimelineView mTimelineView;
        AppCompatTextView date;
        AppCompatTextView messsage;
        public ItemsViewHolder(@NonNull View itemView, int viewType) {

            super(itemView);
            date = itemView.findViewById(R.id.text_timeline_date);
            messsage = itemView.findViewById(R.id.text_timeline_title);
            mTimelineView = itemView.findViewById(R.id.item_timeline);
            mTimelineView.initLine(viewType);
        }
    }
}
