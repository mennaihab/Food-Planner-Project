package com.example.foodplanner.features.search.views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodplanner.R;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.entities.CategoryEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.Category;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.AreaRepository;
import com.example.foodplanner.features.common.repositories.CategoryRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.search.adapters.CategoriesListAdapter;
import com.example.foodplanner.features.search.helpers.SearchCriteria;
import com.example.foodplanner.features.search.models.SearchCategoriesModelImpl;
import com.example.foodplanner.features.search.presenters.SearchCategoriesPresenter;

import java.util.List;

public class SearchCategoriesFragment extends Fragment implements SearchCategoriesView {
    private static final String TAG = "SearchIngredientsFragment";

    private RecyclerView list;
    private CategoriesListAdapter listAdapter;
    private SearchCategoriesPresenter presenter;

    public SearchCategoriesFragment() {
        super(R.layout.items_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchCategoriesPresenter(
                this,
                new SearchCategoriesModelImpl(savedInstanceState,
                        new CategoryRepository(
                                MealRemoteService.create(),
                                AppDatabase.getInstance(requireContext()).categoryDAO(),
                                new BaseMapper<>(Category.class, CategoryEntity.class)
                        )
                )
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        list = view.findViewById(R.id.items_list);
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 8), 2)
        );
        listAdapter = new CategoriesListAdapter(area -> {
            Navigation.findNavController(view).navigate(
                    SearchFragmentDirections.actionSearchToSearchResults(new SearchCriteria(SearchCriteria.Type.CATEGORY, area.getName()))
            );
        });
        list.setAdapter(listAdapter);
        GridLayoutManager categoriesLayout = new GridLayoutManager(requireContext(), 2);
        list.setLayoutManager(categoriesLayout);

        presenter.init(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateCategories(List<Category> products) {
        list.setVisibility(View.VISIBLE);
        listAdapter.updateCategories(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}