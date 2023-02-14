package com.example.foodplanner.features.favourites.helpers;

import android.content.Context;
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

<<<<<<<< HEAD:app/src/main/java/com/example/foodplanner/features/favourites/helpers/FavouritesAdapter.java
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {
========
public class DayMealsAdapter extends RecyclerView.Adapter<DayMealsAdapter.DayMealsViewHolder> {
>>>>>>>> origin/favourites:app/src/main/java/com/example/foodplanner/features/plan/helpers/DayMealsAdapter.java

    private final AsyncListDiffer<MealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    public FavouritesAdapter() {
    }

    public void updateList(List<MealItem> items) {
        mDiffer.submitList(items);
    }


    @NonNull
    @Override
<<<<<<<< HEAD:app/src/main/java/com/example/foodplanner/features/favourites/helpers/FavouritesAdapter.java
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.item_favourite, parent, false);
        return new FavouritesAdapter.FavouritesViewHolder(photoView);
========
    public DayMealsAdapter.DayMealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the layout
        View photoView = inflater.inflate(R.layout.day_meals_item, parent, false);
        return new DayMealsViewHolder(photoView);
>>>>>>>> origin/favourites:app/src/main/java/com/example/foodplanner/features/plan/helpers/DayMealsAdapter.java
    }

    @Override
<<<<<<<< HEAD:app/src/main/java/com/example/foodplanner/features/favourites/helpers/FavouritesAdapter.java
    public void onBindViewHolder(final FavouritesAdapter.FavouritesViewHolder viewHolder, final int position) {
========
    public void onBindViewHolder(DayMealsAdapter.DayMealsViewHolder viewHolder, int position) {
>>>>>>>> origin/favourites:app/src/main/java/com/example/foodplanner/features/plan/helpers/DayMealsAdapter.java
        viewHolder.bindData(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

<<<<<<<< HEAD:app/src/main/java/com/example/foodplanner/features/favourites/helpers/FavouritesAdapter.java
    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView img;

        private FavouritesViewHolder(View itemView) {
========
    public static class DayMealsViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView img;

        private DayMealsViewHolder(View itemView) {
>>>>>>>> origin/favourites:app/src/main/java/com/example/foodplanner/features/plan/helpers/DayMealsAdapter.java
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
