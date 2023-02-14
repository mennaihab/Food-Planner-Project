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

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private final AsyncListDiffer<MealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);
    private final Context context;

    public FavouritesAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<MealItem> items) {
        mDiffer.submitList(items);
    }


    @NonNull
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.favourite_item, parent, false);
        return new FavouritesAdapter.FavouritesViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(final FavouritesAdapter.FavouritesViewHolder viewHolder, final int position) {
        viewHolder.bindData(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public class FavouritesViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final ImageView img;

        private FavouritesViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
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
