package com.example.foodplanner.features.search.adapters;

import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.features.common.helpers.ItemClickListener;
import com.example.foodplanner.features.common.models.Area;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AreasListAdapter extends RecyclerView.Adapter<AreasListAdapter.ViewHolder> {
    private final AsyncListDiffer<String> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private final ItemClickListener<String> itemListener;

    public AreasListAdapter(ItemClickListener<String> itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button button = new MaterialButton(parent.getContext());
        button.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new ViewHolder(button);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mDiffer.getCurrentList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public void updateAreas(List<String> list) {
        mDiffer.submitList(list);
    }

    public static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String oldArea, @NonNull String newArea) {
            return oldArea.equals(newArea);
        }
        @Override
        public boolean areContentsTheSame(@NonNull String oldArea, @NonNull String newArea) {
            return oldArea.equals(newArea);
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        private ViewHolder(@NonNull Button itemView) {
            super(itemView);
            button = itemView;
        }

        private void bind(String area) {
            button.setText(area);
            button.setOnClickListener( e -> {
                itemListener.onClick(area);
            });
        }
    }
}
