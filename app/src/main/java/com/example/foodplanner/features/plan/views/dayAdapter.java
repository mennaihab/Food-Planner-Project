package com.example.foodplanner.features.plan.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;

import java.util.Collections;
import java.util.List;

public class dayAdapter extends RecyclerView.Adapter<dayAdapter.DayViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<DayData> list = Collections.emptyList();

    Context context;


    public dayAdapter(List<DayData> list,Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.day_card,parent, false);
        DayViewHolder viewHolder = new DayViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DayViewHolder viewHolder, final int position) {
        DayData parentItem = list.get(position);
        viewHolder.dayName.setText(parentItem.getParentItemTitle());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.ChildRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        InsideDayAdapter childItemAdapter = new InsideDayAdapter(parentItem.getChildItemList(), context.getApplicationContext());
        viewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        viewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
        viewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayName;
       RecyclerView ChildRecyclerView;
        DayViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
        }
        }
    }

