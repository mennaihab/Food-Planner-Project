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
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.helpers.MarginItemDecoration;
import com.example.foodplanner.core.utils.GeneralUtils;
import com.example.foodplanner.core.utils.NavigationUtils;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.FavouriteMealMapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.impl.FavouritesBackupServiceImpl;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.favourites.helpers.FavouriteClickListener;
import com.example.foodplanner.features.favourites.helpers.FavouritesAdapter;
import com.example.foodplanner.features.favourites.models.FavouriteMealsModelImpl;
import com.example.foodplanner.features.favourites.presenters.FavouritesPresenter;

import java.util.List;

public class FavouritesFragment extends Fragment implements FavouritesView {
    private static final String TAG = "FavouritesFragment";
    public static final String SELECTED_MEAL = "SELECTED_MEAL";

    private FavouritesPresenter presenter;
    private RecyclerView list;
    private ProgressBar loader;
    private TextView errorTv;
    private FavouritesAdapter itemAdapter;

    public FavouritesFragment() {
        super(R.layout.fragment_favourites);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new FavouritesPresenter(
                this,
                new FavouriteMealsModelImpl(
                        savedInstanceState,
                        FoodPlannerApplication.from(requireContext()).getAuthenticationManager(),
                        new FavouriteRepository(
                                AppDatabase.getInstance(requireContext()).favouriteMealDAO(),
                                AppDatabase.getInstance(requireContext()).mealItemDAO(),
                                new FavouritesBackupServiceImpl(FoodPlannerApplication.from(requireContext()).getFirestore()),
                                new FavouriteMealMapper(new BaseMapper<>(MealItem.class, MealItemEntity.class))
                        )
                )
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean pickItem = FavouritesFragmentArgs.fromBundle(requireArguments()).getPickItem();

        list = view.findViewById(R.id.items_list);
        loader = view.findViewById(R.id.items_loader);
        errorTv = view.findViewById(R.id.items_error_tv);

        list.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        list.addItemDecoration(
                new MarginItemDecoration(ViewUtils.dpToPx(requireContext(), 16), 2, LinearLayoutManager.VERTICAL)
        );

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        itemAdapter = new FavouritesAdapter(!pickItem, new FavouriteClickListener() {
            @Override
            public void onFavourite(FavouriteMealItem item) {
                presenter.updateFavourite(item);
            }

            @Override
            public void onClick(FavouriteMealItem item) {
                Log.d(TAG, "onClick: " + item);
                if (!pickItem) {
                    Navigation.findNavController(view).navigate(FavouritesFragmentDirections.actionGlobalToMeal(item.getMeal().getId()));
                } else {
                    NavigationUtils.setResult(view, SELECTED_MEAL, item.getMeal());
                    NavigationUtils.navigateUp(view);
                }
            }
        });

        list.setAdapter(itemAdapter);
        list.setLayoutManager(layoutManager);

        presenter.init(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveInstance(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateFavourites(List<FavouriteMealItem> products) {
        Log.d(TAG, "updateFavourites: " + products);
        if (products.isEmpty()) {
            list.setVisibility(View.GONE);
            loader.setVisibility(View.GONE);
            errorTv.setVisibility(View.VISIBLE);
            errorTv.setText(R.string.no_items_found);
        } else {
            list.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
            errorTv.setVisibility(View.GONE);
        }
        itemAdapter.submitList(products);
    }

    @Override
    public void onLoadFailure(Throwable error) {
        list.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(GeneralUtils.getErrorMessage(error));
        Toast.makeText(getActivity(), GeneralUtils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavouriteSuccess(FavouriteMealItem mealItem) {

    }

    @Override
    public void onFavouriteFailure(FavouriteMealItem mealItem, Throwable error) {
        Toast.makeText(getActivity(), GeneralUtils.getErrorMessage(error), Toast.LENGTH_SHORT).show();
    }
}