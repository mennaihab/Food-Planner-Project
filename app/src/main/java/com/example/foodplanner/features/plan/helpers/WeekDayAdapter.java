package com.example.foodplanner.features.plan.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.plan.views.DayData;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayAdapter.DayViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<DayData> list = Collections.emptyList();

    Context context;


    public WeekDayAdapter(List<DayData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.day_card, parent, false);
        return new DayViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(final DayViewHolder viewHolder, final int position) {
        viewHolder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayName;
        RecyclerView ChildRecyclerView;

        DayViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            DayMealsAdapter childItemAdapter = new DayMealsAdapter(context.getApplicationContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            ChildRecyclerView.setLayoutManager(layoutManager);
            ChildRecyclerView.setAdapter(childItemAdapter);
            ChildRecyclerView.setRecycledViewPool(viewPool);
            ChildRecyclerView.setNestedScrollingEnabled(false);
        }

        public void bindData(DayData parentItem) {
            dayName.setText(parentItem.getParentItemTitle());
            ((DayMealsAdapter) Objects.requireNonNull(ChildRecyclerView.getAdapter())).updateList(parentItem.getChildItemList());
        }
    }
}

