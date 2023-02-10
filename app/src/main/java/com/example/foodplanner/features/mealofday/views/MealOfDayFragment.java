package com.example.foodplanner.features.mealofday.views;

import static java.lang.Math.abs;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foodplanner.R;
import com.example.foodplanner.features.common.views.OnBackPressedListener;
import com.example.foodplanner.features.landing.views.LandingFragment;

import java.util.Arrays;
import java.util.List;

public class MealOfDayFragment extends Fragment implements OnBackPressedListener {
    public MealOfDayFragment(){
        super(R.layout.fragment_meal_of_day);
    }

    private ViewPager2 viewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.pager);

        viewPager.setOffscreenPageLimit(3);
        //viewPager.setClipToPadding(false);
        //viewPager.setClipChildren(false);
        //viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        //CompositePageTransformer transformer = new CompositePageTransformer();
        //transformer.addTransformer(new MarginPageTransformer(40));
        //transformer.addTransformer();
        viewPager.setPageTransformer(new DepthPageTransformer());
      /*  int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.tran);
        int peekMarginPx = getResources().getDimensionPixelOffset(R.dimen.tran);
        RecyclerView rv = (RecyclerView) viewPager.getChildAt(0);
        rv.setClipToPadding(false);
        rv.setClipChildren(false);
        int padding = peekMarginPx + pageMarginPx;
       rv.setPadding(padding, 0, -padding, 0);
       */

        viewPager.setAdapter(new MealOfDayFragment.MealOfDaySlideAdapter(Arrays.asList(
                new MealOfDayFragment.MealOfDayFirstFragment(),
                new MealOfDayFragment.MealOfDayFirstFragment(),
                new MealOfDayFragment.MealOfDayFirstFragment()
        )));
    }

        @Override
        public boolean onBackPressed () {
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
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_meal_of_day_first,
                    container, false);
       // public MealOfDayFirstFragment() {
         //   super(R.layout.fragment_meal_of_day_first);
        }
    }

    private static class DepthPageTransformer implements ViewPager2.PageTransformer {
        public void transformPage(View view, float position) {


             float DEFAULT_TRANSLATION_X = .0f;
             float DEFAULT_TRANSLATION_FACTOR = 1.2f;
             float SCALE_FACTOR = .14f;
             float DEFAULT_SCALE = 1f;
             float DEFAULT_ALPHA = 1f;
             float scaleFactor = SCALE_FACTOR * position + DEFAULT_SCALE;

         if (position <=2 ) {

            view.setScaleX( scaleFactor);
            view.setScaleY( scaleFactor);
            view.setTranslationX(- (view.getWidth() / DEFAULT_TRANSLATION_FACTOR) *(position));
            view.setAlpha( DEFAULT_ALPHA);
        }
        else {
            view.setTranslationX(DEFAULT_TRANSLATION_X);
            view.setScaleX( DEFAULT_SCALE);
            view.setScaleY( DEFAULT_SCALE);
            view.setAlpha(DEFAULT_ALPHA);

        }
}}}