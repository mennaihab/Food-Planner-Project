package com.example.foodplanner.features.search.helpers;

import com.example.foodplanner.features.common.helpers.RemoteModelWrapper;
import com.example.foodplanner.features.common.models.Category;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteCategoriesWrapper implements RemoteModelWrapper<Category> {
    @SerializedName("categories")
    private final List<Category> items;

    public RemoteCategoriesWrapper(List<Category> items) {
        this.items = items;
    }

    public List<Category> getItems() {
        return items;
    }
}
