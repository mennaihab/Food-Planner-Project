package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.GeneralUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.entities.IngredientEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.Ingredient;
import com.example.foodplanner.features.common.repositories.AreaRepository;
import com.example.foodplanner.features.common.repositories.IngredientRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.search.adapters.IngredientsListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchIngredientsModelImpl;
import com.example.foodplanner.features.search.presenters.SearchIngredientsPresenter;
import com.example.foodplanner.features.common.remote.MealRemoteService;

import java.util.List;

public class SearchIngredientsFragment extends Fragment implements SearchIngredientsView {
    private static final String TAG = "SearchIngredientsFragment";

    private RecyclerView list;

    private ProgressBar loader;

    private TextView errorTv;
    private IngredientsListAdapter listAdapter;

    private SearchIngredientsPresenter presenter;

    public SearchIngredientsFragment() {
        super(R.layout.items_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchIngredientsPresenter(
                this,
                new SearchIngredientsModelImpl(
                        savedInstanceState,
                        new IngredientRepository(
                                MealRemoteService.create(),
                                AppDatabase.getInstance(requireContext()).ingredientDAO(),
                                new BaseMapper<>(Ingredient.class, IngredientEntity.class)
                        )
                )
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        list = view.findViewById(R.id.items_list);
        loader = view.findViewById(R.id.items_loader);
        errorTv = view.findViewById(R.id.items_error_tv);
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 4), 1, LinearLayoutManager.HORIZONTAL)
        );
        listAdapter = new IngredientsListAdapter(area -> Navigation.findNavController(view).navigate(
                SearchFragmentDirections.actionSearchToSearchResults(new SearchCriteria(SearchCriteria.Type.INGREDIENT, area.getName()))
        ),R.layout.item_v);
        list.setAdapter(listAdapter);
        LinearLayoutManager ingredientsLayout = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        list.setLayoutManager(ingredientsLayout);

        presenter.init(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateIngredients(List<Ingredient> products) {
        list.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.GONE);
        listAdapter.updateIngredients(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        list.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(GeneralUtils.getErrorMessage(error));
        Toast.makeText(getActivity(), GeneralUtils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
    }
}