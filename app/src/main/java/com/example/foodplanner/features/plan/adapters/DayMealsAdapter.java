package com.example.foodplanner.features.plan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
    private final PlanMealClickListener clickListener;

    public DayMealsAdapter(PlanMealClickListener clickListener) {
        this.clickListener = clickListener;
    }

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

    public class DayMealViewHolder extends RecyclerView.ViewHolder {
        private final CardView card;
        private final TextView name;
        private final ImageView img;
        private final Button remove;

        private DayMealViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.meal_card);
            name = itemView.findViewById(R.id.meal_name);
            img = itemView.findViewById(R.id.meal_img);
            remove = itemView.findViewById(R.id.meal_remove);
        }

        private void bindData(PlanMealItem item) {
            name.setText(item.getMeal().getName());
            ViewUtils.loadImageInto(item.getMeal().getPreview(),img);
            remove.setOnClickListener(v -> clickListener.onRemoveItem(item));
            card.setOnClickListener(v -> clickListener.onClick(item));
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
