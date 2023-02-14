package com.example.foodplanner.features.plan.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.plan.models.DayData;


import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayAdapter.DayViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
   // List<DayData> list = Collections.emptyList();
    private final AsyncListDiffer<DayData> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private final Context context;

    public WeekDayAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<DayData> items) {
        mDiffer.submitList(items);
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
        viewHolder.bindData(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
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
    private static final DiffUtil.ItemCallback<DayData> DIFF_CALLBACK = new DiffUtil.ItemCallback<DayData>() {
        @Override
        public boolean areItemsTheSame(@NonNull DayData oldArea, @NonNull DayData newArea) {
            return oldArea.getParentItemTitle().equals(newArea.getParentItemTitle());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DayData oldArea, @NonNull DayData newArea) {
            return oldArea.equals(newArea);
        }
    };
}

