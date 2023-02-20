package com.example.foodplanner.features.mealofday.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.utils.ViewUtils;
import com.example.foodplanner.features.common.entities.MealItemEntity;
import com.example.foodplanner.features.common.helpers.mappers.BaseMapper;
import com.example.foodplanner.features.common.helpers.mappers.FavouriteMealMapper;
import com.example.foodplanner.features.common.models.FavouriteMealItem;
import com.example.foodplanner.features.common.models.MealItem;
import com.example.foodplanner.features.common.remote.MealRemoteService;
import com.example.foodplanner.features.common.remote.impl.FavouritesBackupServiceImpl;
import com.example.foodplanner.features.common.repositories.FavouriteRepository;
import com.example.foodplanner.features.common.repositories.MealItemRepository;
import com.example.foodplanner.features.common.services.AppDatabase;
import com.example.foodplanner.features.mealofday.models.MealOfDayModelImpl;
import com.example.foodplanner.features.mealofday.presenters.MealOfDayPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MealOfDayFragment extends Fragment {
    private static final String TAG = "MealOfDayFragment";
    private static final int FRAGMENTS_COUNT = 5;
    private static final String FRAGMENTS = "FRAGMENTS";

    private final List<Fragment> fragments = new ArrayList<>(FRAGMENTS_COUNT);

    public MealOfDayFragment() {
        super(R.layout.fragment_meal_of_day);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < FRAGMENTS_COUNT; i++) {
            String key = FRAGMENTS + "_" + i;
            if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
                fragments.add(getChildFragmentManager().getFragment(savedInstanceState, key));
            } else {
                fragments.add(new MealOfDayViewFragment());
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewPager2 viewPager = view.findViewById(R.id.meals_pager);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setPageTransformer(new DepthPageTransformer());
        viewPager.setAdapter(new MealOfDayFragment.MealOfDaySlideAdapter(fragments));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        for (int i = 0; i < fragments.size(); i++) {
            String key = FRAGMENTS + "_" + i;
            getChildFragmentManager().putFragment(outState, key, fragments.get(i));
        }
        super.onSaveInstanceState(outState);
    }

    private class MealOfDaySlideAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public MealOfDaySlideAdapter(List<Fragment> items) {
            super(MealOfDayFragment.this);
            fragmentList = items;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }

    public static class MealOfDayViewFragment extends Fragment implements MealOfDayView {
        private static final String TAG = "MealOfDayFirst";
        private CardView cardView;
        private TextView name;
        private ImageView image;

        private MealOfDayPresenter presenter;

        public MealOfDayViewFragment() {
            super(R.layout.fragment_meal_of_day_card);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            presenter = new MealOfDayPresenter(
                    this,
                    new MealOfDayModelImpl(
                            savedInstanceState,
                            FoodPlannerApplication.from(requireContext()).getAuthenticationManager(),
                            new MealItemRepository(
                                    MealRemoteService.create(),
                                    AppDatabase.getInstance(requireContext()).mealItemDAO(),
                                    new BaseMapper<>(MealItem.class, MealItemEntity.class)
                            ),
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
            cardView = view.findViewById(R.id.meal_card);
            name = view.findViewById(R.id.meal_name);
            image = view.findViewById(R.id.meal_img);
            presenter.init(getViewLifecycleOwner());
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            presenter.saveInstance(outState);
            super.onSaveInstanceState(outState);
        }

        @Override
        public void updateMeal(FavouriteMealItem meal) {
            name.setText(meal.getMeal().getName());
            ViewUtils.loadImageInto(meal.getMeal().getPreview(), image);
            cardView.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(MealOfDayFragmentDirections.actionGlobalToMeal(meal.getMeal().getId()));
            });
        }

        @Override
        public void onLoadFailure(Throwable error) {
            Log.e(TAG, error.getLocalizedMessage(), error);
            Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.60f;
        private static final float MAX_SCALE = 0.9f;

        public void transformPage(View view, float position) {
            float pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0f);
            } else if (position <= 0) { // [-1,0]
                view.setAlpha(1f);
                view.setTranslationZ(1f);
                view.setScaleX(MAX_SCALE);
                view.setScaleY(MAX_SCALE);
            } else if (position <= 3) { // (0,3]
                view.setAlpha(1f);
                view.setTranslationX(0.925f * pageWidth * -position);
                view.setTranslationZ(-position);
                float scaleFactor = MIN_SCALE + (MAX_SCALE - MIN_SCALE) * Math.abs(3f - position) / 3f;
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0f);
            }
        }
    }
}