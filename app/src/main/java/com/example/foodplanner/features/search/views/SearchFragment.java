package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.search.adapters.AreasListAdapter;
import com.example.foodplanner.features.search.adapters.CategoriesListAdapter;
import com.example.foodplanner.features.search.adapters.IngredientsListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;

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

        EditText searchBar = view.findViewById(R.id.search_edv);
        searchBar.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Navigation.findNavController(view).navigate(
                        SearchFragmentDirections.actionSearchToSearchResults(
                                new SearchCriteria(SearchCriteria.Type.QUERY,
                                        textView.getText().toString())));
                ViewUtils.hideKeyboard(view);
                return true;
            }
            return false;
        });
    }
}