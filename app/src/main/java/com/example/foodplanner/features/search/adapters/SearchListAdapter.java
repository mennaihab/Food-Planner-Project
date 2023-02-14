package com.example.foodplanner.features.search.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private final AsyncListDiffer<MealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void updateIngredients(List<MealItem> list) {
        mDiffer.submitList(list);
    }

    public static final DiffUtil.ItemCallback<MealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MealItem oldIng, @NonNull MealItem newIng) {
            return oldIng.getId().equals(newIng.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MealItem oldIng, @NonNull MealItem newIng) {
            return oldIng.equals(newIng);
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView name;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.meal_img);
            name = itemView.findViewById(R.id.meal_name);
            itemView.findViewById(R.id.meal_rating).setVisibility(View.GONE);
        }

        private void bind(MealItem ingredient) {
            name.setText(ingredient.getName());
            Glide.with(itemView)
                    .asBitmap()
                    .load(ingredient.getThumbnail() + "/preview") // TODO: extract
                    .placeholder(R.drawable.ic_launcher_foreground) // TODO: change
                    .error(R.drawable.ic_launcher_background) // TODO: change
                    .into(image);
        }
    }
}
