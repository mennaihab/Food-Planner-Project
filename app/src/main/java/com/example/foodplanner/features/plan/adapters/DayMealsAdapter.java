package com.example.foodplanner.features.plan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public class DayMealsAdapter extends RecyclerView.Adapter<DayMealsAdapter.DayMealViewHolder> {

    private final AsyncListDiffer<MealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public void updateList(List<MealItem> items) {
        mDiffer.submitList(items);
    }

    @NonNull
    @Override
    public DayMealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View photoView = inflater.inflate(R.layout.item_plan_meal, parent, false);
        return new DayMealViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(DayMealViewHolder viewHolder, int position) {
        viewHolder.bindData(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public static class DayMealViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView img;

        private DayMealViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.meal_name);
            img = itemView.findViewById(R.id.meal_img);
        }

        private void bindData(MealItem item) {
            name.setText(item.getName());
            Glide.with(img).load(item.getThumbnail()).into(img);
        }
    }

    private static final DiffUtil.ItemCallback<MealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MealItem oldArea, @NonNull MealItem newArea) {
            return oldArea.getId().equals(newArea.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MealItem oldArea, @NonNull MealItem newArea) {
            return oldArea.equals(newArea);
        }
    };

}
