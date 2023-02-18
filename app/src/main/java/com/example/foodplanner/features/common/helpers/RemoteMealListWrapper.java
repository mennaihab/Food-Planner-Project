package com.example.foodplanner.features.common.helpers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RemoteMealListWrapper<T> implements RemoteListWrapper<T> {
    @SerializedName("meals")
    private final List<T> items;

    public RemoteMealListWrapper(List<T> meals) {
        this.items = meals;
    }

    public List<T> getItems() {
        return items;
    }
}
