package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.search.adapters.AreasListAdapter;
import com.example.foodplanner.features.search.adapters.CategoriesListAdapter;
import com.example.foodplanner.features.search.adapters.IngredientsListAdapter;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchAreasFragment areasList = (SearchAreasFragment) getChildFragmentManager().findFragmentById(R.id.areas_list);
        SearchCategoriesFragment categoriesList = (SearchCategoriesFragment) getChildFragmentManager().findFragmentById(R.id.categories_list);
        SearchIngredientsFragment ingredientsList = (SearchIngredientsFragment) getChildFragmentManager().findFragmentById(R.id.ingredients_list);
    }
}