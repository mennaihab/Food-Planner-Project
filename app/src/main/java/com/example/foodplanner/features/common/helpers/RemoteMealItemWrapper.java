package com.example.foodplanner.features.common.helpers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteMealItemWrapper<T> implements RemoteItemWrapper<T> {
    @SerializedName("meals")
    private final List<T> items;

    public RemoteMealItemWrapper(List<T> meals) {
        this.items = meals;
    }

    public T getItem() {
        if (items == null || items.isEmpty()) return null;
        return items.get(0);
    }
}
