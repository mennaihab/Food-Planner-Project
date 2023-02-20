package com.example.foodplanner.features.favourites.helpers;

import android.content.Context;
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
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private final AsyncListDiffer<FavouriteMealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private final FavouriteClickListener clickListener;
    private final boolean enableRemove;

    public FavouritesAdapter(boolean enableRemove, FavouriteClickListener itemListener) {
        this.clickListener = itemListener;
        this.enableRemove = enableRemove;
    }

    public void updateList(List<FavouriteMealItem> items) {
        mDiffer.submitList(items);
    }


    @NonNull
    @Override
    public FavouritesAdapter.FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.item_favourite, parent, false);
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
        private final CardView card;
        private final TextView name;
        private final ImageView img;
        private final Button remove;

        private FavouritesViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.meal_card);
            name = itemView.findViewById(R.id.meal_name);
            img = itemView.findViewById(R.id.meal_img);
            remove = itemView.findViewById(R.id.meal_remove);
            if (!enableRemove) {
                remove.setVisibility(View.GONE);
            }
        }

        private void bindData(FavouriteMealItem item) {
            name.setText(item.getMeal().getName());
            ViewUtils.loadImageInto(item.getMeal().getPreview(), img);
            card.setOnClickListener(e -> clickListener.onClick(item));
            remove.setOnClickListener(e -> clickListener.onFavourite(item));
        }
    }


    private static final DiffUtil.ItemCallback<FavouriteMealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FavouriteMealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavouriteMealItem oldArea, @NonNull FavouriteMealItem newArea) {
            return oldArea.getMeal().getId().equals(newArea.getMeal().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavouriteMealItem oldArea, @NonNull FavouriteMealItem newArea) {
            return oldArea.equals(newArea);
        }
    };

}
