package com.example.foodplanner.features.landing.views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;
import com.example.foodplanner.core.FoodPlannerApplication;
import com.example.foodplanner.core.helpers.TextSpan;
import com.example.foodplanner.core.utils.SpanUtils;
import com.example.foodplanner.core.utils.TextUtils;
import com.example.foodplanner.features.common.views.OnBackPressedListener;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LandingFragment extends Fragment implements OnBackPressedListener {

    private ViewPager2 viewPager;

    public LandingFragment() {
        super(R.layout.fragment_landing);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.landing_pager);
        Button nextBtn = view.findViewById(R.id.next_btn);
        Button getStartedBtn  = view.findViewById(R.id.get_started_btn);
        TextView skipTV = view.findViewById(R.id.skip_tv);

        viewPager.setAdapter(new LandingPageSlideAdapter(Arrays.asList(
                LandingPageFragment.newInstance(R.layout.fragment_landing_first),
                LandingPageFragment.newInstance(R.layout.fragment_landing_second),
                LandingPageFragment.newInstance(R.layout.fragment_landing_third)
        )));

        ImageView[] dots = new ImageView[] {view.findViewById(R.id.dot1), view.findViewById(R.id.dot2), view.findViewById(R.id.dot3)};

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                boolean lastPage = position == Objects.requireNonNull(viewPager.getAdapter()).getItemCount() - 1;
                for (int i = 0; i < viewPager.getAdapter().getItemCount(); i++) {
                    if (lastPage) {
                        dots[i].setVisibility(View.GONE);
                    } else {
                        boolean selected = position == i;
                        dots[i].setVisibility(View.VISIBLE);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), selected ? R.drawable.tab_indicator_selected : R.drawable.tab_indicator_default));
                    }
                }

                if (lastPage) {
                    getStartedBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.GONE);
                    skipTV.setVisibility(View.GONE);
                } else {
                    nextBtn.setVisibility(View.VISIBLE);
                    skipTV.setVisibility(View.VISIBLE);
                    getStartedBtn.setVisibility(View.GONE);
                }
            }
        });


        nextBtn.setOnClickListener(e -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));
        getStartedBtn.setOnClickListener(this::gotoAuthentication);
        underlineSkip(skipTV);
    }

    @Override
    public boolean onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            return false;
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            return true;
        }
    }

    public void underlineSkip(TextView textView){
        textView.setText(SpanUtils.createSpannable("SKIP", TextSpan.of(SpanUtils.createClickableSpan(this::gotoAuthentication))));
        TextUtils.makeClickable(textView);
    }

    private void gotoAuthentication(View view) {
        FoodPlannerApplication.from(requireContext()).getSettingsManager().setHasShownLanding(true);
        Navigation.findNavController(view).navigate(LandingFragmentDirections.actionLandingToAuthentication());
    }

    private class LandingPageSlideAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public LandingPageSlideAdapter(List<Fragment> items) {
            super(LandingFragment.this);
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

    public static class LandingPageFragment extends Fragment {
        private static final String LAYOUT = "layout";
        public static LandingPageFragment newInstance(@LayoutRes int layoutResource) {
            LandingPageFragment fragment = new LandingPageFragment();
            Bundle args = new Bundle();
            args.putInt(LAYOUT, layoutResource);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(requireArguments().getInt(LAYOUT), container, false);
        }
    }
}


