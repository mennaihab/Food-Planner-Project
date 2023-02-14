package com.example.foodplanner.features.search.adapters;

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
import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.Category;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {
    private final AsyncListDiffer<Category> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private final ItemClickListener<Category> itemListener;

    public CategoriesListAdapter(ItemClickListener<Category> itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_h, parent, false);
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

    public void updateCategories(List<Category> list) {
        mDiffer.submitList(list);
    }

    public static final DiffUtil.ItemCallback<Category> DIFF_CALLBACK = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldCat, @NonNull Category newCat) {
            return oldCat.getId().equals(newCat.getId());
        }
        @Override
        public boolean areContentsTheSame(@NonNull Category oldCat, @NonNull Category newCat) {
            return oldCat.equals(newCat);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView itemWrapper;
        private final ImageView image;
        private final TextView name;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
            itemWrapper = itemView.findViewById(R.id.item_wrapper);
        }

        private void bind(Category category) {
            name.setText(category.getName());
            itemView.setOnClickListener(e -> {
                itemListener.onClick(category);
            });
            Glide.with(itemView)
                    .asBitmap()
                 .load(category.getThumbnail())
                 .placeholder(R.drawable.ic_launcher_foreground) // TODO: change
                 .error(R.drawable.ic_launcher_background) // TODO: change
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource != null) {
                                Palette palette = Palette.from(resource).generate();
                                Palette.Swatch swatch = palette.getDominantSwatch();
                                if (swatch == null) {
                                    swatch = palette.getVibrantSwatch();
                                }
                                if (swatch == null) {
                                    swatch = palette.getSwatches().stream().findFirst().orElse(null);
                                }
                                if (swatch != null) {
                                    itemWrapper.setCardBackgroundColor(swatch.getRgb());
                                    name.setTextColor(swatch.getTitleTextColor());
                                }
                            }
                            return false;
                        }
                    })
                 .into(image);
        }
    }
}
