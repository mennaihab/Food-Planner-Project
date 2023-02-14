package com.example.foodplanner.features.plan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.plan.helpers.DayData;

import java.util.List;

public class WeekDayAdapter extends RecyclerView.Adapter<WeekDayAdapter.DayViewHolder> {

    private final AsyncListDiffer<DayData> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    public void submitList(List<DayData> list) {
        mDiffer.submitList(list);
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View photoView = inflater.inflate(R.layout.item_day, parent, false);
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
        private final TextView dayName;
        private final RecyclerView mealsList;
        private final DayMealsAdapter childItemAdapter;

        DayViewHolder(View itemView) {
            super(itemView);
            dayName = itemView.findViewById(R.id.dayName);
            mealsList = itemView.findViewById(R.id.child_recyclerview);
            childItemAdapter = new DayMealsAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            mealsList.addItemDecoration(
                    new MarginItemDecoration(ViewUtils.dpToPx(mealsList.getContext(), 16), 1, LinearLayoutManager.HORIZONTAL)
            );
            mealsList.setLayoutManager(layoutManager);
            mealsList.setAdapter(childItemAdapter);
            mealsList.setRecycledViewPool(viewPool);
            mealsList.setNestedScrollingEnabled(false);
        }

        public void bindData(DayData parentItem) {
            dayName.setText(parentItem.getDay().getDayOfWeek().name());
            childItemAdapter.updateList(parentItem.getMeals());
        }
    }

    private static final DiffUtil.ItemCallback<DayData> DIFF_CALLBACK = new DiffUtil.ItemCallback<DayData>() {
        @Override
        public boolean areItemsTheSame(@NonNull DayData oldArea, @NonNull DayData newArea) {
            return oldArea.getDay().equals(newArea.getDay());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DayData oldArea, @NonNull DayData newArea) {
            return oldArea.equals(newArea);
        }
    };
}

