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
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.PlanMealItem;

import java.util.List;

public class DayMealsAdapter extends RecyclerView.Adapter<DayMealsAdapter.DayMealViewHolder> {

    private final AsyncListDiffer<PlanMealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public void updateList(List<PlanMealItem> items) {
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

        private void bindData(PlanMealItem item) {
            name.setText(item.getMeal().getName());
            ViewUtils.loadImageInto(item.getMeal().getThumbnail(),img);
        }
    }

    private static final DiffUtil.ItemCallback<PlanMealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlanMealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlanMealItem oldArea, @NonNull PlanMealItem newArea) {
            return oldArea.getMeal().getId().equals(newArea.getMeal().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlanMealItem oldArea, @NonNull PlanMealItem newArea) {
            return oldArea.equals(newArea);
        }
    };

}
