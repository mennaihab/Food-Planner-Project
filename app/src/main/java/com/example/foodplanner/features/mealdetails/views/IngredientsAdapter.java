package com.example.foodplanner.features.mealdetails.views;

import android.annotation.SuppressLint;
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
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.plan.adapters.DayMealsAdapter;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private final AsyncListDiffer<Meal.Ingredient> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public void updateList(List<Meal.Ingredient> items) {
        mDiffer.submitList(items);
    }

    @NonNull
    @Override
    public IngredientsAdapter.IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View photoView = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsAdapter.IngredientsViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientsViewHolder viewHolder, int position) {
        viewHolder.bindData(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public static class IngredientsViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView measure;

        private IngredientsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredient_name);
            measure = itemView.findViewById(R.id.ingredient_quantity);
        }

        private void bindData(Meal.Ingredient item) {
            name.setText(item.getIngredient());
            measure.setText(item.getMeasure());
        }
    }

    private static final DiffUtil.ItemCallback<Meal.Ingredient> DIFF_CALLBACK = new DiffUtil.ItemCallback<Meal.Ingredient>() {
        @Override
        public boolean areItemsTheSame(@NonNull Meal.Ingredient oldArea, @NonNull Meal.Ingredient newArea) {
            return oldArea.getIngredient().equals(newArea.getIngredient());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Meal.Ingredient oldArea, @NonNull Meal.Ingredient newArea) {
            return oldArea.equals(newArea);
        }
    };

}
