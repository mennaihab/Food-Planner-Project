package com.example.foodplanner.core.helpers;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private final int spaceSize;
    private final int spanCount;
    private final int orientation;

    public MarginItemDecoration(int spaceSize, int spanCount, int orientation) {
        this.spaceSize = spaceSize;
        this.spanCount = spanCount;
        this.orientation = orientation;
    }

    public MarginItemDecoration(int spaceSize, int spanCount) {
        this(spaceSize, spanCount, GridLayoutManager.VERTICAL);
    }

    public MarginItemDecoration(int spaceSize) {
        this(spaceSize, 1, GridLayoutManager.VERTICAL);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (orientation == GridLayoutManager.VERTICAL) {
            if (parent.getChildAdapterPosition(view) < spanCount) {
                outRect.top = spaceSize;
            }
            if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                outRect.left = spaceSize;
            }
        } else {
            if (parent.getChildAdapterPosition(view) < spanCount) {
                outRect.left = spaceSize;
            }
            if (parent.getChildAdapterPosition(view) % spanCount == 0) {
                outRect.top = spaceSize;
            }
        }

        outRect.right = spaceSize;
        outRect.bottom = spaceSize;
    }
}

