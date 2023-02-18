package com.example.foodplanner.features.search.helpers;

import com.example.foodplanner.features.common.helpers.RemoteListWrapper;
import com.example.foodplanner.features.common.models.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteCategoriesWrapper implements RemoteListWrapper<Category> {
    @SerializedName("categories")
    private final List<Category> items;

    public RemoteCategoriesWrapper(List<Category> items) {
        this.items = items;
    }

    public List<Category> getItems() {
        return items;
    }
}
