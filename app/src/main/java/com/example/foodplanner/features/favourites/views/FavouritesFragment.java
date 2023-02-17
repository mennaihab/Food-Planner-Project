package com.example.foodplanner.features.favourites.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.NavigationUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.AreaEntity;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.models.Area;
import com.example.foodplanner.features.common.models.Meal;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.repositories.AreaRepository;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.favourites.helpers.FavouriteClickListener;
import com.example.foodplanner.features.favourites.helpers.FavouritesAdapter;
import com.example.foodplanner.features.favourites.models.FavouriteMealsModelImpl;
import com.example.foodplanner.features.favourites.presenters.FavouritesPresenter;
import com.example.foodplanner.features.search.models.SearchAreasModelImpl;
import com.example.foodplanner.features.search.presenters.SearchAreasPresenter;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment implements FavouritesView {
    private static final String TAG = "FavouritesFragment";

    private FavouritesPresenter presenter;
    private RecyclerView recyclerView;
    private FavouritesAdapter ItemAdapter;

    public FavouritesFragment() {
        super(R.layout.fragment_favourites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new FavouritesPresenter(
                getViewLifecycleOwner(),
                this,
                new FavouriteMealsModelImpl(
                        savedInstanceState,
                        FoodPlannerApplication.from(requireContext()).getAuthenticationManager(),
                        new FavouriteRepository(
                                AppDatabase.getInstance(requireContext()).favouriteMealDAO(),
                                new BaseMapper<>(MealItem.class, MealItemEntity.class)
                        )
                )
        );

        String selectionResultKey = FavouritesFragmentArgs.fromBundle(requireArguments()).getSelectionResultKey();

        recyclerView = view.findViewById(R.id.items_list);
        recyclerView.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 16), 2, LinearLayoutManager.VERTICAL)
        );

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        ItemAdapter = new FavouritesAdapter(selectionResultKey == null, new FavouriteClickListener() {
            @Override
            public void onFavourite(MealItem item) {
                presenter.updateFavourite(item);
            }

            @Override
            public void onClick(MealItem item) {
                if (selectionResultKey == null) {
                    // TODO navigate to MealDetails
                } else {
                    NavigationUtils.setResult(view, selectionResultKey, item);
                    NavigationUtils.navigateUp(view);
                }
            }
        });

        recyclerView.setAdapter(ItemAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateFavourites(List<MealItem> products) {
        recyclerView.setVisibility(View.VISIBLE);
        ItemAdapter.updateList(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        Log.e(TAG, error.getLocalizedMessage(), error);
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavouriteSuccess(MealItem mealItem) {

    }

    @Override
    public void onFavouriteFailure(MealItem mealItem, Throwable error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}