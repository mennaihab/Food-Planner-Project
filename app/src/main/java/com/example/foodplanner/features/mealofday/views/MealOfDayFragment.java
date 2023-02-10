package com.example.foodplanner.features.mealofday.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.OnBackPressedListener;

import java.util.Arrays;
import java.util.List;

public class MealOfDayFragment extends Fragment implements OnBackPressedListener {
    public MealOfDayFragment() {
        super(R.layout.fragment_meal_of_day);
    }

    private ViewPager2 viewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(6);
        viewPager.setPageTransformer(new DepthPageTransformer());
        viewPager.setAdapter(new MealOfDayFragment.MealOfDaySlideAdapter(Arrays.asList(
                new MealOfDayFragment.MealOfDayFirstFragment(),
                new MealOfDayFragment.MealOfDayFirstFragment(),
                new MealOfDayFragment.MealOfDayFirstFragment()
        )));
    }

    @Override
    public boolean onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            return true;
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return false;
        }
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

    public static class MealOfDayFirstFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_meal_of_day_card,
                    container, false);

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