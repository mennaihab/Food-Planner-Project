package com.example.foodplanner.features.search.adapters;

import android.graphics.Bitmap;
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
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.foodplanner.R;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {
    private final AsyncListDiffer<FavouriteMealItem> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

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

    public void updateIngredients(List<FavouriteMealItem> list) {
        mDiffer.submitList(list);
    }

    private static final DiffUtil.ItemCallback<FavouriteMealItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<FavouriteMealItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull FavouriteMealItem oldIng, @NonNull FavouriteMealItem newIng) {
            return oldIng.getMeal().getId().equals(newIng.getMeal().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FavouriteMealItem oldIng, @NonNull FavouriteMealItem newIng) {
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

        private void bind(FavouriteMealItem item) {
            name.setText(item.getMeal().getName());
            ViewUtils.loadImageInto(item.getMeal().getPreview(), image);
            ViewUtils.loadImageInto((item.isFavourite() ? R.drawable.favourite : R.drawable.ic_favorite_border), favourite);
            card.setOnClickListener(e -> clickListener.onClick(item));
            favourite.setOnClickListener(e -> clickListener.onFavourite(item));
        }
    }
}
