package com.example.foodplanner.features.common.helpers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteMealWrapper<T> implements RemoteModelWrapper<T> {
    @SerializedName("meals")
    private final List<T> items;

    public RemoteMealWrapper(List<T> meals) {
        this.items = meals;
    }

    public List<T> getItems() {
        return items;
    }
}
