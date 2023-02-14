package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.search.adapters.IngredientsListAdapter;
import com.example.foodplanner.features.search.adapters.SearchListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchIngredientsModelImpl;
import com.example.foodplanner.features.search.models.SearchResultsModelImpl;
import com.example.foodplanner.features.search.presenters.SearchIngredientsPresenter;
import com.example.foodplanner.features.search.presenters.SearchResultsPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Optional;

public class SearchResultsFragment extends Fragment implements SearchResultsView {
    private static final String TAG = "SearchResultsFragment";

    private RecyclerView list;
    private SearchListAdapter listAdapter;

    private SearchResultsPresenter presenter;

    public SearchResultsFragment() {
        super(R.layout.fragment_search_results);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SearchCriteria criteria = SearchResultsFragmentArgs.fromBundle(requireArguments()).getCriteria();

        presenter = new SearchResultsPresenter(
                getViewLifecycleOwner(),
                this,
                new SearchResultsModelImpl(
                        savedInstanceState,
                        criteria,
                        MealRemoteService.create()
                )
        );

        list = view.findViewById(R.id.items_list);
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 16), 2, LinearLayoutManager.VERTICAL)
        );
        listAdapter = new SearchListAdapter();
        list.setAdapter(listAdapter);
        LinearLayoutManager ingredientsLayout = new GridLayoutManager(requireContext(), 2);
        list.setLayoutManager(ingredientsLayout);

        TextInputLayout searchBarLayout = view.findViewById(R.id.search_edl);
        EditText searchBar = view.findViewById(R.id.search_edv);
        Chip criteriaChip = view.findViewById(R.id.search_criteria);
        if (criteria.getType() == SearchCriteria.Type.QUERY) {
            criteriaChip.setVisibility(View.GONE);
            if (savedInstanceState == null) {
                searchBar.setText(criteria.getCriteria());
            }
            searchBar.setOnEditorActionListener((textView, actionId, keyEvent) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.filter(new SearchCriteria(criteria.getType(), textView.getText().toString()));
                    ViewUtils.hideKeyboard(view);
                    return true;
                }
                return false;
            });
        } else {
            searchBarLayout.setVisibility(View.GONE);
            criteriaChip.setCloseIconVisible(false);
            criteriaChip.setText(criteria.getCriteria());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateResults(Optional<List<MealItem>> results) {
        if (results.isPresent()) {
            list.setVisibility(View.VISIBLE);
            listAdapter.updateIngredients(results.get());
        } else {
            list.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoadFailure(Throwable error) {
        list.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}