package com.example.foodplanner.features.search.views;


import java.util.List;

public interface SearchAreasView {
    void updateAreas(List<String> products);
    void onLoadFailure(Throwable error);
}
