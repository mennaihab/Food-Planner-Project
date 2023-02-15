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
import com.example.foodplanner.features.common.models.Ingredient;

import java.util.List;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {
    private final AsyncListDiffer<Ingredient> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private final ItemClickListener<Ingredient> itemListener;
    private final int layout;

    public IngredientsListAdapter(ItemClickListener<Ingredient> itemListener,int layout) {
        this.itemListener = itemListener;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
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

    public void updateIngredients(List<Ingredient> list) {
        mDiffer.submitList(list);
    }

    public static final DiffUtil.ItemCallback<Ingredient> DIFF_CALLBACK = new DiffUtil.ItemCallback<Ingredient>() {
        @Override
        public boolean areItemsTheSame(@NonNull Ingredient oldIng, @NonNull Ingredient newIng) {
            return oldIng.getId().equals(newIng.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Ingredient oldIng, @NonNull Ingredient newIng) {
            return oldIng.equals(newIng);
        }
    };



    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final CardView imageWrapper;
        private final TextView name;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.ingredient_name);
            imageWrapper = itemView.findViewById(R.id.item_image_wrapper);
        }

        private void bind(Ingredient ingredient) {
            name.setText(ingredient.getName());
            itemView.setOnClickListener(e -> {
                itemListener.onClick(ingredient);
            });
            Glide.with(itemView)
                    .asBitmap()
                    .load("https://www.themealdb.com/images/ingredients/" + ingredient.getName() + "-Small.png") // TODO: extract
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
                                imageWrapper.setCardBackgroundColor(palette.getDominantColor(itemView.getContext().getColor(android.R.color.transparent)));
                            }
                            return false;
                        }
                    })
                    .into(image);
        }
    }
}
