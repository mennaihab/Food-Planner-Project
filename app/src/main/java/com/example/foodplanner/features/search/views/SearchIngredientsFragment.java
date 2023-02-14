package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.search.adapters.CategoriesListAdapter;
import com.example.foodplanner.features.search.adapters.IngredientsListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchIngredientsModelImpl;
import com.example.foodplanner.features.search.presenters.SearchIngredientsPresenter;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;

public class SearchIngredientsFragment extends Fragment implements SearchIngredientsView {
    private static final String TAG = "SearchIngredientsFragment";

    private RecyclerView list;
    private IngredientsListAdapter listAdapter;

    private SearchIngredientsPresenter presenter;

    public SearchIngredientsFragment() {
        super(R.layout.search_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        presenter = new SearchIngredientsPresenter(
                getViewLifecycleOwner(),
                this,
                new SearchIngredientsModelImpl(savedInstanceState, MealRemoteService.create())
        );

        list = view.findViewById(R.id.items_list);
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 4), 1, LinearLayoutManager.HORIZONTAL)
        );
        listAdapter = new IngredientsListAdapter(area -> {
            Navigation.findNavController(view).navigate(
                    SearchFragmentDirections.actionSearchToSearchResults(new SearchCriteria(SearchCriteria.Type.INGREDIENT, area.getName()))
            );
        });
        list.setAdapter(listAdapter);
        LinearLayoutManager ingredientsLayout = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        list.setLayoutManager(ingredientsLayout);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateIngredients(List<Ingredient> products) {
        list.setVisibility(View.VISIBLE);
        listAdapter.updateIngredients(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}