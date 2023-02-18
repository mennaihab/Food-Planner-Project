package com.example.foodplanner.features.search.adapters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.example.foodplanner.R;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private final AsyncListDiffer<MealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private final SearchClickListener clickListener;

    public SearchListAdapter(SearchClickListener clickListener) {
        this.clickListener = clickListener;
    }

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

    private static final DiffUtil.ItemCallback<MealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull MealItem oldIng, @NonNull MealItem newIng) {
            return oldIng.getId().equals(newIng.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MealItem oldIng, @NonNull MealItem newIng) {
            return oldIng.equals(newIng);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView card;
        private final ImageView image;
        private final TextView name;
        private final Button favourite;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.meal_img);
            name = itemView.findViewById(R.id.meal_name);
            favourite = itemView.findViewById(R.id.meal_favourite);
            card = itemView.findViewById(R.id.meal_card);
        }

        private void bind(MealItem item) {
            name.setText(item.getName());
            Glide.with(itemView)
                    .asBitmap()
                    .load(item.getThumbnail() + "/preview") // TODO: extract
                    .placeholder(R.drawable.ic_launcher_foreground) // TODO: change
                    .error(R.drawable.ic_launcher_background) // TODO: change
                    .into(image);

            Glide.with(favourite)
                    .load(item.isFavourite() ? R.drawable.favourite : R.drawable.ic_favorite_border)
                    .into(new RequestFutureTarget<Drawable>(favourite.getWidth(), favourite.getHeight()) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            favourite.setBackground(resource);
                        }
                    });

            card.setOnClickListener(e -> clickListener.onClick(item));
            favourite.setOnClickListener(e -> clickListener.onFavourite(item));
        }
    }
}
